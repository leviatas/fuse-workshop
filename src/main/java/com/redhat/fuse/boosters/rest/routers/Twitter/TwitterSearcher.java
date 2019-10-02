package com.redhat.fuse.boosters.rest.routers.Twitter;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.search.TwitterSearchComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for routing the messages from and to the Telegram chat.
 */
@Component
public class TwitterSearcher extends RouteBuilder {

    /* @Autowired
    private Bot bot; */

    @Value("${twiter.accessToken}")
    private String accessToken;
    
    @Value("${twitter.accessTokenSecret}")
    private String accessTokenSecret;
    
    @Value("${twitter.consumerKey}")
    private String consumerKey;
    
    @Value("${twitter.consumerSecret}")
	private String consumerSecret;

    @Value("${twitter.search.delay}")
    private String searchDelay;

    @Value("${twitter.search.term}")
    private String searchTerm;
    
    @Override
    public void configure() throws Exception {
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(accessToken);
        tc.setAccessTokenSecret(accessTokenSecret);
        tc.setConsumerKey(consumerKey);
        tc.setConsumerSecret(consumerSecret);

        fromF("twitter-search://%s?delay=%s", searchTerm, searchDelay)
            .log("${body}");
            // and push tweets to all web socket subscribers on camel-tweet
            //.to("websocket:camel-tweet?sendToAll=true");

    }
}