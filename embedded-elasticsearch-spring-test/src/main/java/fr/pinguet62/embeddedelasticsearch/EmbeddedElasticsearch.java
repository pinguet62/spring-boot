package fr.pinguet62.embeddedelasticsearch;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface EmbeddedElasticsearch {

    String version();

    int port() default 9300;

    EmbeddedElasticsearchIndex[] indexes() default {};

}
