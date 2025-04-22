package com.project.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.project.weatherapp.futureRecyclerView.RecyclerDataModel;
import com.project.weatherapp.pager_fragment.PagerAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import me.relex.circleindicator.CircleIndicator3;


public class ShowWeather extends AppCompatActivity {
    private String baseDate;
    private int nx, ny;
    private ViewPager2 viewPager2;
    private PagerAdapter adapter;
    private CircleIndicator3 circleIndicator3;
    private String temperature, precipitation, humidity, precipitationType,  skyCondition;

    //초단기예보 시간 계산
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
        LocalTime currentTime = LocalTime.now().minusMinutes(15); // 15분 전 기준
        LocalDate baseDate = LocalDate.now();

        if (currentTime.getHour() == 23 && LocalTime.now().getHour() == 0) {
            // 자정 넘어간 경우 날짜 보정
            baseDate = baseDate.minusDays(1);
        }

        int hour = currentTime.getHour();
        String baseTime = String.format("%02d30", hour);

        String formattedBaseDate = baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Log.d("FcstTime_Adjusted", "BaseDate: " + formattedBaseDate + ", BaseTime: " + baseTime);
        return new TimeResult(formattedBaseDate, baseTime);
    }


    //초단기실황 시간 계산 메서드
    public String NcstTime() {
        LocalTime currentTime = LocalTime.now().minusMinutes(10); // 약간 보정

        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        int baseMinute;

        if (minute < 30) {
            baseMinute = 0;
        } else {
            baseMinute = 30;
        }

        LocalTime baseTime = LocalTime.of(hour, baseMinute);
        return baseTime.format(DateTimeFormatter.ofPattern("HHmm"));
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

        Intent intent = getIntent();
        String str = intent.getStringExtra("location");
        TextView textView = (TextView) findViewById(R.id.tv_city);
        textView.setText(str);

        nx = intent.getIntExtra("nx", -1);
        ny = intent.getIntExtra("ny", -1);

        LocalDate currentDate = LocalDate.now();
        baseDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String ncstTime = NcstTime();

        TimeResult timeResult = FcstTime();
        String fcstTime = timeResult.getBaseTime();
        String fcstDate = timeResult.getBaseDate();

        viewPager2 = (ViewPager2) findViewById(R.id.view_Pager);
        circleIndicator3 = (CircleIndicator3) findViewById(R.id.indicator);

        Context context = ShowWeather.this;
        ApiRequest apiRequest = new ApiRequest(context, fcstTime, fcstDate, ncstTime, baseDate, nx, ny,
                new ApiRequest.ApiCallBack() {
                    @Override
                    public void onNcstDataReceived(String temperature, String precipitation,
                                                   String humidity, String precipitationType, String skyCondition, RecyclerDataModel dataModel) {

                            viewPager2.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (temperature != null && precipitation != null && humidity != null && precipitationType != null && skyCondition != null) {
                                        ShowWeather.this.temperature = temperature;
                                        ShowWeather.this.precipitation = precipitation;
                                        ShowWeather.this.humidity = humidity;
                                        ShowWeather.this.precipitationType = precipitationType;
                                        ShowWeather.this.skyCondition = skyCondition;

                                        adapter = new PagerAdapter(ShowWeather.this,
                                                ShowWeather.this.temperature, ShowWeather.this.precipitation,
                                                ShowWeather.this.humidity, ShowWeather.this.precipitationType,
                                                ShowWeather.this.skyCondition, dataModel);
                                        viewPager2.setAdapter(adapter);
                                        circleIndicator3.setViewPager(viewPager2);
                                    }
                                }
                            });
                        }

                });
        apiRequest.API_Request();
    }
}
