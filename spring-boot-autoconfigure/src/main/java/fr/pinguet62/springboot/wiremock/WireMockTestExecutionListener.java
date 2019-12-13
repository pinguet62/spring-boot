package fr.pinguet62.springboot.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.trace;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static fr.pinguet62.springboot.wiremock.WireMockCallMock.NULL;
import static org.springframework.core.annotation.AnnotationUtils.getRepeatableAnnotations;

@Configuration
public class WireMockTestExecutionListener extends AbstractTestExecutionListener {

    private final Map<String, WireMockServer> startedApis = new HashMap<>();

    @Override
    public void beforeTestClass(TestContext testContext) {
        for (WireMockApi wireMockApi : getRepeatableAnnotations(testContext.getTestClass(), WireMockApi.class)) {
            WireMockServer wireMockServer = new WireMockRule(options().dynamicPort());
            wireMockServer.start();
            System.setProperty(wireMockApi.propertyKey(), String.valueOf(wireMockServer.port())); // TODO use Spring's context
            startedApis.put(wireMockApi.api(), wireMockServer);
        }
    }

    @Override
    public void beforeTestExecution(TestContext testContext) {
        ResourceLoader resourceLoader = testContext.getApplicationContext();

        for (WireMockCallMock wireMockCallMock : getRepeatableAnnotations(testContext.getTestMethod(), WireMockCallMock.class)) {
            String api = wireMockCallMock.api();
            WireMockServer wireMockServer = startedApis.get(api);
            if (wireMockServer == null)
                throw new RuntimeException("Not declarated API: " + api + ". Use @" + WireMockApi.class.getSimpleName() + "(api = \"" + api + "\")");

            UrlPattern urlPattern = urlMatching(wireMockCallMock.urlMatching());
            MappingBuilder mappingBuilder = getMappingBuilder(urlPattern, wireMockCallMock.method());
            ResponseDefinitionBuilder responseDefBuilder = aResponse()
                    .withStatus(wireMockCallMock.status())
                    .withBody(getBody(wireMockCallMock, resourceLoader));
            mappingBuilder.willReturn(responseDefBuilder);
            wireMockServer.stubFor(mappingBuilder);
        }
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        startedApis.forEach((api, wireMockServer) -> wireMockServer.stop());
        startedApis.clear();
    }

    private static MappingBuilder getMappingBuilder(UrlPattern urlPattern, HttpMethod method) {
        switch (method) {
            case GET:
                return get(urlPattern);
            case HEAD:
                return head(urlPattern);
            case POST:
                return post(urlPattern);
            case PUT:
                return put(urlPattern);
            case PATCH:
                return patch(urlPattern);
            case DELETE:
                return delete(urlPattern);
//                case OPTIONS: return options(urlPattern);
            case TRACE:
                return trace(urlPattern);
            default:
                throw new UnsupportedOperationException("Unsupported method: " + method);
        }
    }

    private static String getBody(WireMockCallMock wireMockCallMock, ResourceLoader resourceLoader) {
        if (!wireMockCallMock.body().equals(NULL)) {
            return wireMockCallMock.body();
        } else if (!wireMockCallMock.bodyResource().equals(NULL)) {
            Resource resource = resourceLoader.getResource(wireMockCallMock.bodyResource());
            try {
                return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else
            return null;
    }

}
