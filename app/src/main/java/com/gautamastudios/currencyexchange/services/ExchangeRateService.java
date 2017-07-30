package com.gautamastudios.currencyexchange.services;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ExchangeRateService {
    private final static String mAPIKEY = "D92122FDAF3484AD3B6DC7DAE25E7650";
    private final static String mFixerAPIUrl = "http://api.fixer.io/";
    private final static String mExchangeLabUrl = "http://api.exchangeratelab.com/api/";

    private Retrofit buildRestAdapter(String url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public Response<Object> getSpecificRate(String symbols, String base) throws IOException {
        FixerExchangeService service = buildRestAdapter(mFixerAPIUrl).create(FixerExchangeService.class);
        Call<Object> call = service.getFixerSpecificRate(symbols, base);
        return call.execute();
    }

    public Response<Object> getLatest(String base) throws IOException {
        FixerExchangeService service = buildRestAdapter(mFixerAPIUrl).create(FixerExchangeService.class);

        Call<Object> call = service.getFixerLatest(base);
        return call.execute();
    }

    public Response<Object> getLatestFallback(String base) throws IOException {
        FixerExchangeService service = buildRestAdapter(mExchangeLabUrl).create(FixerExchangeService.class);

        Call<Object> call = service.getExchangeLabLatest(base, mAPIKEY);
        return call.execute();
    }

    public Response<Object> getSupportedCurrencies() throws IOException {
        FixerExchangeService service = buildRestAdapter(mExchangeLabUrl).create(FixerExchangeService.class);

        Call<Object> call = service.getSupportedCurrencies(mAPIKEY);
        return call.execute();
    }

    private interface FixerExchangeService {
        @GET("latest?")
        Call<Object> getFixerLatest(@Query("base") String base);

        @GET("current/{base}")
        Call<Object> getExchangeLabLatest(@Path("base") String base, @Query("apikey") String apiKey);

        @GET("latest?")
        Call<Object> getFixerSpecificRate(@Query("symbols") String symbols, @Query("base") String base);

        @GET("currencies?")
        Call<Object> getSupportedCurrencies(@Query("apikey") String apiKey);


    }
}
