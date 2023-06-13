package org.otasyn.template.jersey2.guice;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import jakarta.inject.Qualifier;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({ FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface TypesafeConfig {}
