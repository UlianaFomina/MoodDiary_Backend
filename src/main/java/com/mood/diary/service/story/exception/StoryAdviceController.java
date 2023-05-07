package com.mood.diary.service.story.exception;

import com.mood.diary.service.exception.model.ErrorDetail;
import com.mood.diary.service.exception.model.ErrorMessage;
import com.mood.diary.service.story.exception.variants.StoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class StoryAdviceController {

    @ExceptionHandler(StoryNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userEmailNotConfirmedException(StoryNotFoundException storyNotFoundException, WebRequest webRequest) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                List.of(new ErrorDetail(storyNotFoundException.getMessage())),
                webRequest.getDescription(false)
        );
    }
}
