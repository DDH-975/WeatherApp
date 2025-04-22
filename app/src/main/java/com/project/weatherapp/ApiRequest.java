package com.project.weatherapp;

import android.content.Context;
import android.util.Log;

import com.project.weatherapp.futureRecyclerView.RecyclerDataModel;
import com.project.weatherapp.srtFcst.FcstApiRespone;
import com.project.weatherapp.srtFcst.Item;
import com.project.weatherapp.srtNcst.NcstApiRespone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ApiRequest {

    private String Apikey = BuildConfig.Apikey;
    private Context context;
    private String fcstTime;
    private String fcstDate;
    private String ncstTime;
    private String baseDate;
    private int nx, ny;
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private boolean isNcstDataReady = false;
    private boolean isFcstDataReady = false;
    private ApiCallBack apiCallBack;
    private String temperature, precipitation, humidity, precipitationType, skyCondition;
    private RecyclerDataModel dataModel;

    public interface ApiCallBack {
        void onNcstDataReceived(String temperature, String precipitation, String humidity,
                                String precipitationType, String skyCondition, RecyclerDataModel dataModel);
    }

    public ApiRequest(Context context, String fcstTime, String fcstDate, String ncstTime,
                      String baseDate, int nx, int ny, ApiCallBack apiCallBack) {
        this.context = context;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.ncstTime = ncstTime;
        this.baseDate = baseDate;
        this.ny = ny;
        this.nx = nx;
        this.apiCallBack = apiCallBack;
    }


    public void API_Request() {
        // Retrofit 객체 초기화
        retrofit = NetworkClient.getRetrofitClient(context);
        retrofitApi = retrofit.create(RetrofitApi.class);

        dataModel = new RecyclerDataModel();

        // 초단기실황조회 (Ncst)
        retrofitApi.getDataSrtNcst(Apikey, "1", "10", "JSON", baseDate, ncstTime, nx, ny)
                .enqueue(new Callback<NcstApiRespone>() {
                    @Override
                    public void onResponse(Call<NcstApiRespone> call, Response<NcstApiRespone> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("Ncst API요청(onResponse) : ", "성공 ");
                            NcstApiRespone data = response.body();
                            temperature = data.getResponse().getBody().getItmes().getItem().get(3).getObsrValue();
                            precipitation = data.getResponse().getBody().getItmes().getItem().get(2).getObsrValue();
                            humidity = data.getResponse().getBody().getItmes().getItem().get(1).getObsrValue();
                            precipitationType = data.getResponse().getBody().getItmes().getItem().get(0).getObsrValue();
                            isNcstDataReady = true;
                            Log.d("NcstReady", "NcstReady : " + isNcstDataReady);

                        } else {
                            Log.d("Ncst API요청(onResponse) : ", "응답 데이터가 없음");
                        }
                    }

                    @Override
                    public void onFailure(Call<NcstApiRespone> call, Throwable t) {
                        Log.d("Ncst onFailure", "실패");
                        t.printStackTrace();
                    }
                });

        // 초단기예보조회 (Fcst)
        retrofitApi.getDataSrtFcst(Apikey,
                "1", "100", "JSON", fcstDate, fcstTime, nx, ny).enqueue(new Callback<FcstApiRespone>() {
            @Override
            public void onResponse(Call<FcstApiRespone> call, Response<FcstApiRespone> response) {
                if (response.isSuccessful()) {
                    FcstApiRespone data = response.body();
                    if (data != null) {
                        for (Item item : data.getResponse().getBody().getItmes().getItem()) {
                            if (item.getCategory().equals("SKY")) {
                                skyCondition = item.getFcstValue();
                                break;
                            }
                        }

                        for (Item item : data.getResponse().getBody().getItmes().getItem()) {
                            if (item.getCategory().equals("PTY")) {
                                dataModel.addListPtyData(item.getFcstValue());
                                dataModel.addListDateData(item.getFcstDate());
                                dataModel.addListTimeData(item.getFcstTime());
                            } else if (item.getCategory().equals("SKY")) {
                                dataModel.addListSkyData(item.getFcstValue());
                            } else if (item.getCategory().equals("T1H")) {
                                dataModel.addListTemperatureData(item.getFcstValue());
                            }
                        }

                        isFcstDataReady = true;
                        Log.d("FcstReady", "FcstReady : " + isFcstDataReady);
                        checkAndSendData();

                        Log.d("Fcst API요청(onResponse) : ", "성공 ");
                    } else {
                        Log.d("Fcst API요청(onResponse) : ", "응답 데이터가 없음");
                    }


                } else {
                    Log.d("Fcst API요청(onResponse) : ", "실패");
                }
            }

            @Override
            public void onFailure(Call<FcstApiRespone> call, Throwable t) {
                Log.d("Fcst onFailure", "실패");
                t.printStackTrace();
            }
        });
    }

    //  데이터가 준비되었는지 확인하고 콜백 호출
    private void checkAndSendData() {
        if (isNcstDataReady && isFcstDataReady) {
            Log.i("checkAndSendData()", "둘다 true 임");
            apiCallBack.onNcstDataReceived(temperature, precipitation, humidity, precipitationType,
                    skyCondition, dataModel);
        } else {
            Log.i("checkAndSendData()", "아님 뭐가 잘못됨");
        }
    }

}
