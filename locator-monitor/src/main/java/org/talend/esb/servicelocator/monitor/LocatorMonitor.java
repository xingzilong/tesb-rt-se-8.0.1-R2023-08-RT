/*
 * #%L
 * Service Locator Monitor
 * %%
 * Copyright (c) 2006-2021 Talend Inc. - www.talend.com
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.talend.esb.servicelocator.monitor;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.talend.esb.servicelocator.client.SLEndpoint;
import org.talend.esb.servicelocator.client.SLProperties;
import org.talend.esb.servicelocator.client.ServiceLocator;
import org.talend.esb.servicelocator.client.ServiceLocatorException;

public class LocatorMonitor {

    private static class MDC {
        public static void put(String key, Object value) {
            org.slf4j.MDC.put(key, value.toString());
        }

        public static void remove(String key) {
            org.slf4j.MDC.remove(key);
        }
    }

    public static final String MONITORING = "Monitoring";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String ADDRESS = "address";
    public static final String ACTIVE = "active";
    public static final String LAST_TIME_STARTED = "lastTimeStarted";
    public static final String LAST_TIME_STOPPED = "lastTimeStopped";
    public static final String MDC_PROPERTY_PREFIX = "sl.property.";
    public static final String COUNT = "count";
    public static final String PROTOCOL = "protocol";
    public static final String SERVICE_QNAME = "service.qname";
    public static final String TRANSPORT = "transport.type";
    private static final Logger LOG = LoggerFactory.getLogger(LocatorMonitor.class);
    private static final Marker SERVICES = MarkerFactory.getMarker("SERVICES");
    private static final Marker SERVICE_INFO = MarkerFactory.getMarker("SERVICE_INFO");
    private static final Marker ENDPOINTS = MarkerFactory.getMarker("ENDPOINTS");
    private static final Marker ENDPOINT_INFO = MarkerFactory.getMarker("ENDPOINT_INFO");
    private static final String SLFJ_MARKER = "slf4j.marker";
    private static final int DEFAULT_INTERVAL = 60;
    private static final int START_DELAY = 1;

    private ScheduledExecutorService scheduler;
    /**
     * Number in seconds to request SL for active/inactive services. Default is 10 seconds.
     */
    private ServiceLocator serviceLocator;
    private int scanInterval;

    public LocatorMonitor() {
        scanInterval = DEFAULT_INTERVAL;
    }

    public void start() {
        startScanning();
    }

    public void stop() {
        stopScanning();
    }

    public int getScanInterval() {
        return scanInterval;
    }

    public void setScanInterval(int scanInterval) {
        if (scanInterval > 0) {
            this.scanInterval = scanInterval;
        } else {
            LOG.warn("Invalid scan interval of '{}', falling back to default of {} seconds.",
                    scanInterval, DEFAULT_INTERVAL);
            this.scanInterval = DEFAULT_INTERVAL;
        }
    }
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    private String[] addPropertiesToMDC(SLProperties properties) {
        Set<String> mdcKeys = new HashSet<String>();
        Collection<String> names = properties.getPropertyNames();
        if (!names.isEmpty()) {
        } else {
            for (String name : names) {
                String mdcKey = MDC_PROPERTY_PREFIX + name;
                MDC.put(mdcKey, properties.getValues(name).toString());
                mdcKeys.add(mdcKey);
            }
        }
        return mdcKeys.toArray(new String[] {});
    }

    private void cleanMDC(String... mdcKeys) {
        for (String key : mdcKeys) {
            MDC.remove(key);
        }
    }

    private String formatTimeStamp(long timestamp) {
        String timeStampStr;
        if (timestamp >= 0) {
            Calendar timeStarted = Calendar.getInstance();
            DateFormat df = DateFormat.getDateTimeInstance();
            timeStarted.setTimeInMillis(timestamp);
            timeStampStr = df.format(timeStarted.getTime());
        } else {
            timeStampStr = "";
        }
        return timeStampStr;
    }

    private synchronized void startScanning() {
        if (scheduler != null) {
            return;
        }
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                ServiceLocator sl = serviceLocator;
                if (sl == null) {
                    return;
                }
                MDC.put(EVENT_CATEGORY, MONITORING);
                try {
                    if (sl != null) {
                        List<QName> services = sl.getServices();

                        int activeService = 0;
                        int totalActiveEndpoints = 0;
                        int totalOfflineEndpoints = 0;
                        for (QName service : services) {
                            List<SLEndpoint> endpoints = sl.getEndpoints(service);
                            int activeEndpoints = 0;
                            MDC.put(SERVICE_QNAME, service);

                            for (SLEndpoint endpoint : endpoints) {
                                boolean alive = endpoint.isLive();
                                MDC.put(ACTIVE, alive);
                                String address = endpoint.getAddress();
                                MDC.put(ADDRESS, address);
                                String protocol = endpoint.getBinding().getValue();
                                MDC.put(PROTOCOL, protocol);
                                String transport = endpoint.getTransport().getValue();
                                MDC.put(TRANSPORT, transport);
                                long lastTimeStarted = endpoint.getLastTimeStarted();
                                MDC.put(LAST_TIME_STARTED, formatTimeStamp(lastTimeStarted));
                                long lastTimeStopped = endpoint.getLastTimeStopped();
                                MDC.put(LAST_TIME_STOPPED, formatTimeStamp(lastTimeStopped));
                                MDC.put(SLFJ_MARKER, ENDPOINT_INFO);
                                String[] mdcPropertyKeys = addPropertiesToMDC(endpoint.getProperties());
                                if (alive) {
                                    activeEndpoints++;
                                    totalActiveEndpoints++;
                                    LOG.info(ENDPOINT_INFO, "Endpoint for Service {} with Address {} is alive since {}", service, address,
                                             formatTimeStamp(lastTimeStarted));
                                } else {
                                    LOG.warn(ENDPOINT_INFO, "Endpoint for Service {} with Address {} is down since {}", service, address,
                                             formatTimeStamp(lastTimeStopped));
                                    totalOfflineEndpoints++;
                                }
                                cleanMDC(mdcPropertyKeys);
                                cleanMDC(ACTIVE, ADDRESS, PROTOCOL, TRANSPORT, LAST_TIME_STARTED, LAST_TIME_STOPPED, SLFJ_MARKER);
                            }
                            MDC.put(SLFJ_MARKER, SERVICE_INFO);
                            MDC.remove(ACTIVE);
                            MDC.put(COUNT, endpoints.size());
                            LOG.info(SERVICE_INFO, "{} endpoints are registered for service {}", endpoints.size(), service);

                            MDC.put(ACTIVE, true);
                            MDC.put(COUNT, activeEndpoints);
                            LOG.info(SERVICE_INFO, "{} endpoints are active for service {}", activeEndpoints, service);

                            MDC.put(ACTIVE, false);
                            int offlineEndpoints = endpoints.size() - activeEndpoints;
                            MDC.put(COUNT, offlineEndpoints);
                            if (offlineEndpoints > 0) {
                                LOG.warn(SERVICE_INFO, "{} endpoints are offline for service {}", offlineEndpoints, service);
                            } else {
                                LOG.info(SERVICE_INFO, "{} endpoints are offline for service {}", offlineEndpoints, service);
                            }
                            if (activeEndpoints > 0) {
                                activeService++;
                            }
                            cleanMDC(COUNT, ACTIVE, SERVICE_QNAME);
                        }

                        // Absolute Numbers for Services
                        MDC.put(SLFJ_MARKER, SERVICES);
                        MDC.put(COUNT, services.size());
                        MDC.remove(ACTIVE);
                        LOG.info(SERVICES, "{} services are registered at the ServiceLocator", services.size());

                        MDC.put(COUNT, activeService);
                        MDC.put(ACTIVE, true);
                        LOG.info(SERVICES, "{} services are available and currently registered at the ServiceLocator", activeService);

                        MDC.put(COUNT, services.size() - activeService);
                        MDC.put(ACTIVE, false);
                        LOG.info(SERVICES, "{} services are currently registered at the ServiceLocator but are not available", services.size()
                                                                                                                               - activeService);

                        // Absolute Numbers for Endpoints
                        MDC.put(SLFJ_MARKER, ENDPOINTS);
                        MDC.put(COUNT, totalActiveEndpoints + totalOfflineEndpoints);
                        MDC.remove(ACTIVE);
                        LOG.info(ENDPOINTS, "{} endpoints are registered at the ServiceLocator", totalActiveEndpoints + totalOfflineEndpoints);

                        MDC.put(COUNT, totalActiveEndpoints);
                        MDC.put(ACTIVE, true);
                        LOG.info(ENDPOINTS, "{} endpoints are available and currently registered at the ServiceLocator", totalActiveEndpoints);

                        MDC.put(COUNT, totalOfflineEndpoints);
                        MDC.put(ACTIVE, false);
                        LOG.info(ENDPOINTS, "{} endpoints are currently registered at the ServiceLocator but are not available",
                                 totalOfflineEndpoints);

                        cleanMDC(COUNT, ACTIVE, SLFJ_MARKER);
                    }
                } catch (ServiceLocatorException e) {
                    LOG.warn("Error during SL monitoring", e);
                } catch (InterruptedException e) {
                }
                MDC.remove(EVENT_CATEGORY);
            }
        }, START_DELAY, scanInterval, TimeUnit.SECONDS);
    }

    private synchronized void stopScanning() {
        if (scheduler == null) {
            return;
        }
        scheduler.shutdown();
        scheduler = null;
    }
}
