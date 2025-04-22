package com.project.weatherapp.futureRecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.weatherapp.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Viewholder> {
        private RecyclerDataModel dataModel;

    public RecyclerAdapter(RecyclerDataModel dataModel) {
        this.dataModel = dataModel;

    }

    @NonNull
    @Override
    public RecyclerAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.future_items, parent, false);
        Viewholder holder = new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.Viewholder holder, int position) {
        String temperature = dataModel.getTemperatureList().get(position);
        String precipitationType = dataModel.getPtyList().get(position);
        String skyCondition = dataModel.getSkyList().get(position);
        String time = dataModel.getTimeList().get(position);
        String formattedTime = time.substring(0,2)+" : "+ time.substring(2);
        String date = dataModel.getDateList().get(position);
        String formattedDate = date.substring(0,4)+"년 "+date.substring(4,6)+"월 "+ date.substring(6,8)+"일";

        holder.tv_date.setText(formattedDate);
        holder.tv_time.setText(formattedTime);
        holder.tv_temper.setText(temperature+"도");
        useApiData(precipitationType, skyCondition, holder.img_precipitationType);

    }

    @Override
    public int getItemCount() {
        return dataModel.getSkyList().size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img_precipitationType;
        TextView tv_time, tv_temper, tv_date;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img_precipitationType = (ImageView) itemView.findViewById(R.id.img_sky);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_temper = (TextView) itemView.findViewById(R.id.tv_temper);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }


    //위젯에 데이터 설정 메서드
    public void useApiData(String precipitationType, String skyCondition, ImageView img_precipitationType ) {
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
