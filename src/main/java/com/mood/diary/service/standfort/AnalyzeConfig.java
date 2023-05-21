package com.mood.diary.service.standfort;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class AnalyzeConfig {

    @Bean
    public StanfordCoreNLP pipeline() {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

        return new StanfordCoreNLP(properties);
    }
}
