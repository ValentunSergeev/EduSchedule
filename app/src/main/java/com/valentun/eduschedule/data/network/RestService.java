package com.valentun.eduschedule.data.network;


import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.data.dto.VersionInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {
    @GET("schools/{id}.json")
    Observable<SchoolInfo> getSchoolInfo(@Path("id") int id);

    @GET("schools.json")
    Observable<List<SchoolInfo>> getSchools();

    @GET("app_version")
    Observable<VersionInfo> getVersionInfo();
}
