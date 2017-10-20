package com.valentun.eduschedule.data.network;


import com.valentun.parser.pojo.NamedEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RestService {
    @GET("schools")
    Observable<NamedEntity> getSchools();
}
