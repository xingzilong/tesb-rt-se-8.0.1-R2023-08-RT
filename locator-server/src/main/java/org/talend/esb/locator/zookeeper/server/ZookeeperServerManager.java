package org.talend.esb.locator.zookeeper.server;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.zookeeper.metrics.MetricsProvider;
import org.apache.zookeeper.server.admin.AdminServer.AdminServerException;

public class ZookeeperServerManager implements ZookeeperServer {

    private static final Logger LOG = Logger.getLogger(ZookeeperServerManager.class.getName());

    private ZookeeperServer main;
    private Thread zkMainThread;

    private Dictionary<?, ?> properties;
    public void setProperties(Dictionary<?, ?> properties) {
        this.properties = properties;
    }

    public void startup() {

        Dictionary<?, ?> dict = properties;

        LOG.info("Starting up ZooKeeper server");

        if (dict == null) {
            LOG.info("Ignoring configuration update because updated configuration is empty.");
            shutdown();
            return;
        }


        if (main != null) {
            // stop the current instance
            shutdown();
            // then reconfigure and start again.
        }

        if (dict.get("clientPort") == null) {
            LOG.info("Ignoring configuration update because required property 'clientPort' isn't set.");
            return;
        }

        Properties props = new Properties();
        for (Enumeration<?> e = dict.keys(); e.hasMoreElements(); ) {
            Object key = e.nextElement();
            if (key instanceof String) {
                String skey = (String) key;
                if (skey.startsWith("zookeeper.admin.")) {
                    System.setProperty(skey, dict.get(skey).toString());
                } else {
                    props.put(skey, dict.get(skey));
                }
            } else {
                props.put(key, dict.get(key));
            }
        }

        try {
            main = ZookeeperServerImpl.getZookeeperServer(props);

            zkMainThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        main.startup();
                    } catch (IOException|AdminServerException e) {
                        LOG.log(Level.SEVERE, "Problem running ZooKeeper server.", e);
                    }
                }
            });
            //set classloader for thread to be the zookeeper classloader to allow it to load MetricClasses
            ClassLoader bundleClassLoader = MetricsProvider.class.getClassLoader();
            zkMainThread.setContextClassLoader(bundleClassLoader);
            zkMainThread.start();

            LOG.info("Applied configuration update :" + props);
        } catch (Exception th) {
            LOG.log(Level.SEVERE, "Problem applying configuration update: " + props, th);
        }
    }

    public synchronized void shutdown() {
        if (main != null) {
            LOG.info("Shutting down ZooKeeper server");
            main.shutdown();
            try {
                zkMainThread.join();
            } catch (InterruptedException e) {
                // ignore
            }
            if (zkMainThread.isAlive()) {
                LOG.warning("Enforcing termination of zookeeper server thread.");
                zkMainThread.interrupt();
            }
            zkMainThread.setContextClassLoader(null);
            main = null;
            zkMainThread = null;
        }
    }

}
