package com.orange;

import com.orange.config.MainConfig;
import org.springframework.boot.SpringApplication;

import java.util.TimeZone;

public class OrangeApp {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("isThreadContextMapInheritable", "true");
        SpringApplication.run(MainConfig.class, args);
    }

}
