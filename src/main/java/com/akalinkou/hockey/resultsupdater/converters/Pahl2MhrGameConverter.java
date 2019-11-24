package com.akalinkou.hockey.resultsupdater.converters;

import com.akalinkou.hockey.resultsupdater.models.MhrGame;
import com.akalinkou.hockey.resultsupdater.models.PahlGame;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pahl2MhrGameConverter {

  private static final String SCHEDULED_GAME_SCORE = "999";
  private static final String LEAGUE_GAME_TYPE = "l";
  private static Map<String, String> teamsMap;
  private static Map<String, String> rinksMap;
  static {
    teamsMap = new HashMap<>();
    teamsMap.put("FOXES 279", "24390");
    teamsMap.put("ALLEGHENY 240", "17847");
    teamsMap.put("WHEELING 508", "24414");
    teamsMap.put("WESTMORELAND 388", "17825");
    teamsMap.put("SOUTH HILLS 327", "15417");
    teamsMap.put("BUTLER VALLEY 294", "26151");
    teamsMap.put("MON VALLEY 519", "17821");
    teamsMap.put("INDIANA 342", "26158");
    teamsMap.put("ICE MINERS 526", "15400");
  }

  static {
    rinksMap = new HashMap<>();
    rinksMap.put("RMUS", "1451");
    rinksMap.put("KNEV", "1270");
    rinksMap.put("DELM", "1112");
    rinksMap.put("YMCA", "1106");
    rinksMap.put("STBK", "2247");
    rinksMap.put("WSBC", "1314");
    rinksMap.put("WPRK", "1068");
    rinksMap.put("ROST", "1571");
    rinksMap.put("POND", "1277");
    rinksMap.put("MINE", "2581");
  }

  public static MhrGame convert(PahlGame game) {
    return MhrGame.builder()
        .age(getAge(game))
        .division(getDivision(game))
        .homeTeam(getTeam(game.getHomeTeam()))
        .awayTeam(getTeam(game.getAwayTeam()))
        .homeTeamScore(SCHEDULED_GAME_SCORE)
        .awayTeamScore(SCHEDULED_GAME_SCORE)
        .rink(getLocation(game.getRink()))
        .date(getDate(game.getDate()))
        .hours(getHours(game.getTime()))
        .minutes(getMinutes(game.getTime()))
        .gameType(LEAGUE_GAME_TYPE)
        .description(game.getGameNumber())
        .build();
  }

  private static String getMinutes(String time) {
    String minutes = String.valueOf(parseTime(time).getMinute());
    if (minutes.length() == 1) return "0" + minutes;
    return minutes;
  }

  private static String getHours(String time) {
    return String.valueOf(parseTime(time).getHour());
  }

  private static LocalTime parseTime(String time) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
    return LocalTime.parse(time, formatter);
  }

  private static LocalDate getDate(String date) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");
    return LocalDate.parse(date, formatter);
  }

  private static String getLocation(String rink) {
    final String mhrRinkId = rinksMap.get(rink);
    if (mhrRinkId == null) {
      log.error("Unknown mapping for rink: {}.", rink);
      throw new IllegalArgumentException("Unknown mapping for rink: " + rink);
    }
    return mhrRinkId;
  }

  private static String getTeam(String team) {
    final String mhrTeamId = teamsMap.get(team);
    if (mhrTeamId == null) {
      log.error("Unknown mapping for team: {}.", team);
      throw new IllegalArgumentException("Unknown mapping for team: " + team);
    }
    return mhrTeamId;
  }

  private static String getAge(PahlGame game) {
    final String[] params = game.getDivision().split(" ");
    switch (params[0]) {
      case "10U":
        return "1"; // Squirt
      case "12U":
        return "2"; // PeeWee
      case "14U":
        return "3"; // Bantam
      default:
        throw new IllegalArgumentException("Unknown age group: " + params[0]);
    }
  }

  private static String getDivision(PahlGame game) {
    switch (game.getDivision()) {
      case "12U A MINOR GOLD":
        return "2";
      default:
        throw new IllegalArgumentException("Unknown division: " + game.getDivision());
    }
  }
}
