# *Spring Boot* auto-configuration modules

## [Embedded Elasticsearch](https://github.com/allegro/embedded-elasticsearch)

```xml
<dependencies>
    <dependency>
        <groupId>fr.pinguet62</groupId>
        <artifactId>spring-boot-starter-embedded-elasticsearch</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

```java
@EmbeddedElasticsearch(version = "5.6.0", indexes = {
        @EmbeddedElasticsearchIndex(name = "cars", types = {
                @EmbeddedElasticsearchType(name = "car", definition = "classpath:car-mapping.json"),
        }),
        @EmbeddedElasticsearchIndex(name = "books", types = {
                @EmbeddedElasticsearchType(name = "paper_book", definition = "classpath:paper-book-mapping.json"),
                @EmbeddedElasticsearchType(name = "audio_book", definition = "classpath:audio-book-mapping.json")
        })
})
class MyTest {}
```

## [WireMock](http://wiremock.org/docs/junit-rule)

```xml
<dependencies>
    <dependency>
        <groupId>fr.pinguet62</groupId>
        <artifactId>spring-boot-starter-wiremock</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

```java
@WireMockApi(api = "facebook", propertyKey = "api.facebook.port")
// Spring boot annotations...
class MyTest {
    @Test
    @WireMockCallMock(api = "facebook", method = GET, urlMatching = "/second", status = 200, bodyResource = "classpath:fr/pinguet62/springboot/wiremock/test.txt")
    public void myTest() {}
}
```
