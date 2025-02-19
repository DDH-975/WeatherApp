package com.project.weatherapp;



import com.project.weatherapp.srtFcst.FcstApiRespone;
import com.project.weatherapp.srtNcst.NcstApiRespone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst
//?serviceKey=인증키&numOfRows=10&pageNo=1
//&base_date=20210628&base_time=0600&nx=55&ny=127
//예시 요청URL인데 여기에 있는 모든 파라미터들을 쿼리로 작성해준다.
//그러면 요청 URL을 Retrofit이 자동으로 작성해준다.
//만약 각 기능마다 요구하는 요청 메시지가 다르면 각 기능에 맞는 @GET 메서드를 만들어야 한다.

public interface RetrofitApi {
    @GET("getUltraSrtNcst")
    Call<NcstApiRespone> getDataSrtNcst(
            @Query("serviceKey") String serviceKey,
            @Query("pageNo") String pageNo,
            @Query("numOfRows") String numOfRows,
            @Query("dataType") String dataType,
            @Query("base_date") String base_data,
            @Query("base_time") String base_time,
            @Query("nx") int nx,
            @Query("ny") int ny
    );

    @GET("getUltraSrtFcst")
    Call<FcstApiRespone> getDataSrtFcst(
            @Query("serviceKey") String serviceKey,
            @Query("pageNo") String pageNo,
            @Query("numOfRows") String numOfRows,
            @Query("dataType") String dataType,
            @Query("base_date") String base_data,
            @Query("base_time") String base_time,
            @Query("nx") int nx,
            @Query("ny") int ny
    );

}



