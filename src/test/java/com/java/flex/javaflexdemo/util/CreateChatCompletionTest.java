package com.java.flex.javaflexdemo.util;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.java.flex.javaflexdemo.domain.ChatMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.java.flex.javaflexdemo.gpt.OpenAiApi.executeRequest;

public class CreateChatCompletionTest {

    List<ChatMessage> messages = new ArrayList<>();

    @Test
    public void createChatCompletion() {
        executeRequest("[8,9]");
    }


    @Test
    public void createChatCompletion_01() {
        Pair<Integer, Integer> gpt = gpt(9, 10);
        System.out.println(gpt);
    }


    private Pair<Integer, Integer> gpt(int row, int col) {

        String str = "[%s,%s]";

        String coordinateUser = String.format(str, row, col);

        String coordinateGpt = executeRequest(coordinateUser);

        Integer subAfter = Integer.valueOf(StrUtil.subAfter(coordinateGpt, ",", true));

        Integer subBefore = Integer.valueOf(StrUtil.subBefore(coordinateGpt, ",", true));

        return new Pair<>(subBefore, subAfter);
    }
}