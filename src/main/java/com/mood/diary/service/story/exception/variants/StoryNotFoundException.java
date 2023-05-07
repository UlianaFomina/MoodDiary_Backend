package com.mood.diary.service.story.exception.variants;

public class StoryNotFoundException extends RuntimeException {

    public StoryNotFoundException(String message) {
        super(message);
    }
}
