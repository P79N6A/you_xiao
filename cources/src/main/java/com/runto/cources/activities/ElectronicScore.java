package com.runto.cources.activities;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.media.DrmInitData;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.runto.cources.R;
import com.runto.cources.databinding.ActivityElectronicScoreBinding;

@Route(path = "/electronic/electronicScore")
public class ElectronicScore extends Activity {

    public ActivityElectronicScoreBinding binding;
    public int clickCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_electronic_score);
        initData();
    }

    public void initData(){
        binding.courseLeaveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.eleBuyCourseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.eleBuyAfterButton.setText("学习曲谱");
                binding.eleBuyAfterButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                binding.electronicBottomLayout.setVisibility(View.GONE);
            }
        });

        binding.eleBuyAfterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.eleBuyAfterButton.getText().equals("学习曲谱")){
                    ARouter.getInstance().build("/electronic/playerActivity").navigation();
                }
            }
        });

        binding.eleCollectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount == 0) {
                    clickCount++;
                    binding.boutiqueEleCourseCollectionImage.setImageResource(R.drawable.boutique_course_collectioned);
                }else{
                    clickCount = 0;
                    binding.boutiqueEleCourseCollectionImage.setImageResource(R.drawable.boutique_course_collection);
                }
            }
        });
    }
}
