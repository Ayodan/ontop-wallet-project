package com.ontop.wallet.config;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class Client {
    @Value("${mock.host}")
    private String host;
    private final static String PAYMENT_CREATION_PATH = "/api/v1/payments";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .build();

    public String transferServiceCall(String json) throws IOException {
        String url = host + PAYMENT_CREATION_PATH;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
