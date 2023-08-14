package com.java.flex.javaflexdemo.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.java.flex.javaflexdemo.util.ThreadExecutorUtil.threadExecutor;

/**
 * @author kscs
 */
public class HttpClientUtil {
    /**
     * 初始化httpclient
     */
    private static final HttpClient HTTP_CLIENT =
        HttpClient.newBuilder().executor(threadExecutor()).version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofMillis(5000)).build();



    public static InputStream sendPostAsync(String url, String token, String requestBody) {

        HttpRequest httpRequest =
            HttpRequest.newBuilder().uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        CompletableFuture<InputStream> result =

            HTTP_CLIENT.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofInputStream()).whenComplete((resp, throwable) -> {

                if (ObjectUtil.isNotEmpty(throwable)) {

                    System.out.printf("sendPostAsync: {%s}", (Object)throwable.getStackTrace());

                }
            }).thenApply(HttpResponse::body).exceptionally(err -> {

                System.out.printf("sendPostAsync: {%s}", (Object)err.getStackTrace());

                return InputStream.nullInputStream();
            });

        return result.join();
    }

    public List<String> sendBatchHttpAsync(List<String> urls) {

        List<HttpRequest> requests =
            urls.stream().map(url -> HttpRequest.newBuilder(URI.create(url))).map(HttpRequest.Builder::build).toList();

        List<CompletableFuture<HttpResponse<String>>> futures =
            requests.stream().map(request -> HTTP_CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

        futures.forEach(e -> e.whenComplete((resp, throwable) -> {
            if (ObjectUtil.isNotEmpty(throwable)) {
                System.out.printf("sendHttpAsync: {%s}", (Object)throwable.getStackTrace());
            }
        }).thenApply(HttpResponse::body).exceptionally(err -> {
            System.out.printf("sendHttpAsync: {%s}", (Object)err.getStackTrace());
            return ExceptionUtil.getRootCauseMessage(err);
        }));
        // CompletableFuture.allOf(futures.toArray(CompletableFuture<?>[]::new)).join();

        return futures.stream().map(CompletableFuture::join).map(HttpResponse::body).collect(Collectors.toList());
    }

    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    public HttpResponse<InputStream> downLoadVideo(String url) {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try {

            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                String errorMessage = new BufferedReader(new InputStreamReader(response.body())).lines()
                    .collect(Collectors.joining("\n"));
                throw new IOException("Failed to download video: " + response.statusCode() + "\n" + errorMessage);
            }

            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
