package fr.pinguet62.springboot.embeddedelasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperBookRepository extends ElasticsearchRepository<PaperBookDocument, String> {
}
