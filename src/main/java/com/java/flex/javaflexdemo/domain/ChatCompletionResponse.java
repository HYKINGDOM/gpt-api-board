package com.java.flex.javaflexdemo.domain;


import java.util.List;

/**
 * @author kscs
 */
public class ChatCompletionResponse {


    private String id;


    private String object;


    private List<ChatCompletionChoice> choices;

    private String finish_reason;

    public ChatCompletionResponse(String id, String object, List<ChatCompletionChoice> choices, String finishReason) {
        this.id = id;
        this.object = object;
        this.choices = choices;
        this.finish_reason = finishReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<ChatCompletionChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<ChatCompletionChoice> choices) {
        this.choices = choices;
    }

    public String getFinishReason() {
        return finish_reason;
    }

    public void setFinishReason(String finishReason) {
        this.finish_reason = finishReason;
    }
}
