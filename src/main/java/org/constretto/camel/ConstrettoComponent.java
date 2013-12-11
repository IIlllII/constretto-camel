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

package org.constretto.camel;

import org.apache.camel.component.properties.PropertiesComponent;

/**
 * An extension of the Apache Camel's @{link PropertiesComponent} providing the full power of Constretto
 * in Camel contexts.
 *
 * @author zapodot at gmail dot com
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
