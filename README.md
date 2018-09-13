# *Spring Boot test* integration for *embedded-elasticsearch*

## Usage

### Configuration

```xml
<dependencies>
    <dependency>
        <groupId>fr.pinguet62.embeddedelasticsearch</groupId>
        <artifactId>embedded-elasticsearch-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

Using the *starter*:
* all transitive dependencies are auto-included: `pl.allegro.tech:embedded-elasticsearch` and `fr.pinguet62.embeddedelasticsearch:embedded-elasticsearch-spring-test`;
* the `TestExecutionListener` is auto-registered: `@TestExecutionListeners(value = EmbeddedElasticsearchTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)`.

The default host is `localhost:9300`.  
You should update your test configuration to connect to the embedded server.

### Testing

Example based on [Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/):
```java
@SpringBootTest(properties = "spring.data.elasticsearch.cluster-nodes = localhost:9300")
class MyTest {}
```

You can register the Elasticsearch configuration by annotation:
```java
@EmbeddedElasticsearch(version = "5.6.0", indexs = {
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

## TODO

* Support all configurations
* Support method annotation
* Configuration from bean (from AutoConfiguration?)
* *data set* initialisation
