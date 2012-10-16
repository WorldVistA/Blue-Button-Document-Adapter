package org.osehra.integration.test.spring;

import org.osehra.integration.util.NullChecker;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.annotation.ProfileValueSource;

public class CustomProfileValueSource implements ProfileValueSource {
    String testProp = "test-groups";
    Properties sysProps = System.getProperties();

    private static final Log LOG = LogFactory
            .getLog(CustomProfileValueSource.class);

    @Override
    public String get(String arg0) {
        CustomProfileValueSource.LOG.debug("Value of " + this.testProp + " is "
                + this.sysProps.getProperty(this.testProp));
        final String value = this.sysProps.getProperty(this.testProp);
        if (NullChecker.isEmpty(value)) {
            return "all";
        } else {
            return value;
        }
    }
}
