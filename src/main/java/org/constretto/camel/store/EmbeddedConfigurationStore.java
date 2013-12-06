package org.constretto.camel.store;

import org.constretto.ConfigurationStore;
import org.constretto.ConstrettoConfiguration;
import org.constretto.Property;
import org.constretto.model.TaggedPropertySet;

import java.util.*;

/**
 * @author sondre
 */
public class EmbeddedConfigurationStore implements ConfigurationStore {

    private final ConstrettoConfiguration configuration;

    public EmbeddedConfigurationStore(final ConstrettoConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Collection<TaggedPropertySet> parseConfiguration() {
        final Iterator<Property> iterator = configuration.iterator();

        return Arrays.asList(new TaggedPropertySet(createMapFromIterator(iterator), getClass()));
    }

    private Map<String, String> createMapFromIterator(final Iterator<Property> propertyIterator) {
        final Map<String, String> answer = new HashMap<String, String>();
        while(propertyIterator.hasNext()) {
            final Property property = propertyIterator.next();
            answer.put(property.getKey(), property.getValue());
        }
        return answer;
    }
}
