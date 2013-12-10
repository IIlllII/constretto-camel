package org.constretto.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.Registry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author sondre
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstrettoPropertiesResolverTest {

    @Mock
    private CamelContext camelContext;

    @Mock
    private Registry registry;

    private ConstrettoPropertiesResolver resolver = new ConstrettoPropertiesResolver();

    @Before
    public void setUp() throws Exception {
        when(camelContext.getRegistry()).thenReturn(registry);

    }

    @Test(expected = FileNotFoundException.class)
    public void testResolvePropertiesFails() throws Exception {
        resolver.resolveProperties(null, false, "classpath:doesNotExist.properties");
    }

    @Test
    public void testResolvePropertiesIgnore() throws Exception {
        final Properties properties = resolver.resolveProperties(null, true, "classpath:doesNotExist.properties");
        assertNotNull(properties);
        assertTrue(properties.containsKey("java.vm.name"));
    }

    @Test
    public void testResolvePropertiesExists() throws Exception {
        final Properties properties = resolver.resolveProperties(null, true, "classpath:test.properties");
        assertEquals("value", properties.getProperty("key"));

    }

    @Test
    public void testResolveIniFileExists() throws Exception {
        final Properties properties = resolver.resolveProperties(null, true, "classpath:test.ini");
        assertEquals("value", properties.getProperty("key"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveUnsupportedExtension() throws Exception {
        final Properties properties = resolver.resolveProperties(null, true, "classpath:unsupported.txt");
        assertEquals("value", properties.getProperty("key"));

    }



    @Test
    public void testResolveReferenceIgnore() throws Exception {
       final Properties properties = resolver.resolveProperties(camelContext, true, "ref:configuration");
        assertNotNull(properties);

    }
}
