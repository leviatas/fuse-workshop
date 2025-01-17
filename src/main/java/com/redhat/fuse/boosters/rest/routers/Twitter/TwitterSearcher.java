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
        if (accessToken == "CHANGE_WITH_YOURS")
            accessToken = System.getenv("twiter.accessToken");			
		if (accessTokenSecret == "CHANGE_WITH_YOURS")
            accessTokenSecret = System.getenv("twitter.accessTokenSecret");
		if (consumerKey == "CHANGE_WITH_YOURS")
            consumerKey = System.getenv("twitter.consumerKey");
        if (consumerSecret == "CHANGE_WITH_YOURS")
            consumerSecret = System.getenv("twitter.consumerSecret");			
		if (searchDelay == "CHANGE_WITH_YOURS")
            searchDelay = System.getenv("twitter.search.delay");
		if (searchTerm == "CHANGE_WITH_YOURS")
            searchTerm = System.getenv("twitter.search.term");
        
        TwitterSearchComponent tc = getContext().getComponent("twitter-search", TwitterSearchComponent.class);
        tc.setAccessToken(accessToken);
        tc.setAccessTokenSecret(accessTokenSecret);
        tc.setConsumerKey(consumerKey);
        tc.setConsumerSecret(consumerSecret);

        // Si quiero que me mande empty cuando no haya nada.
        /* fromF("twitter-search://%s?delay=%s&sendEmptyMessageWhenIdle=true", searchTerm, searchDelay) */
        from("direct:twitter-search")
        .routeId("twitter_searcher")
        //.log("Los parametros son *${body}*")
        .recipientList(simple("twitter-search://${body.term}?delay=${body.delay}&sendEmptyMessageWhenIdle=true"))  
        .log("El mensaje es: ${body}");
        /* 
        .choice()
        .when(simple("${body} == ''"))
            .log("No hay Tweets nuevos")            
        .otherwise() 
            .log("El mensaje es: *${body}*"); */

        // fromF("twitter-search://%s?delay=%s", searchTerm, searchDelay)//.marshal().json()
        // .routeId("twitter_searcher")
        // .noAutoStartup()
        // .choice()
        // .when(simple("${body} == ''"))
        //     .log("No hay Tweets nuevos")            
        // .otherwise() 
        //     .log("El mensaje es: *${body}*");

        /* fromF("twitter-search://%s?delay=%s&sendEmptyMessageWhenIdle=true", searchTerm, searchDelay)
            .log("Twitter search Hospital ${body}")
        .end(); */

        

        /* from("timer:generate?repeatCount=1")
        .doTry()    
            .log("Checking Twitter messages")
            .toF("twitter-search://%s?delay=%s", searchTerm, searchDelay)
            .log("${body}")
        .doCatch(TwitterException.class)
            .log("Error en Twitter Searcher")
        .end(); */
                // and push tweets to all web socket subscribers on camel-tweet
                //.to("websocket:camel-tweet?sendToAll=true");

    }
}