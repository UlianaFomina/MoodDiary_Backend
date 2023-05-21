package com.mood.diary.service.standfort;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {

    private final StanfordCoreNLP pipeline;

    @Override
    public double satisfaction(String text) {
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

        return convertSentimentToValue(sentiment);
    }

    private double convertSentimentToValue(String sentiment) {
        return switch (sentiment) {
            case "Very negative" -> -2.0;
            case "Negative" -> -1.0;
            case "Neutral" -> 0.0;
            case "Positive" -> 1;
            case "Very positive" -> 2;
            default -> 0.0;
        };
    }
}
