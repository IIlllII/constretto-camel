# Constretto Camel #

[Apache Camel](http://camel.apache.org) component supported for [Constretto](http://constretto.github.io). Builds upon the PropertiesComponent distributed with Apache Camel. Enables you to use the power of Constretto to setup configuration from various sources such as Java properties and INI files with support for environment tagging. 

[![Build Status](https://travis-ci.org/constretto/constretto-camel.png)](https://travis-ci.org/constretto/constretto-camel)

## Adding to project ##
The component is deployed in the Sonatype repo and should thus be widely available.

### Maven ###
```xml
<dependency>
  <groupId>org.constretto</groupId>
  <artifactId>constretto-camel</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Adding to Camel Context ##
### Spring XML ###
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:camel="http://camel.apache.org/schema/spring" xmlns:to="http://camel.apache.org/schema/spring"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd">

    <bean id="properties" class="org.constretto.camel.ConstrettoComponent">
        <constructor-arg>
            <array>
                <value>classpath:test.properties</value>
            </array>
        </constructor-arg>
    </bean>

    <camel:camelContext id="testContext" xmlns="http://camel.apache.org/schema/spring">
        <route>
            <from uri="direct:input"/>
            <to uri="log:{{outlogger}}" id="output"/>
        </route>

    </camel:camelContext>

</beans>
```
Or you can combine it with the Constretto-Spring XML namespace support
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:camel="http://camel.apache.org/schema/spring"
       xmlns:constretto="http://constretto.org/schema/constretto"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://camel.apache.org/schema/spring http://camel.apache.org/schema/spring/camel-spring.xsd
       http://constretto.org/schema/constretto http://constretto.org/schema/constretto/constretto-1.2.xsd">

    <constretto:configuration>
        <constretto:stores>
            <constretto:properties-store>
                <constretto:resource location="classpath:test.properties" />
            </constretto:properties-store>
        </constretto:stores>
    </constretto:configuration>

    <bean id="properties" class="org.constretto.camel.ConstrettoComponent">
        <constructor-arg>
            <array>
                <value>ref:constretto:constrettoConfiguration</value> 
                <!-- ref: links will be looked up from the Spring context -->
            </array>
        </constructor-arg>
    </bean>

    <camel:camelContext id="testContext" xmlns="http://camel.apache.org/schema/spring">
        <route>
            <from uri="direct:input"/>
            <to uri="log:{{outlogger}}" id="output"/>
        </route>

    </camel:camelContext>

</beans>
```
### Spring Java Context ###
```java
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

```