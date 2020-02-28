package com.akalinkou.hockey.resultsupdater;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.http.common.cookie.InstanceCookieHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    log.debug("Starting application");
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public InstanceCookieHandler instanceCookieHandler() {
    return new InstanceCookieHandler();
  }
}

