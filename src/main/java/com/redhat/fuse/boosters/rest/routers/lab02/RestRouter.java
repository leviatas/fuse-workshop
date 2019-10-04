package com.redhat.fuse.boosters.rest.routers.lab02;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import com.redhat.fuse.boosters.rest.model.Order;
import com.redhat.fuse.boosters.rest.model.TwitterSearchRequest;;

@Component
public class RestRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // insert your rest code here
        rest("/telegram")
            .put("/toggle").description("Toggle Start/Stop Telegram Bot")
                .route().routeId("telegram-toggle")
                .process(new ToggleRouteProcessor("telegram_bot"))
                .log("${body}")                
                .endRest();
        
        rest("/twitter")
            .put("/toggle").type(TwitterSearchRequest.class).description("Toggle Start/Stop Twitter searcher")
                .route().routeId("twitter-toggle")
                .log("${body}")
                .process(new ToggleRouteProcessor("twitter_searcher"))
                .log("${body}")                
                .endRest();
        
        rest("/ordergenerator")
            .put("/toggle").description("Toggle Start/Stop ordergenerator searcher")
                .route().routeId("ordergenerator-toggle")
                .process(new ToggleRouteProcessor("order_generator"))
                .log("${body}")                
                .endRest();
                
        rest("/orders")
            .get("/").description("Get all orders")
                .route().routeId("all-orders")
                .log("geting all orders entries from database")
                .to(this.selectAll)
                .endRest()
            
            .get("/{id}").description("Get orders by id")
                .route().routeId("find-by-id")
                .log("geting order with id ${header.id} entry from database")
                .to(this.selectById)
                .endRest()

            .post("/").type(Order.class).description("Create a new order")
                .route().routeId("create order")
                .log("Order received")
                .to(this.insertOrder)
                .endRest()

            .post("/async").type(Order.class).description("Create a new order")
                .route().routeId("create order async")
                .log("Order received")
                .wireTap("direct:create-order")
                .setBody().simple("We received your request, as soon we process your request we will notify you by email.")
                .endRest();
        
            from("direct:create-order")
                .log("sending ${body.item} to JMS queue")
                .to("activemq:queue:orders");

            // Consume from the message broker queue
            from("activemq:queue:orders")
            .routeId("queue_consumer")
            .noAutoStartup()
                .log("received ${body.item} from JMS queue")
                .to(this.insertOrder)
                .to("mock:notify-by-email");

    }

    // Query support
    private String ds = "?dataSource=dataSource";
    private String selectAll = "sql:select * from orders" + ds;
    private String selectById = "sql:select * from orders where id = :#${header.id}"  + ds;
    private String insertOrder = "sql:insert into orders (item, amount, description, processed) values " +
    	                		"(:#${body.item}, :#${body.amount}, :#${body.description}, false)"+ ds;

}