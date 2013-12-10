package org.constretto.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesResolver;
import org.apache.camel.util.ObjectHelper;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.Property;
import org.constretto.camel.store.EmbeddedConfigurationStore;
import org.constretto.model.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author sondre
 */
public class ConstrettoPropertiesResolver implements PropertiesResolver {


    @Override
    public Properties resolveProperties(final CamelContext context, final boolean ignoreMissingLocation, final String... uri) throws Exception {
        final ConstrettoBuilder constrettoBuilder = new ConstrettoBuilder();
        for (String currentUri : uri) {
            if (currentUri.startsWith("ref:")) {
                loadFromRegistry(constrettoBuilder, context, currentUri, ignoreMissingLocation);
            } else {
                loadFromResource(constrettoBuilder, currentUri, ignoreMissingLocation);
            }
        }
        final ConstrettoConfiguration configuration = constrettoBuilder.getConfiguration();
        return mapToProperties(configuration);

    }

    private Properties mapToProperties(final ConstrettoConfiguration configuration) {
        final Iterator<Property> propertyIterator = configuration.iterator();
        final Properties properties = new Properties();
        while (propertyIterator.hasNext()) {
            final Property property = propertyIterator.next();
            properties.setProperty(property.getKey(), property.getValue());
        }
        return properties;
    }

    private void loadFromResource(final ConstrettoBuilder constrettoBuilder,
                                  final String uri,
                                  final boolean ignoreMissingLocation) throws IOException {
        final Resource resource = Resource.create(uri);
        final boolean resourceExist = resource.exists();
        if (!ignoreMissingLocation && !resourceExist) {
            throw new FileNotFoundException(String.format("The resource \"%1$s\" does not exist", uri));
        }
        if (resourceExist) {
            if (uri.endsWith(".properties")) {
                constrettoBuilder.createPropertiesStore().addResource(resource).done();
            } else if (uri.endsWith(".ini")) {
                constrettoBuilder.createIniFileConfigurationStore().addResource(resource).done();
            } else {
                throw new IllegalArgumentException(String.format("I do not know what to do with the url \"%1$s\". " +
                        "Only files with .properties or .ini extensions is currently supported", uri));
            }
        }
    }

    private void loadFromRegistry(final ConstrettoBuilder constrettoBuilder,
                                  final CamelContext context,
                                  final String currentUri,
                                  final boolean ignoreMissingLocation) throws IOException {
        final String componentName = ObjectHelper.after(currentUri, "ref:");
        final ConstrettoConfiguration configuration = context
                .getRegistry()
                .lookupByNameAndType(componentName, ConstrettoConfiguration.class);
        if (configuration == null && !ignoreMissingLocation) {
            throw new FileNotFoundException(String.format("No ConstrettoConfiguration called \"%1$s\" was found", componentName));

        }
        if (configuration != null) {
            constrettoBuilder.addConfigurationStore(new EmbeddedConfigurationStore(configuration));
        }

    }
}
