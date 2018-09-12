package fr.pinguet62.embeddedelasticsearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleConfig.class, properties = "spring.data.elasticsearch.cluster-nodes = localhost:9200")
@TestExecutionListeners(value = EmbeddedElasticsearchTestExecutionListener.class, mergeMode = MERGE_WITH_DEFAULTS)
@EmbeddedElasticsearch(version = "5.6.0", indexs = {
//        @EmbeddedElasticsearchIndex(name = "cars", types = {
//                @EmbeddedElasticsearchType(name = "car", definition = "classpath:car-mapping.json"),
//        }),
        @EmbeddedElasticsearchIndex(name = "books", types = {
                @EmbeddedElasticsearchType(name = "paper_book", definition = "classpath:paper-book-mapping.json"),
                @EmbeddedElasticsearchType(name = "audio_book", definition = "classpath:audio-book-mapping.json")
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
