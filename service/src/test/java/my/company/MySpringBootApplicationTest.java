package my.company;

import org.apache.camel.component.mock.MockEndpoint;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest
@UseAdviceWith
class MySpringBootApplicationTest {

    @Autowired
    CamelContext context;

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:test")
    MockEndpoint mockEndpoint;

    @Test
    void shouldAutowireProducerTemplate() throws Exception {
        mockEndpoint.setExpectedMessageCount(1);

        AdviceWith.adviceWith(context, "hello", rb -> {
            rb.replaceFromWith("direct:test");
        });

        AdviceWith.adviceWith(context, "route-from-dependency", rb -> {
            rb.weaveAddLast().to(mockEndpoint);
        });

        context.start();

        producerTemplate.sendBody("direct:test", "msg");
        mockEndpoint.assertIsSatisfied();
    }
}