package com.orange.config;

import com.orange.OrangeApp;
import com.orange.verticles.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Karim on 10/11/2017.
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class,
        RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@Import({RedisConfig.class})
@ComponentScan(basePackageClasses = OrangeApp.class)
public class MainConfig {

    @Autowired
    private Vertx vertx;

    @Autowired
    private Router router;

    @Autowired
    private MainVerticle mainVerticle;

    @PostConstruct
    public void createServerAndDeployVerticles(){
        vertx.deployVerticle(mainVerticle, new DeploymentOptions().setWorker(true).setWorkerPoolSize(10));
    }

    @PreDestroy
    public void closeVertix(){
        vertx.close();
    }

}
