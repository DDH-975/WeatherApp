package com.project.weatherapp.pager_fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.weatherapp.futureRecyclerView.RecyclerDataModel;

public class PagerAdapter extends FragmentStateAdapter {
    private String temperature, precipitation, humidity, precipitationType, skyCondition;
    private RecyclerDataModel dataModel;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity,
                        String temperature, String precipitation, String humidity,
                        String precipitationType, String skyCondition, RecyclerDataModel dataModel) {
        super(fragmentActivity);
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.precipitationType = precipitationType;
        this.skyCondition = skyCondition;
        this.dataModel = dataModel;
        Log.d("ShowWeather", "setViewPager() 호출됨");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Current(temperature, precipitation, humidity, precipitationType,
                        skyCondition);
            case 1:
                return new Future(dataModel);
            default:
                return new Current(temperature, precipitation, humidity, precipitationType,
                        skyCondition);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
