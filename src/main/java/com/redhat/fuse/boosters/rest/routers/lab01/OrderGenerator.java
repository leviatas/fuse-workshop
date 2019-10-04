package com.redhat.fuse.boosters.rest.routers.lab01;

import com.redhat.fuse.boosters.rest.service.OrderService;

/* import org.apache.camel.CamelContext; */
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
/* import org.apache.camel.component.slack.SlackComponent;
import org.apache.camel.impl.DefaultCamelContext; */
import org.springframework.stereotype.Component;


@Component
public class OrderGenerator extends RouteBuilder {    
    @Value("${slack.webhookUrl.url}")
	private String webhookUrl;

    @Override
    public void configure() throws Exception {

        from("timer:generate?repeatCount=5&period=1000")
            .routeId("order_generator")
            .noAutoStartup()
            .log("Generating Order...")
            .bean(OrderService.class, "generateOrder")
            .log("Order ${body.item} generated")
        .to("direct:book-to-file");

        
        /* from("direct:book-to-file")
            .choice()
                .when(simple("${body.item} == 'Camel'"))
                    .log("Processing a camel book into file")
                    .marshal().json()
                    .to("file:./tmp/fuse-workshop/camel?fileName=camel-${date:now:yyyy-MM-dd-HHmmssSSS}.txt")
                .otherwise()
                    .log("Processing an activemq book into file")
                    .process(new OrderProcessor()) // ADD THIS LINE
                    .marshal().jacksonxml()
                    .to("file:./tmp/fuse-workshop/activemq?fileName=activemq-${date:now:yyyy-MM-dd-HHmmssSSS}.txt");
         */
        from("direct:book-to-file")
        .doTry()
            .log("Sending Order to slack")
            //.to("slack:#general");
            .toF("slack:#general:generate?webhookUrl=%s", webhookUrl)
        .doCatch(Exception.class)
            .log("Error en Slack repeater")
        .end();           


        from("file:./tmp/fuse-workshop/activemq?delete=true")
            .log("uploading activemq orders to ftp")
            .to("file:./tmp/procesados");
            //.to("ftp://<ftp-user>@<ftp-host>?password=<ftp-password>");
    }

}
