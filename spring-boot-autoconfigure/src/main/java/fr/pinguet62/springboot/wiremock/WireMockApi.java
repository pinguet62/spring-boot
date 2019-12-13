package fr.pinguet62.springboot.wiremock;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(WireMockApis.class)
public @interface WireMockApi {

    /**
     * Unique (for test) identifier of API.
     */
    String api();

    /**
     * Property dynamically created containing port value.
     */
    String propertyKey();

}
