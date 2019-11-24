package com.akalinkou.hockey.resultsupdater.routes;

import com.akalinkou.hockey.resultsupdater.models.PahlGame;
import com.akalinkou.hockey.resultsupdater.processors.PahlGameProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GamesFileReader extends RouteBuilder {

  @Override
  public void configure() {
    from("file://input?move=.done")
        .routeId("CSV File Reader")
        .unmarshal(new BindyCsvDataFormat(PahlGame.class))
        .split(body())
        .process(PahlGameProcessor::toWwwForm)
        .log("${body}")
        .end();
  }
}
