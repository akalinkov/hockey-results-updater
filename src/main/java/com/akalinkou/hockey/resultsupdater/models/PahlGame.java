package com.akalinkou.hockey.resultsupdater.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Getter
@Setter
@ToString
@CsvRecord(separator = ",", skipFirstLine = true)
public class PahlGame {

  @DataField(pos = 1, required = true)
  private String division;

  @DataField(pos = 2, required = true)
  private String homeTeam;

  @DataField(pos = 3, required = true)
  private String gameNumber;

  @DataField(pos = 4, required = true)
  private String awayTeam;

  @DataField(pos = 5, required = true)
  private String rink;

  @DataField(pos = 6, required = true)
  private String date;

  @DataField(pos = 7, required = true)
  private String time;
}
