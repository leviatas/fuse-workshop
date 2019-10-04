package com.redhat.fuse.boosters.rest.routers.lab02;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ServiceStatus;

public class ToggleRouteProcessor implements Processor {

    private final String name;

    public ToggleRouteProcessor(String name) {
        this.name = name;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getContext().getInflightRepository().remove(exchange);
        String status = "";
        if(exchange.getContext().getRouteStatus(name) == ServiceStatus.Started){
            exchange.getContext().stopRoute(name);
            status = String.format("Stopping %s", name);
        }else{
            exchange.getContext().startRoute(name);
            status = String.format("Starting %s", name);
        }
        exchange.getOut().setBody(status);
    }

}