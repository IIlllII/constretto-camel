package org.constretto.camel;

import org.apache.camel.component.properties.PropertiesComponent;

/**
 * @author sondre
 */
public class ConstrettoComponent extends PropertiesComponent {

    public ConstrettoComponent(final String... locations) {
        super(locations);
        setConstrettoResolver();
    }

    public ConstrettoComponent(final String location) {
        super(location);
        setConstrettoResolver();
    }

    private void setConstrettoResolver() {
        setPropertiesResolver(new ConstrettoPropertiesResolver());
    }

}
