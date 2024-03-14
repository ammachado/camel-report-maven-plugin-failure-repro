package my.company;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:hello-1?period={{timer.period}}").routeId("hello-1")
            .to("direct:route-from-dependency-1");

        from("timer:hello-2?period={{timer.period}}").routeId("hello-2")
            .to("direct:route-from-dependency-2");
    }
}
