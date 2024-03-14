package my.company;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class DependencyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("direct:route-from-dependency-2").routeId("route-from-dependency-2")
            .log("Hello there 👋🏻");
    }
}
