package fr.pinguet62.embeddedelasticsearch;

import org.junit.Ignore;
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
@SpringBootTest(classes = SampleConfig.class)
public class SampleTest {

    @Autowired
    private PaperBookRepository repository;

    @Ignore
    @Test
    public void test() {
        repository.save(PaperBookDocument.builder().author("author").title("title").description("description").build());

        Iterable<PaperBookDocument> result = repository.findAll();
        List<PaperBookDocument> items = stream(result.spliterator(), false).collect(toList());
        assertThat(items, hasSize(1));
    }

}
