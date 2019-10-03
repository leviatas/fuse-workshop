package com.redhat.fuse.boosters.rest.routers.Telegram;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for routing the messages from and to the Telegram chat.
 */
@Component
public class TelegramBot extends RouteBuilder {

    @Autowired
    private Bot bot;

    @Value("${telegram.bot.token}")
	private String token;

    @Override
    public void configure() throws Exception {
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("http.proxyHost", "10.61.10.225");
        map.put("http.proxyPort", "8080"); 
        
        getContext().setGlobalOptions(map);*/
        
        fromF("telegram:bots/%s", token)
            .bean(bot)
        .toF("telegram:bots/%s", token);
        
        /* fromF("telegram:bots/%s", token)
        .doTry()
            .bean(bot)
            .toF("telegram:bots/%s", token)
        .doCatch(Exception.class)
            .log("Error en Telegram bot")
        .end(); */
        
        /* from("timer:generate?repeatCount=1")
        .doTry()    
            .log("Trying to start Telegram bot")
            .toF("telegram:bots/%s", token)
                .bean(bot)
            .toF("telegram:bots/%s", token)
        .doCatch(java.net.ConnectException.class)
            .log("Error en Telegram bot")
        .end(); */
                

    }
}