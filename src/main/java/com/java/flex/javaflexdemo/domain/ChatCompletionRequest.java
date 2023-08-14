package com.java.flex.javaflexdemo.domain;


import java.util.List;

/**
 * @author kscs
 */
public class ChatCompletionRequest {

    /**
     * 选择使用的模型，如gpt-3.5-turbo
     */
    String model;

    /**
     * 发送的消息列表
     */
    List<ChatMessage> messages;

    /**
     * 温度，参数从0-2，越低表示越精准，越高表示越广发，回答的内容重复率越低
     */
    String temperature;

    /**
     * 回复条数，一次对话回复的条数
     */
    Integer n;

    /**
     * 是否流式处理，就像ChatGPT一样的处理方式，会增量的发送信息。
     */
    Boolean stream;

    /**
     * 生成的答案允许的最大token数
     */
    Integer maxTokens;

    Integer top_p;

    Integer presence_penalty;




    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Integer getTop() {
        return top_p;
    }

    public void setTop(Integer top) {
        this.top_p = top;
    }

    public Integer getPresencePenalty() {
        return presence_penalty;
    }

    public void setPresencePenalty(Integer presencePenalty) {
        this.presence_penalty = presencePenalty;
    }
}
