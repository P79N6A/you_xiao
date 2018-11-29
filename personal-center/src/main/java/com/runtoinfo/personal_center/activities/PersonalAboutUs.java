package com.runtoinfo.personal_center.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalAboutUsBinding;

@Route(path = "/personal/aboutUs")
public class PersonalAboutUs extends BaseActivity {

    public ActivityPersonalAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_about_us);
        setStatusBar(binding.aboutUsTitleLayout);
        binding.activityImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            ((TextView)findViewById(R.id.about_us_version)).setText("版本号：" + versionName);
            Log.e("AboutUs", pkName + "   " + versionName + "  " + versionCode);
        } catch (Exception e) {
        }
    }
}
