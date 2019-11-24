package com.akalinkou.hockey.resultsupdater.processors;

import com.akalinkou.hockey.resultsupdater.converters.Pahl2MhrGameConverter;
import com.akalinkou.hockey.resultsupdater.models.MhrGame;
import com.akalinkou.hockey.resultsupdater.models.PahlGame;
import org.apache.camel.Exchange;

public abstract class PahlGameProcessor {

  public static void toWwwForm(Exchange exchange) {
    PahlGame pahlGame = exchange.getIn().getBody(PahlGame.class);
    MhrGame mhrGame = Pahl2MhrGameConverter.convert(pahlGame);
    exchange.getOut().setBody(mhrGame.toFormData());
  }
}
