package com.akalinkou.hockey.resultsupdater.models;

import com.akalinkou.hockey.resultsupdater.FormProperty;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder()
public class MhrGame {

  @FormProperty(name = "a")
  private String age;
  @FormProperty(name = "ss")
  private String division;
  @FormProperty(name = "team1")
  private String homeTeam;
  @FormProperty(name = "team2")
  private String awayTeam;
  @FormProperty(name = "score1")
  private String homeTeamScore;
  @FormProperty(name = "score2")
  private String awayTeamScore;
  @FormProperty(name = "loc")
  private String rink;
  @FormProperty(name = "date", pattern = "yyyyMMdd")
  private LocalDate date;
  @FormProperty(name = "time1")
  private String hours;
  @FormProperty(name = "time2")
  private String minutes;
  @FormProperty(name = "game_type")
  private String gameType;
  @FormProperty(name = "desc")
  private String description;

  public String toFormData() {
    String tag = "toFormData";
    log.debug("{}: Converting MHR game to Form Data.", tag);
    StringBuilder builder = new StringBuilder();
    for (Field field : this.getClass().getDeclaredFields()) {
      log.debug("{}: Processing field '{}'.", tag, field.getName());
      FormProperty formProperty = getFormProperty(field);
      if (formProperty == null) {
        log.debug("{}: Skipping field without @FormProperty annotation.", tag);
      } else {
        String formPair = getFormPair(field, formProperty);
        if (builder.length() > 0) {
          builder.append("&");
        }
        builder.append(formPair);
      }
    }
    return builder.toString();
  }

  private FormProperty getFormProperty(Field field) {
    FormProperty[] properties = field.getAnnotationsByType(FormProperty.class);
    if (properties.length == 0) {
      return null;
    } else if (properties.length > 1) {
      throw new IllegalArgumentException(
          "Only 1 '@FormProperty' per field allowed. Field: " + field.getName());
    }
    return properties[0];
  }

  private String getFormPair(Field field, FormProperty property) {
    String tag = "getFormPair";
    try {
      return property.name() + "=" + getFieldValue(field, property);
    } catch (IllegalAccessException e) {
      log.error("IllegalAccessException", e);
    } catch (NullPointerException e) {
      log.debug("{}: Skipping field '{}' as null", tag, field);
    }
    return "";
  }

  private String getFieldValue(Field field, FormProperty property)
      throws IllegalAccessException {
    String tag = "getFieldValue";
    Object value = field.get(this);
    if (value instanceof LocalDate) {
      log.debug("{}: Field is a date. Formatting date.", tag);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(property.pattern());
      String date = ((LocalDate) value).format(formatter);
      log.debug("{}: Value = {}", tag, date);
      return date;
    } else if (value instanceof String) {
      log.debug("{}: Value = {}", tag, value);
      return value.toString();
    }
    throw new IllegalArgumentException(
        "@FormProperty can be applied only to String or LocalDate fields. Field: " + field
            .getName());
  }
}
