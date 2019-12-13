package fr.pinguet62.springboot.embeddedelasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Data
@Document(indexName = "books", type = "paper_book")
@Builder
public class PaperBookDocument {
    @Id
    private String id;

    @Field(type = Keyword)
    private String author;

    @Field(type = Keyword)
    private String title;

    private String description;
}
