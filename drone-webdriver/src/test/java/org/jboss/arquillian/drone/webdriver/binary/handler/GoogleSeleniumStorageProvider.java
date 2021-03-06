package org.jboss.arquillian.drone.webdriver.binary.handler;

import org.jboss.arquillian.drone.webdriver.binary.downloading.source.ExternalBinarySource;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class GoogleSeleniumStorageProvider {

    public static ExternalBinarySource getIeStorageSource(String version){
        return new InternetExplorerBinaryHandler.IeStorageSource(version);
    }

    public static ExternalBinarySource getSeleniumServerStorageSource(String version){
        return new SeleniumServerBinaryHandler.SeleniumServerStorage(version);
    }
}
