package fr.pinguet62.springboot.embeddedelasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleConfig.class, properties = "spring.data.elasticsearch.cluster-nodes = localhost:9300")
@EmbeddedElasticsearch(version = "6.8.0", indexes = {
        @EmbeddedElasticsearchIndex(name = "cars", types = {
                @EmbeddedElasticsearchType(name = "car", definition = "classpath:cars-mapping.json"),
        }),
        @EmbeddedElasticsearchIndex(name = "books", types = {
                @EmbeddedElasticsearchType(name = "paper_book", definition = "classpath:paper-book-mapping.json"),
        })
})
public class SampleTest {

    @Autowired
    private PaperBookRepository repository;

    @Test
    public void test() {
        repository.save(PaperBookDocument.builder().author("author").title("title").description("description").build());

        Iterable<PaperBookDocument> result = repository.findAll();
        List<PaperBookDocument> items = stream(result.spliterator(), false).collect(toList());
        assertThat(items, hasSize(1));
    }

}
