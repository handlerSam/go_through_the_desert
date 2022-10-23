package com.example.desertservive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Date extends ConstraintLayout {
    TextView dateNumber;
    TextView dateWeather;
    public Date(@NonNull Context context, int number, int weather) {
        //1晴朗 2高温 3沙暴
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.date,Date.this);
        dateNumber = v.findViewById(R.id.dateNumber);
        dateWeather = v.findViewById(R.id.dateWeather);
        dateNumber.setText(""+number);
        dateWeather.setText(weather == 1? "晴朗":(weather == 2? "高温":"沙暴"));
    }

    public void setDateHighLight(boolean isHighLight){
        if(isHighLight){
            dateNumber.setTextColor(getResources().getColor(R.color.colorRed));
            dateWeather.setTextColor(getResources().getColor(R.color.colorRed));
        }else{
            dateNumber.setTextColor(getResources().getColor(R.color.colorBlack));
            dateWeather.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }
}
