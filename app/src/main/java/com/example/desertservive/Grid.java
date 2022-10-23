package com.example.desertservive;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Grid extends ConstraintLayout {
    public ImageView background;
    public TextView areaNumber;
    public TextView areaName;
    public ImageView player;
    public View v;
    public MainActivity m;
    public int number;//从1开始
    public int topography;
    public Grid(@NonNull Context context, MainActivity m, int topography) {
        super(context);
        View v = LayoutInflater.from(context).inflate(R.layout.grid,Grid.this);
        background = v.findViewById(R.id.background);
        areaNumber = v.findViewById(R.id.areaNumber);
        areaName = v.findViewById(R.id.areaName);
        player = v.findViewById(R.id.player);
        this.topography = topography;
        this.m = m;
        setClickEvent();
    }

    public void setBackground(int colorResource){
        background.setBackgroundResource(colorResource);
    }

    public void setAreaName(String name){
        areaName.setText(name);
    }

    public void setAreaNumber(String number){
        this.number = Integer.parseInt(number);
        areaNumber.setText(number);
    }

    public void setClickEvent(){
        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int weather = m.weatherList.get(m.date-1);
                if(m.state == -1){
                    if(weather == 1 || weather == 2){
                        if(canReach(m.playerPosition-1, number-1)){
                            m.chooseGrid = number;
                            setBackground(getTopographyColor(topography,true));
                            m.state = 0;
                            int temp = m.playerPosition == m.chooseGrid? 1:2;
                            m.showResourceChange(1,-m.weatherCost[weather-1][0]*temp,-m.weatherCost[weather-1][1]*temp,0);
                        }
                    }else{
                        if(m.playerPosition == number){
                            m.chooseGrid = number;
                            setBackground(getTopographyColor(topography,true));
                            m.state = 0;
                            m.showResourceChange(1,-m.weatherCost[weather-1][0],-m.weatherCost[weather-1][1],0);
                        }
                    }
                }else if(m.state == 0){
                    if(m.chooseGrid != number){
                        m.gridList.get(m.chooseGrid-1).setBackground(getTopographyColor(m.gridList.get(m.chooseGrid-1).topography,false));
                        m.chooseGrid = -1;
                        m.state = -1;
                        m.showResourceChange(0,0,0,0);
                    }else{
                        //确认移动
                        int temp = m.playerPosition == m.chooseGrid? 1:2;
                        m.gridList.get(m.chooseGrid-1).setBackground(getTopographyColor(m.gridList.get(m.chooseGrid-1).topography,false));
                        m.gridList.get(m.playerPosition-1).setPlayerShow(false);
                        m.playerPosition = m.chooseGrid;
                        m.gridList.get(m.playerPosition-1).setPlayerShow(true);
                        m.chooseGrid = -1;
                        m.showResourceChange(0,0,0,0);
                        m.state = -1;
                        m.setResourceChange(1,-m.weatherCost[weather-1][0]*temp,-m.weatherCost[weather-1][1]*temp,0);
                        if(m.topography[m.playerPosition-1] == 2){
                            m.purchaseFood.setVisibility(VISIBLE);
                            m.purchaseWater.setVisibility(VISIBLE);
                        }else{
                            m.purchaseFood.setVisibility(INVISIBLE);
                            m.purchaseWater.setVisibility(INVISIBLE);
                        }
                        if(m.topography[m.playerPosition-1]==3){
                            m.mining.setVisibility(VISIBLE);
                        }else{
                            m.mining.setVisibility(INVISIBLE);
                        }
                        if(m.topography[m.playerPosition-1] == 5 && !m.isEnd){
                            m.setOutcome();
                        }
                    }
                }else if(m.state == 1){
                    m.state = -1;
                    m.showResourceChange(0,0,0,0);
                }
            }
        });
    }

    public int getTopographyColor(int topography, boolean isChosen) {
        switch(topography){
            case 1: case 4: case 5:
                return isChosen? R.color.colorSandChoose:R.color.colorSand;
            case 2:
                return isChosen? R.color.colorVillageChoose:R.color.colorVillage;
            case 3:
                return isChosen? R.color.colorMineChoose:R.color.colorMine;
            default:
                return -1;
        }
    }

    public boolean canReach(int fromNumber, int toNumber){
        int temp = toNumber - fromNumber;
        int layer = toNumber/8 - fromNumber/8;
        if(fromNumber == toNumber)return true;
        if((fromNumber/8)%2 == 0){
            if((temp == -9 && layer == -1)||(temp == -8 && layer == -1)||(temp == -1 && layer == 0)||(temp == 1 && layer == 0)||(temp == 7 && layer == 1)||(temp == 8 && layer == 1)){
                return true;
            }else{
                return false;
            }
        }else{
            if((temp == -8 && layer == -1)||(temp == -7 && layer == -1)||(temp == -1 && layer == 0)||(temp == 1 && layer == 0)||(temp == 8 && layer == 1)||(temp == 9 && layer == 1)){
                return true;
            }else{
                return false;
            }
        }
    }

    public void setPlayerShow(boolean isShow){
        player.setVisibility(isShow ? VISIBLE:GONE);
    }
}
