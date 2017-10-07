package com.valentun.parser;

import android.util.Log;

import com.valentun.parser.pojo.School;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Parser {
    private static final String LOG_TAG = "ScheduleParser";

    private boolean isLogEnabled = true;

    public void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }

    public Observable<School> parseFrom(String rawData) {
        return Observable.just(rawData)
                .subscribeOn(Schedulers.io())
                .map(this::parse)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private School parse(String data) {
        String json = getRawJson(data);
        log(json);

        return new School();
    }

    private String getRawJson(String rawData) {
        int index = rawData.indexOf('{');
        return rawData.substring(index);
    }

    private void log(String s) {
        if(isLogEnabled) Log.d(LOG_TAG, s);
    }
}
