package com.akalinkou.hockey.resultsupdater.routes;

import static org.apache.camel.component.http4.HttpMethods.POST;

import com.akalinkou.hockey.resultsupdater.models.PahlGame;
import com.akalinkou.hockey.resultsupdater.processors.PahlGameProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.http.common.cookie.InstanceCookieHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GamesFileReader extends RouteBuilder {

  private final InstanceCookieHandler instanceCookieHandler;

  @Autowired
  public GamesFileReader(InstanceCookieHandler instanceCookieHandler) {
    this.instanceCookieHandler = instanceCookieHandler;
  }

  @Override
  public void configure() {
    from("file://input?move=.done")
        .routeId("CSV File Reader")
        .unmarshal(new BindyCsvDataFormat(PahlGame.class))
        .setProperty("pahl_game", body())
        .setBody(simple("user={{mhr.username}}&pass=${{mhr.password}}"))
        .log("Body before login - ${body}")
        .to("https4://myhockeyrankings.com/login.php?cookieHandler=#instanceCookieHandler")
        .setBody(exchangeProperty("pahl_game"))
        .log("Body before split - ${body}")
        .split(body())
          .process(PahlGameProcessor::toWwwForm)
          .log("${body}")
          .setHeader(Exchange.HTTP_METHOD, constant(POST))
          .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
          .to("log:DEBUG?showBody=true&showHeaders=true")
          .to("https4://myhockeyrankings.com/volunteer/insert_sched.php?cookieHandler=#instanceCookieHandler")
          .to("log:DEBUG?showBody=true&showHeaders=true")
        .end();
  }
}
