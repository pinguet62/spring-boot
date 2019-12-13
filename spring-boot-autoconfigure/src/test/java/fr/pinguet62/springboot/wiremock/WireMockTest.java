package fr.pinguet62.springboot.wiremock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

@RunWith(SpringRunner.class)
@WireMockApi(api = "facebook", propertyKey = "api.facebook.port")
@WireMockApi(api = "google", propertyKey = "api.google.port")
@SpringBootTest(properties = {
        "controller.facebook.url=http://localhost:${api.facebook.port}",
        "controller.google.url=http://localhost:${api.google.port}",
}, classes = WireMockTest.class)
// auto-configuration
@TestExecutionListeners(value = WireMockTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
public class WireMockTest {

    @Value("${controller.facebook.url}")
    String facebookUrl;

    @Value("${controller.google.url}")
    String googleUrl;

    final RestTemplate restTemplate = new RestTemplate();

    @Test
    @WireMockCallMock(api = "facebook", method = GET, urlMatching = "/first", status = 200, body = "I'm first URL")
    @WireMockCallMock(api = "facebook", method = GET, urlMatching = "/second", status = 200, bodyResource = "classpath:fr/pinguet62/springboot/wiremock/test.txt")
    @WireMockCallMock(api = "google", method = GET, urlMatching = "/other", status = 200, body = "I'm the other")
    public void test() {
        assertThat(restTemplate.getForEntity(facebookUrl + "/first", String.class).getBody(), is("I'm first URL"));
        assertThat(restTemplate.getForEntity(facebookUrl + "/second", String.class).getBody(), is("I'm second URL"));
        assertThat(restTemplate.getForEntity(googleUrl + "/other", String.class).getBody(), is("I'm the other"));
    }

}
