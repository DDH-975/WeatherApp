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

    //초단기실황 시간 계산 메서드
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
                                                   String humidity, String precipitationType, String skyCondition) {

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
                                                ShowWeather.this.skyCondition);
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
