package com.akalinkou.hockey.resultsupdater;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormProperty {

  String name() default "";

  boolean required() default false;

  String pattern() default "";
}
