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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(properties = "spring.main.banner-mode=off")
@UseAdviceWith
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class MySpringBootApplicationTest {

    @Autowired
    CamelContext context;

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:test")
    MockEndpoint mockEndpoint;

    @Test
    void routeFromDependency1() throws Exception {
        mockEndpoint.setExpectedMessageCount(1);

        AdviceWith.adviceWith(context, "hello-1", rb -> {
            rb.replaceFromWith("direct:test");
        });

        AdviceWith.adviceWith(context, "route-from-dependency-1", rb -> {
            rb.weaveAddLast().to(mockEndpoint);
        });

        context.start();

        producerTemplate.sendBody("direct:test", "msg");
        mockEndpoint.assertIsSatisfied();
    }

    @Test
    void routeFromDependency2() throws Exception {
        mockEndpoint.setExpectedMessageCount(1);

        AdviceWith.adviceWith(context, "hello-2", rb -> {
            rb.replaceFromWith("direct:test");
        });

        AdviceWith.adviceWith(context, "route-from-dependency-2", rb -> {
            rb.weaveAddLast().to(mockEndpoint);
        });

        context.start();

        producerTemplate.sendBody("direct:test", "msg");
        mockEndpoint.assertIsSatisfied();
    }
}