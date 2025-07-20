package com.lucky.utils.http;

import lombok.Getter;
import lombok.Setter;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

public class Httphelp {

    @Getter
    @Setter
    private OkHttpClient client;

    public Httphelp() {
        client = new OkHttpClient();
    }

    public Httphelp(String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        client = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

    }

    public Response Get(String url) throws IOException {
        return Get(url, null);
    }


    public Response Get(String url, Map<String, String> headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }

    private Response Post(String url, Map<String, String> headers, RequestBody requestBody) throws IOException {
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.build();

        return client.newCall(request).execute();
    }

    private Response PostJson(String url, Map<String, String> headers, String json) throws IOException {
        return Post(url, headers, RequestBody.create(json, MediaType.get("application/json; charset=utf-8")));
    }

    private Response PostJson(String url, String json) throws IOException {
        return PostJson(url, null, json);
    }


}
