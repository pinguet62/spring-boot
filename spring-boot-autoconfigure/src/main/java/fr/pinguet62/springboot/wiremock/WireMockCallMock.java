package fr.pinguet62.springboot.wiremock;

import org.springframework.http.HttpMethod;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
@Repeatable(WireMockCallMocks.class)
public @interface WireMockCallMock {

    String NULL = "##NULL##";

    /**
     * @see WireMockApi#api()
     */
    String api();

    HttpMethod method();

    /**
     * @see com.github.tomakehurst.wiremock.client.WireMock#urlMatching(String)
     */
    String urlMatching();

    int status();

    String body() default NULL;

    String bodyResource() default NULL;

}
