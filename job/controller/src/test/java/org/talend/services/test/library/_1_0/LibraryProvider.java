package org.talend.services.test.library._1_0;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.12
 * 2017-08-29T13:29:42.492+03:00
 * Generated source version: 3.1.12
 * 
 */
@WebServiceClient(name = "LibraryProvider", 
                  wsdlLocation = "classpath:conf/libraryService/Library.wsdl",
                  targetNamespace = "http://services.talend.org/test/Library/1.0") 
public class LibraryProvider extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://services.talend.org/test/Library/1.0", "LibraryProvider");
    public final static QName LibraryHttpPort = new QName("http://services.talend.org/test/Library/1.0", "LibraryHttpPort");
    static {
        URL url = null;
        try {
            url = new URL("classpath:conf/libraryService/Library.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(LibraryProvider.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/C:/sopera/Tasks/TESB-20129/repo5/tesb-rt-se/policies/compression-policy/src/test/resources/Library.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public LibraryProvider(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public LibraryProvider(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public LibraryProvider() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public LibraryProvider(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public LibraryProvider(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public LibraryProvider(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns Library
     */
    @WebEndpoint(name = "LibraryHttpPort")
    public Library getLibraryHttpPort() {
        return super.getPort(LibraryHttpPort, Library.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Library
     */
    @WebEndpoint(name = "LibraryHttpPort")
    public Library getLibraryHttpPort(WebServiceFeature... features) {
        return super.getPort(LibraryHttpPort, Library.class, features);
    }

}
