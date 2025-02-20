package com.project.weatherapp.futureRecyclerView;


import android.util.Log;

import java.util.ArrayList;

public class RecyclerDataModel {
    private ArrayList<String> skyList = new ArrayList<>();
    private ArrayList<String> temperatureList = new ArrayList<>();
    private ArrayList<String> ptyList = new ArrayList<>();
    private ArrayList<String> timeList = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();

    public void addListSkyData(String skyData) {
        Log.d("addListSkyData","sky : "+skyData);
        skyList.add(skyData);
    }

    public void addListTemperatureData(String temperatureData) {
        temperatureList.add(temperatureData);
    }

    public void addListPtyData(String ptyData) {
        ptyList.add(ptyData);
    }

    public void addListTimeData(String timeData) {
        timeList.add(timeData);
    }

    public void addListDateData(String dateData) {
        dateList.add(dateData);
    }

    public ArrayList<String> getSkyList() {
        return skyList;
    }

    public ArrayList<String> getTemperatureList() {
        return temperatureList;
    }

    public ArrayList<String> getPtyList() {
        return ptyList;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public ArrayList<String> getDateList() {
        return dateList;
    }
}
