package fr.pinguet62.embeddedelasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "books", type = "paper_book")
@Builder
public class PaperBookDocument {
    @Id
    private String id;

    private String author;
    private String title;
    private String description;
}
