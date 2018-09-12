package fr.pinguet62.embeddedelasticsearch;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic.Builder;
import pl.allegro.tech.embeddedelasticsearch.IndexSettings;

import static org.apache.commons.logging.LogFactory.getLog;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.CLUSTER_NAME;
import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.TRANSPORT_TCP_PORT;

public class EmbeddedElasticsearchTestExecutionListener extends AbstractTestExecutionListener {

    private static final Log logger = getLog(EmbeddedElasticsearchTestExecutionListener.class);

    private EmbeddedElastic elasticsearch;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * Should be executed before {@link org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration}.
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    public void beforeTestClass(TestContext testContext) throws Exception {
        EmbeddedElasticsearch config = findAnnotation(testContext.getTestClass(), EmbeddedElasticsearch.class);
        if (config == null) {
            logger.debug("Skipped: no @" + EmbeddedElasticsearch.class.getSimpleName() + " found");
            return;
        }

        testContext.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this); // fix: ResourceLoader resourceLoader = testContext.getApplicationContext().getBean(ResourceLoader.class)

        Builder eeBuilder = EmbeddedElastic.builder()
                .withElasticVersion(config.version())
                .withSetting(TRANSPORT_TCP_PORT, config.port())
                .withSetting(CLUSTER_NAME, config.clusterName());
        for (EmbeddedElasticsearchIndex index : config.indexs()) {
            IndexSettings.Builder indexBuilder = IndexSettings.builder();
            for (EmbeddedElasticsearchType type : index.types()) {
                Resource resource = resourceLoader.getResource(type.definition());
                indexBuilder = indexBuilder.withType(type.name(), resource.getInputStream());
            }
            eeBuilder = eeBuilder.withIndex(index.name(), indexBuilder.build());
        }
        elasticsearch = eeBuilder.build();

        logger.debug("Starting embedded Elasticsearch...");
        elasticsearch.start();
    }

    public void afterTestClass(TestContext testContext) {
        if (elasticsearch == null) return;

        logger.debug("Stopping embedded Elasticsearch...");
        elasticsearch.stop();
    }

}
