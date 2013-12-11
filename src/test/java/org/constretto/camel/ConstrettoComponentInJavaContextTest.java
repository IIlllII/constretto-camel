package org.constretto.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author sondre
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConstrettoComponentInJavaContextTest.Context.class,
        loader = CamelSpringDelegatingTestContextLoader.class)
@MockEndpoints
public class ConstrettoComponentInJavaContextTest {

    public static final String TEST_BODY = "test";

    @Configuration
    public static class Context extends SingleRouteCamelConfiguration {

        @Bean
        public static ConstrettoConfiguration constrettoConfiguration() {
            return new ConstrettoBuilder(false)
                    .createPropertiesStore()
                    .addResource(Resource.create("classpath:test.properties"))
                    .done()
                    .getConfiguration();
        }

        @Bean
        public ConstrettoComponent properties() {
            return new ConstrettoComponent("ref:constrettoConfiguration");
        }

        @Override
        protected void setupCamelContext(final CamelContext camelContext) throws Exception {
            camelContext.addComponent("properties", properties());
        }

        @Override
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:input").routeId("javaContextInputRoute").to("log:{{outlogger}}");
                }
            };
        }
    }

    @Produce(uri = "direct:input")
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:log:loggerName")
    private MockEndpoint endpoint;

    @Test
    public void testRoute() throws Exception {
        endpoint.expectedBodiesReceived(TEST_BODY);
        producerTemplate.sendBody(TEST_BODY);
        endpoint.assertIsSatisfied();


    }
}
