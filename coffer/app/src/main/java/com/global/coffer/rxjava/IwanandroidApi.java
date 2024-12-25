package com.global.coffer.rxjava;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IwanandroidApi {
    @GET("banner/json/")
    Observable<BannerBean> getBannerInfo();

    @GET("popular/column/json")
    Observable<PopularInfoBean> getPopularInfo();
}
