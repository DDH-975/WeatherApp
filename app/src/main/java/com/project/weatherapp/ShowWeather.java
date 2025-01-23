package com.project.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.weatherapp.SrtFcst.FcstApiRespone;
import com.project.weatherapp.srtNcst.NcstApiRespone;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ShowWeather extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private String baseDate, baseTime;
    private String temperature, precipitation, humidity, skyCondition, precipitationType;
    private int nx, ny;
    private TextView tv_temperature, tv_precipitation, tv_humidity, tv_skyCondition;
    private ImageView img_precipitationType;
    private ImageButton btnBack;
    private boolean isNcstDataReady = false;
    private boolean isFcstDataReady = false;
    String Apikey = BuildConfig.Apikey;


    public void API_Request(String base_date, String ncstTime, String fcstTime, String fcstDate, int nx, int ny) {

        /********************************************************
         ******************** 초단기실황조회 ***********************
         **********************************************************/
        retrofitApi.getDataSrtNcst(Apikey,
                        "1", "10", "JSON", base_date, ncstTime, nx, ny).
                enqueue(new Callback<NcstApiRespone>() {
                    @Override
                    public void onResponse(Call<NcstApiRespone> call, retrofit2.Response<NcstApiRespone> response) {
                        if (response.isSuccessful()) {
                            NcstApiRespone data = response.body();
                            if (data != null) {
                                temperature = data.getResponse().getBody().getItmes().getItem().get(3).getObsrValue();
                                precipitation = data.getResponse().getBody().getItmes().getItem().get(2).getObsrValue();
                                humidity = data.getResponse().getBody().getItmes().getItem().get(1).getObsrValue();
                                precipitationType = data.getResponse().getBody().getItmes().getItem().get(0).getObsrValue();

                                isNcstDataReady = true;
                                if (isNcstDataReady && isFcstDataReady) {
                                    useApiData();
                                }
                                Log.d("Ncst API요청(onResponse) : ", "성공 ");
                            } else {
                                Log.d("Ncst API요청(onResponse) : ", "응답 데이터가 없음");
                            }
                        } else {
                            Log.d("Ncst API요청(onResponse) : ", "실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<NcstApiRespone> call, Throwable t) {
                        Log.d("Ncst onFailure", "실패");
                        t.printStackTrace();

                    }
                });


/********************************************************
 ******************** 초단기예보조회 ***********************
 **********************************************************/
        retrofitApi.getDataSrtFcst(Apikey,
                "1", "40", "JSON", fcstDate, fcstTime, nx, ny).enqueue(new Callback<FcstApiRespone>() {
            @Override
            public void onResponse(Call<FcstApiRespone> call, Response<FcstApiRespone> response) {
                if (response.isSuccessful()) {
                    FcstApiRespone data = response.body();
                    int count = 0;
                    if (data != null) {
                        while (true) {
                            String category = data.getResponse().getBody().getItmes().getItem().get(count).getCategory();
                            if (category.equals("SKY")) {
                                skyCondition = data.getResponse().getBody().getItmes().getItem().get(count).getFcstValue();
                                Log.d("Fcst API요청(onResponse) : ", "SKY : " + skyCondition);
                                count = 0;
                                break;
                            } else {
                                count++;
                            }
                        }
                        isFcstDataReady = true;
                        if (isNcstDataReady && isFcstDataReady) {
                            useApiData();
                        }
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


    /********************************************************
     ************ 초단기예보 시간 계산  ****************
     **********************************************************/
    public class TimeResult {
        private String baseDate;
        private String baseTime;

        public TimeResult(String baseDate, String baseTime) {
            this.baseDate = baseDate;
            this.baseTime = baseTime;
        }

        public String getBaseDate() {
            return baseDate;
        }

        public String getBaseTime() {
            return baseTime;
        }

        @Override
        public String toString() {
            return "BaseDate: " + baseDate + ", BaseTime: " + baseTime;
        }
    }


    public TimeResult FcstTime() {
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        LocalDate baseDate = currentDate; // 기본적으로 오늘 날짜로 설정
        LocalTime baseTime;

        // 0~29분일 경우 전 시간 30분, 날짜가 전날일 가능성 처리
        if (currentTime.getMinute() < 30) {
            baseTime = currentTime.minusHours(1).withMinute(30).withSecond(0).withNano(0);
            if (currentTime.getHour() == 0) { // 자정일 경우
                baseDate = currentDate.minusDays(1); // 날짜를 하루 전으로 설정
            }
        } else {
            // 30~59분일 경우 현재 시간의 30분으로 설정
            baseTime = currentTime.withMinute(30).withSecond(0).withNano(0);
        }

        String formattedBaseTime = baseTime.format(DateTimeFormatter.ofPattern("HHmm"));
        String formattedBaseDate = baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Log.d("FcstTime", "BaseDate: " + formattedBaseDate + ", BaseTime: " + formattedBaseTime);

        return new TimeResult(formattedBaseDate, formattedBaseTime);
    }

    /********************************************************
     ************ 초단기실황 시간 계산 메서드 ****************
     **********************************************************/
    public String NcstTime() {
        LocalTime currentTime = LocalTime.now();

        int minute = currentTime.getMinute();
        LocalTime baseTime;

        if (minute < 10) {
            baseTime = currentTime.minusHours(1).withMinute(59).withSecond(0).withNano(0);
        } else {
            baseTime = LocalTime.now().withSecond(0).withNano(0);
        }

        String formattedBaseTime = baseTime.format(DateTimeFormatter.ofPattern("HHmm"));
        return formattedBaseTime;
    }


    /********************************************************
     **************** 위젯에 데이터 설정 메서드 *******************
     **********************************************************/
    public void useApiData() {
        //현재 기온 설정
        tv_temperature.setText(temperature + "도");

        //현재 강수량 설정
        tv_precipitation.setText(precipitation + "mm");

        //현재 습도 설정
        tv_humidity.setText(humidity + "%");

        //하늘상태 설정
        if (skyCondition.equals("1")) {
            tv_skyCondition.setText("맑음");
        } else if (skyCondition.equals("3")) {
            tv_skyCondition.setText("구름많음");
        } else if (skyCondition.equals("4")) {
            tv_skyCondition.setText("흐림");
        }

        //이미지 설정
        if (precipitationType.equals("0")) {
            if (skyCondition.equals("1")) {
                img_precipitationType.setImageResource(R.drawable.baseline_sunny_24);
            } else if (skyCondition.equals("3")) {
                img_precipitationType.setImageResource(R.drawable.cloudy);
            } else if (skyCondition.equals("4")) {
                img_precipitationType.setImageResource(R.drawable.partly_cloudy);
            }
        } else if (precipitationType.equals("1") || precipitationType.equals("5")) {
            if (skyCondition.equals("1")) {
                img_precipitationType.setImageResource(R.drawable.baseline_water_drop_24);
            } else if (skyCondition.equals("3") || skyCondition.equals("4")) {
                img_precipitationType.setImageResource(R.drawable.rounded_rainy_24);
            }
        } else if (precipitationType.equals("2") || precipitationType.equals("6")) {
            img_precipitationType.setImageResource(R.drawable.outline_weather_mix_24);
        } else if (precipitationType.equals("3") || precipitationType.equals("7")) {
            if (skyCondition.equals("1")) {
                img_precipitationType.setImageResource(R.drawable.baseline_ac_unit_24);
            } else if (skyCondition.equals("3") || skyCondition.equals("4")) {
                img_precipitationType.setImageResource(R.drawable.baseline_cloudy_snowing_24);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_weather);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_precipitation = (TextView) findViewById(R.id.tv_precipitation);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_skyCondition = (TextView) findViewById(R.id.tv_skyCondition);
        img_precipitationType = (ImageView) findViewById(R.id.img_precipitationType);
        btnBack = (ImageButton) findViewById(R.id.back_btn);

        retrofit = NetworkClient.getRetrofitClient(ShowWeather.this);
        retrofitApi = retrofit.create(RetrofitApi.class);

        Intent intent = getIntent();
        String str = intent.getStringExtra("location");
        TextView textView = (TextView) findViewById(R.id.tv_city);
        textView.setText(str);

        // nx : 예보지점의 X 좌표값, nx: 예보지점의 Y 좌표값
        nx = intent.getIntExtra("nx", -1);
        ny = intent.getIntExtra("ny", -1);


        LocalDate currentDate = LocalDate.now();
        baseDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String ncstTime = NcstTime();

        TimeResult timeResult = FcstTime();
        String fcstTime = timeResult.getBaseTime();
        String fcstDate = timeResult.getBaseDate();

        Log.d("현재 날짜 : ", baseDate);
        Log.d("현재시간 : ", ncstTime);
        Log.d("fcstTime : ", fcstTime);
        Log.d("fcstDate : ", fcstDate);


        //api요청
        API_Request(baseDate, ncstTime, fcstTime, fcstDate, nx, ny);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
