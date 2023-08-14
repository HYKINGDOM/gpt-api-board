package com.java.flex.javaflexdemo.gpt;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.java.flex.javaflexdemo.constant.ChatMessageRole;
import com.java.flex.javaflexdemo.domain.ChatCompletionChoice;
import com.java.flex.javaflexdemo.domain.ChatCompletionRequest;
import com.java.flex.javaflexdemo.domain.ChatCompletionResponse;
import com.java.flex.javaflexdemo.domain.ChatMessage;
import com.java.flex.javaflexdemo.util.HttpClientUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static com.java.flex.javaflexdemo.constant.MessageContant.LONG_STR;

/**
 * @author kscs
 */
public class OpenAiApi {

    private static final ChatMessage SYSTEM_MESSAGE = new ChatMessage(ChatMessageRole.system.getValue(), LONG_STR);
    private static final List<ChatMessage> MESSAGE_ARRAYLIST = Lists.newArrayList(SYSTEM_MESSAGE);

    private static final String APP_TOKEN = "";

    private static final String APP_URL = "https://gptgod.online/api/v1/chat/completions";

    private static final Gson GSON_BUILDER = new Gson();

    public static String executeRequest(String coordinate) {

        System.out.println("===============start=================");

        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setModel("gpt-3.5-turbo-16k");

        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.getValue(), coordinate);

        if (MESSAGE_ARRAYLIST.size() == 1) {
            MESSAGE_ARRAYLIST.add(userMessage);
            chatCompletionRequest.setMessages(MESSAGE_ARRAYLIST);
        } else {
            chatCompletionRequest.setMessages(Lists.newArrayList(SYSTEM_MESSAGE, userMessage));
        }
        chatCompletionRequest.setMaxTokens(500);
        chatCompletionRequest.setTemperature("0.8");
        chatCompletionRequest.setPresencePenalty(1);
        chatCompletionRequest.setTop(1);
        chatCompletionRequest.setStream(Boolean.FALSE);

        String executeRet = executeInputStream(chatCompletionRequest);

        ChatCompletionResponse result = GSON_BUILDER.fromJson(executeRet, ChatCompletionResponse.class);

        List<ChatCompletionChoice> choices = result.getChoices();

        ChatMessage context =
            new ChatMessage(choices.get(0).getMessage().getRole(), choices.get(0).getMessage().getContent());
        MESSAGE_ARRAYLIST.add(context);
        String subBetween = StrUtil.subBetween(context.getContent(), "[", "]");

        String cleanBlankOperate = StrUtil.cleanBlank(subBetween);
        System.out.println(cleanBlankOperate);

        System.out.println("===============end=================");

        return cleanBlankOperate;
    }

    public static String executeInputStream(ChatCompletionRequest chatCompletionRequest) {

        String gsonBuilderJson = GSON_BUILDER.toJson(chatCompletionRequest);

        System.out.printf("Request param: %s", gsonBuilderJson);

        InputStream responseStream = HttpClientUtil.sendPostAsync(APP_URL, APP_TOKEN, gsonBuilderJson);

        String responseBody = null;

        try {
            // 处理收到的流
            // 创建一个BufferedReader对象，以便逐行读取流中的数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

            // 创建一个StringBuilder对象，用于存储从流中读取的数据
            StringBuilder stringBuilder = new StringBuilder();

            // 使用BufferedReader逐行读取流中的数据，并将其追加到StringBuilder中
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // 关闭InputStream和BufferedReader，以释放资源
            responseStream.close();
            reader.close();

            // 使用StringBuilder中的数据进行进一步处理，这里我们只是将其打印出来
            responseBody = stringBuilder.toString();

        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        System.out.printf("Response body: %s", responseBody);

        return responseBody;
    }
}
