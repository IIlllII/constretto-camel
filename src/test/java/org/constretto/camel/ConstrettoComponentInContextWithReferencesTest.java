package org.constretto.camel;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author zapodot at gmail dot com
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:referencedConstrettoContext.xml")
@MockEndpoints("log:*")
public class ConstrettoComponentInContextWithReferencesTest {

    @Produce(uri = "direct:input")
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:log:loggerName")
    private MockEndpoint endpoint;

    @Test
    public void testInRoute() throws Exception {
        producerTemplate.sendBody("blah");
        endpoint.expectedMessageCount(1);
        endpoint.assertIsSatisfied();

    }
}
