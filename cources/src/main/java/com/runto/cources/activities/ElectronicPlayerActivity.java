package com.runto.cources.activities;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runto.cources.R;
import com.runto.cources.databinding.ActivityElectronicPlayerBinding;

@Route(path = "/electronic/playerActivity")
public class ElectronicPlayerActivity extends FragmentActivity {

    ActivityElectronicPlayerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_electronic_player);
        initData();
    }

    public void initData(){
        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.playerTurnSpeed.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
//                PopupWindowShowUpView windowShowUpView = new PopupWindowShowUpView(ElectronicPlayerActivity.this);
//                windowShowUpView.showUp(binding.playerTurnSpeed);
            }
        });

        binding.playerOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binding.playerOrPause.getTag().toString()){
                    case "player":
                        binding.playerOrPause.setTag("pause");
                        binding.playerOrPause.setImageResource(R.drawable.electronic_score_pause);
                        break;
                    case "pause":
                        binding.playerOrPause.setTag("player");
                        binding.playerOrPause.setImageResource(R.drawable.electronic_score_player);
                        break;
                }
            }
        });
    }


}
