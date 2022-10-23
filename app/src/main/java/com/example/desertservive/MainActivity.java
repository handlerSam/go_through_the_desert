package com.example.desertservive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public LinearLayout map;
    public ArrayList<LinearLayout> weatherListLayout = new ArrayList<>();
    public ArrayList<Date> dateList = new ArrayList<>();
    public TextView day;
    public TextView dayAdd;
    public TextView waterAdd;
    public TextView water;
    public TextView food;
    public TextView foodAdd;
    public TextView money;
    public TextView moneyAdd;
    public Button purchaseWater;
    public Button purchaseFood;
    public Button mining;
    public int state = -1;//-1什么都没选 0选择了某个格子 1选择了挖矿
    public int chooseGrid = -1;//从1开始
    public int date = 1;//从1开始
    public int ownMoney = 10000;
    public int ownWater = 0;
    public int ownFood = 0;
    public int[][] weatherCost = {{5,7},{8,6},{10,10}};
    public int waterPrice = 5;
    public int foodPrice = 10;
    public int miningIncome = 1000;
    public int waterWeight = 3;
    public int foodWeight = 2;
    public int maxWeight = 1200;
    public int bagWeight = 0;
    public boolean isEnd = false;

    public ArrayList<Grid> gridList = new ArrayList<>();
    //1晴朗 2高温 3沙暴
    public ArrayList<Integer> weatherList = new ArrayList<>();

    //1沙子 2村庄 3矿山 4起点 5终点
    public int[] topography = new int[64];

    public int playerPosition;//从1开始

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFind();
        initMap();
        initWeather();
        setResourceChange(0,0,0,0);//初始化
        initButton();
    }

    public void initFind(){
        map = findViewById(R.id.map);
        LinearLayout weatherList1 = findViewById(R.id.weatherList1);
        LinearLayout weatherList2 = findViewById(R.id.weatherList2);
        LinearLayout weatherList3 = findViewById(R.id.weatherList3);
        weatherListLayout.add(weatherList1);
        weatherListLayout.add(weatherList2);
        weatherListLayout.add(weatherList3);
        day = findViewById(R.id.day);
        dayAdd = findViewById(R.id.dayAdd);
        waterAdd = findViewById(R.id.waterAdd);
        water = findViewById(R.id.water);
        food = findViewById(R.id.food);
        foodAdd = findViewById(R.id.foodAdd);
        money = findViewById(R.id.money);
        moneyAdd = findViewById(R.id.moneyAdd);
        purchaseWater = findViewById(R.id.purchaseWater);
        purchaseFood = findViewById(R.id.purchaseFood);
        mining = findViewById(R.id.mining);
    }

    public void initMap(){
        Arrays.fill(topography, 1);
        //map2
        //topography[29] = 3;
        //topography[38] = 2;
        //topography[54] = 3;
        //topography[61] = 2;
        //topography[0] = 4;
        //playerPosition = 1;
        //topography[63] = 5;

        //map1
        topography[0] = 4;
        playerPosition = 1;
        topography[3] = 5;
        topography[45] = 3;
        topography[28] = 2;

        for(int i = 0; i < 8; i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setId(View.generateViewId());
            map.addView(layout);
            if(i % 2 == 1){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)layout.getLayoutParams();
                params.setMarginStart(60);
                layout.setLayoutParams(params);
            }
            for(int j = 0; j < 8; j++){
                Grid grid = new Grid(this,this,topography[8*i+j]);
                grid.setId(View.generateViewId());
                gridList.add(grid);
                switch(topography[8*i+j]){
                    case 1:
                        grid.setAreaName("");
                        grid.setBackground(R.color.colorSand);
                        break;
                    case 2:
                        grid.setAreaName("村庄");
                        grid.setBackground(R.color.colorVillage);
                        break;
                    case 3:
                        grid.setAreaName("矿山");
                        grid.setBackground(R.color.colorMine);
                        break;
                    case 4:
                        grid.setAreaName("起点");
                        grid.setBackground(R.color.colorSand);
                        break;
                    case 5:
                        grid.setAreaName("终点");
                        grid.setBackground(R.color.colorSand);
                        break;
                }
                grid.setAreaNumber(""+(8*i+j+1));
                layout.addView(grid);
            }
        }
        gridList.get(playerPosition-1).setPlayerShow(true);
    }

    public void initWeather(){
        weatherList.add(2);
        weatherList.add(2);
        weatherList.add(1);
        weatherList.add(3);
        weatherList.add(1);
        weatherList.add(2);
        weatherList.add(3);
        weatherList.add(1);
        weatherList.add(2);
        weatherList.add(2);
        weatherList.add(3);
        weatherList.add(2);
        weatherList.add(1);
        weatherList.add(2);
        weatherList.add(2);
        weatherList.add(2);
        weatherList.add(3);
        weatherList.add(3);
        weatherList.add(2);
        weatherList.add(2);
        weatherList.add(1);
        weatherList.add(1);
        weatherList.add(2);
        weatherList.add(1);
        weatherList.add(3);
        weatherList.add(2);
        weatherList.add(1);
        weatherList.add(1);
        weatherList.add(2);
        weatherList.add(2);
        int listMax = weatherList.size()%3 == 0 ? weatherList.size()/3 : weatherList.size()/3+1;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < listMax; j++){
                if(i*listMax+j < weatherList.size()){
                    Date date = new Date(this,i*listMax+j+1,weatherList.get(i*listMax+j));
                    weatherListLayout.get(i).addView(date);
                    dateList.add(date);
                }
            }
        }
    }

    private String getWeatherName(int weather){
        return weather == 1? "晴朗":(weather == 2? "高温":"沙暴");
    }


    public void showResourceChange(int day, int water, int food, int money){
        if(day == 0){
            dayAdd.setVisibility(View.INVISIBLE);
        }else if(day > 0){
            dayAdd.setVisibility(View.VISIBLE);
            dayAdd.setText("+"+day);
            dayAdd.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            dayAdd.setVisibility(View.VISIBLE);
            dayAdd.setText("-"+day);
            dayAdd.setTextColor(getResources().getColor(R.color.colorRed));
        }
        if(water == 0){
            waterAdd.setVisibility(View.INVISIBLE);
        }else if(water > 0){
            waterAdd.setVisibility(View.VISIBLE);
            waterAdd.setText("+"+water);
            waterAdd.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            waterAdd.setVisibility(View.VISIBLE);
            waterAdd.setText("-"+water);
            waterAdd.setTextColor(getResources().getColor(R.color.colorRed));
        }
        if(food == 0){
            foodAdd.setVisibility(View.INVISIBLE);
        }else if(food > 0){
            foodAdd.setVisibility(View.VISIBLE);
            foodAdd.setText("+"+food);
            foodAdd.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            foodAdd.setVisibility(View.VISIBLE);
            foodAdd.setText("-"+food);
            foodAdd.setTextColor(getResources().getColor(R.color.colorRed));
        }
        if(money == 0){
            moneyAdd.setVisibility(View.INVISIBLE);
        }else if(day > 0){
            moneyAdd.setVisibility(View.VISIBLE);
            moneyAdd.setText("+"+money);
            moneyAdd.setTextColor(getResources().getColor(R.color.colorGreen));
        }else{
            moneyAdd.setVisibility(View.VISIBLE);
            moneyAdd.setText("-"+money);
            moneyAdd.setTextColor(getResources().getColor(R.color.colorRed));
        }
    }

    public void setResourceChange(int day, int water, int food, int money) {
        date += day;
        if(date >= 0){
            setDateShow(date);
        }
        ownWater += water;
        ownFood += food;
        ownMoney += money;
        this.day.setText("Day "+date);
        this.water.setText("水 "+ownWater);
        this.food.setText("食物 "+ownFood);
        this.money.setText("资金 "+ownMoney);
        if(ownWater < 0 || ownFood < 0 || date > 30){
            isEnd = true;
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("游戏结束");
            dialog.setMessage(ownWater < 0? "你渴死在了沙漠":(ownFood < 0? "你饿死在了沙漠":"你在沙漠中迷失了太久..."));
            dialog.setCancelable(false);
            dialog.setPositiveButton("重新开始游戏", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restart();
                }});
            dialog.show();
        }
    }

    public void setDateShow(int number){
        if(number <= dateList.size()) dateList.get(number-1).setDateHighLight(true);
        if(number-2>=0)dateList.get(number-2).setDateHighLight(false);
    }

    public void initButton(){
        purchaseWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBagWeight();
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("购买水：价格:"+(date == 1? waterPrice:waterPrice*2)+"元/箱,最多购买"+getMaxPurchase(true)+"箱, 背包"+bagWeight+"/"+maxWeight)
                        .setView(editText)
                        .setPositiveButton("购买", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try{
                                            int purchaseNumber = Integer.parseInt(editText.getText().toString());
                                            purchase(purchaseNumber,true);
                                        }catch (Error e){
                                            Toast.makeText(MainActivity.this, "购买失败", Toast.LENGTH_SHORT).show();
                                        }catch (Exception e){
                                            Toast.makeText(MainActivity.this, "购买失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                builder.create().show();
            }
        });
        purchaseFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshBagWeight();
                final EditText editText = new EditText(MainActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("购买食物：价格:"+(date == 1? foodPrice:foodPrice*2)+"元/箱,最多购买"+getMaxPurchase(false)+"箱, 背包"+bagWeight+"/"+maxWeight)
                        .setView(editText)
                        .setPositiveButton("购买", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try{
                                            int purchaseNumber = Integer.parseInt(editText.getText().toString());
                                            purchase(purchaseNumber,false);
                                        }catch (Error e){
                                            Toast.makeText(MainActivity.this, "购买失败", Toast.LENGTH_SHORT).show();
                                        }catch (Exception e){
                                            Toast.makeText(MainActivity.this, "购买失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                        );
                builder.create().show();
            }
        });
        mining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weather = weatherList.get(date-1);
                if(state == -1){
                    state = 1;
                    showResourceChange(1,-weatherCost[weather-1][0]*3,-weatherCost[weather-1][1]*3,miningIncome);
                }else if(state == 1){
                    showResourceChange(0,0,0,0);
                    state = -1;
                    setResourceChange(1,-weatherCost[weather-1][0]*3,-weatherCost[weather-1][1]*3,miningIncome);
                }
            }
        });
    }

    public void purchase(int number, boolean isWater){
        int temp = getMaxPurchase(isWater);
        if(number <= temp){
            int tempPrice = isWater ? (date == 1? waterPrice:waterPrice*2):(date == 1? foodPrice:foodPrice*2);
            setResourceChange(0,isWater? number:0, isWater? 0:number, -tempPrice*number);
            refreshBagWeight();
        }else{
            Toast.makeText(MainActivity.this, "购买失败", Toast.LENGTH_SHORT).show();
        }
    }

    public int getMaxPurchase(boolean isWater){
        if(isWater){
            int tempWaterPrice = date == 1? waterPrice:waterPrice*2;
            return Math.min(ownMoney/tempWaterPrice, (maxWeight - bagWeight)/waterWeight);
        }else{
            int tempFoodPrice = date == 1? foodPrice:foodPrice*2;
            return Math.min(ownMoney/tempFoodPrice, (maxWeight - bagWeight)/foodWeight);
        }
    }

    public void refreshBagWeight(){
        bagWeight = waterWeight*ownWater + foodWeight*ownFood;
    }

    public void setOutcome(){
        float sum = ownMoney + ownFood*foodPrice*0.5f + ownWater*waterPrice*0.5f;
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("游戏胜利");
        dialog.setMessage("你走出了沙漠。你最后的资金是"+sum+"资源");
        dialog.setCancelable(false);
        dialog.setPositiveButton("重新开始游戏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restart();
            }});
        dialog.show();
    }

    public void restart(){
        state = -1;//-1什么都没选 0选择了某个格子 1选择了挖矿
        chooseGrid = -1;//从1开始
        if(date-1 < dateList.size()){
            dateList.get(date-1).setDateHighLight(false);
        }
        date = 1;//从1开始
        ownMoney = 10000;
        ownWater = 0;
        ownFood = 0;
        bagWeight = 0;
        isEnd = false;
        gridList.get(playerPosition-1).setPlayerShow(false);
        playerPosition = 1;
        gridList.get(playerPosition-1).setPlayerShow(true);
        setResourceChange(0,0,0,0);//初始化
        showResourceChange(0,0,0,0);
        purchaseWater.setVisibility(View.VISIBLE);
        purchaseFood.setVisibility(View.VISIBLE);
        mining.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder;
        switch (item.getItemId()){
            case R.id.checkSetting:
                final ImageView imageView = new ImageView(MainActivity.this);
                imageView.setImageResource(R.drawable.setting);
                builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("查看设定")
                        .setView(imageView);
                builder.create().show();
                break;
            case R.id.checkProgrammer:
                builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("说明")
                        .setMessage("注意：移动和挖矿的时候都需要重复点击才能行动。\n                                       V1.0 制作者 sgy");
                builder.create().show();
                break;
            default:
        }
        return true;
    }
}

