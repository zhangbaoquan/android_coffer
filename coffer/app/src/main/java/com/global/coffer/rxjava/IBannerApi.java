package com.global.coffer.rxjava;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface IBannerApi {
    @GET(".")
    Observable<BannerBean> getBannerInfo();
}
