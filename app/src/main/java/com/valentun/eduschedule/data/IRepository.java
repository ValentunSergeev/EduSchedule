package com.valentun.eduschedule.data;


import com.valentun.parser.pojo.School;

import io.reactivex.Observable;

public interface IRepository {
    Observable<School> getSchool();
}
