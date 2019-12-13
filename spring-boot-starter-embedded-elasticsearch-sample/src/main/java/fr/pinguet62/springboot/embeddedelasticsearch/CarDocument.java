package fr.pinguet62.springboot.embeddedelasticsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Data
@Document(indexName = "cars", type = "car")
@Builder
public class CarDocument {
    @Id
    private String id;

    @Field(type = Keyword)
    private String manufacturer;

    @Field(type = Keyword)
    private String model;

    private String description;
}
