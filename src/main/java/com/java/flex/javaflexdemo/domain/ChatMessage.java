package com.java.flex.javaflexdemo.domain;

/**
 * @author kscs
 */
public class ChatMessage {

    /**
     * 消息角色
     */
    String role;

    /**
     * 消息内容
     */
    String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatMessage{" + "role='" + role + '\'' + ", content='" + content + '\'' + '}';
    }
}
