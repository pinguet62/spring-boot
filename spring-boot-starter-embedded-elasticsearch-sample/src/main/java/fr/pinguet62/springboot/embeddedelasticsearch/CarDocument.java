package fr.pinguet62.springboot.embeddedelasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(indexName = "cars", type = "car")
public class CarDocument {
    @Id
    private String id;

    @Field(type = Keyword)
    private String manufacturer;

    @Field(type = Keyword)
    private String model;

    private String description;
}
