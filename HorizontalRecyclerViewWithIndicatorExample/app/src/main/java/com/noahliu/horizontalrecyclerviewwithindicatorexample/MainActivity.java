package com.noahliu.horizontalrecyclerviewwithindicatorexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerViewIndicator indicator = findViewById(R.id.indicator);
        RecyclerView recyclerView = findViewById(R.id.recyclerView_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<TanksData> data = new ArrayList<>();
        data.add(new TanksData("Mark I坦克",R.drawable.mark,"這是第一種被正式命名為“坦克”的裝甲戰鬥車輛，於1916年首次出現在戰場上。"));
        data.add(new TanksData("Churchill坦克",R.drawable.churchill,"在第二次世界大戰期間，Churchill坦克是英國陸軍的主要戰車之一，擁有強大的裝甲和火力，是一款可靠的坦克。"));
        data.add(new TanksData("Cromwell坦克",R.drawable.cromwell,"在第二次世界大戰期間，Cromwell坦克是英國陸軍的主要戰車之一，擁有高速度和卓越的機動性。"));
        data.add(new TanksData("Centurion坦克",R.drawable.centurion,"在冷戰期間，Centurion坦克是英國陸軍主力坦克之一，經過多次改進後，成為了一款強大的主戰坦克。"));
        data.add(new TanksData("Challenger 2坦克",R.drawable.challenger,"是當代英國陸軍主力坦克之一，具有強大的裝甲和火力，並且具有良好的防禦能力。"));
        data.add(new TanksData("Warrior步兵戰車",R.drawable.warrior,"是一款由英國製造的輪式裝甲車，是英國陸軍中的主要裝甲車之一。"));
        MyAdapter adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setIndicator(indicator,recyclerView);

    }
}