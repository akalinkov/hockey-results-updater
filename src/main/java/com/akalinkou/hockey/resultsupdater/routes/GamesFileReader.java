package com.akalinkou.hockey.resultsupdater.routes;

import com.akalinkou.hockey.resultsupdater.models.Game;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GamesFileReader extends RouteBuilder {

  @Override
  public void configure() {
    from("file://target/input?move=.done")
        .unmarshal(new BindyCsvDataFormat(Game.class))
        .split(body())
        .log("Line: ${body}");
  }
}
