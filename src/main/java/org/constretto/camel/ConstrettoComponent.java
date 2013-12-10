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

    private void setConstrettoResolver() {
        setPropertiesResolver(new ConstrettoPropertiesResolver());
    }

}
