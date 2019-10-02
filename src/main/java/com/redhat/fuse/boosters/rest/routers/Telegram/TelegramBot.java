package com.redhat.fuse.boosters.rest.routers.Telegram;

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

        fromF("telegram:bots/%s", token)
            .bean(bot)
        .toF("telegram:bots/%s", token);

    }
}