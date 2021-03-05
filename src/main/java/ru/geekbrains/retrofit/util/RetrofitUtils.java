package ru.geekbrains.retrofit.util;

import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.MalformedURLException;
import java.time.Duration;

@UtilityClass
public class RetrofitUtils {
    HttpLoggingInterceptor logging =  new HttpLoggingInterceptor(new PrettyLogger());

    public Retrofit getRetrofit() throws MalformedURLException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMinutes(1l))
                .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .baseUrl(ConfigUtils.getBaseUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}

