package com.orange.config;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Mohamed Gaber on 6/06/18.
 */

@Configuration
public class VertxConfig {

    @Bean
    public Vertx vertx(){
        return Vertx.vertx();
    }

    @Bean
    public Router router(Vertx vertx){
        return Router.router(vertx);
    }

}
