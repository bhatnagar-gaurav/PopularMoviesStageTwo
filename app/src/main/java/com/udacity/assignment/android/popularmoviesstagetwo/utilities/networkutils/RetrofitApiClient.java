package com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils;

import android.content.Context;

import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
