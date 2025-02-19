package com.project.weatherapp.pager_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.weatherapp.R;


public class Current extends Fragment {
    private View view;
    private TextView tv_temperature, tv_precipitation, tv_humidity, tv_skyCondition;
    private ImageView img_precipitationType;
    private com.project.weatherapp.srtFcst.Items FcstItems = new com.project.weatherapp.srtFcst.Items();
    private com.project.weatherapp.srtNcst.Items NcstItems = new com.project.weatherapp.srtNcst.Items();
    private String temperature, precipitation, humidity, precipitationType, skyCondition;

    public Current(String temperature, String precipitation, String humidity, String precipitationType
            , String skyCondition) {
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.precipitationType = precipitationType;
        this.skyCondition = skyCondition;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_current_weather, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_precipitation = (TextView) view.findViewById(R.id.tv_precipitation);
        tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);
        tv_skyCondition = (TextView) view.findViewById(R.id.tv_skyCondition);
        img_precipitationType = (ImageView) view.findViewById(R.id.img_precipitationType);

        useApiData();

    }


    //위젯에 데이터 설정 메서드
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
}
