package com.fkt.networkmaster;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class NetworkMasterApplication {
    private static final Logger log = LoggerFactory.getLogger(NetworkMasterApplication.class);
    public static void main(String[] args) {
        SpringApplication.run( NetworkMasterApplication.class, args);
        log.info("Web Document Reference http://127.0.0.1:9991/swagger-ui/index.html");
        log.info("Server is Running on http://127.0.0.1:9991");
    }

}
