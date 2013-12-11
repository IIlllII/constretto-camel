/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.constretto.camel.store;

import org.constretto.ConfigurationStore;
import org.constretto.ConstrettoConfiguration;
import org.constretto.Property;
import org.constretto.model.TaggedPropertySet;

import java.util.*;

/**
 * @author zapodot at gmail dot com
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
