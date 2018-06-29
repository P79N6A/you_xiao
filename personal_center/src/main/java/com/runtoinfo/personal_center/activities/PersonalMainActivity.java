package com.runtoinfo.personal_center.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citythreelist.CityBean;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalMainBinding;
import com.squareup.leakcanary.RefWatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Route(path = "/personal/personalMain")
public class PersonalMainActivity extends Activity {

    private ActivityPersonalMainBinding binding;
    //在自己的Application中添加如下代码
    private RefWatcher refWatcher;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_personal_main);
        binding = DataBindingUtil.setContentView(PersonalMainActivity.this, R.layout.activity_personal_main);
        initEvent();
    }
    public void initEvent(){
        //头像
        binding.personalRelativeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSelect();
            }
        });
        //用户名
        binding.personalRelativeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //性别
        binding.personalRelativeSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //生日
        binding.personalRelativeBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateSelect(PersonalMainActivity.this, 3, Calendar.getInstance());
                //showDateSelect();
            }
        });
        //地区
        binding.personalRelativeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cityView/province").navigation(PersonalMainActivity.this, 1001);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && data!=null)
        {
            if (resultCode == RESULT_OK)
            {
                CityBean area = data.getParcelableExtra("area");
                CityBean city = data.getParcelableExtra("city");
                CityBean province = data.getParcelableExtra("province");

                binding.personalEditArea.setText(province.getName() + " " + city.getName() + " "+ area.getName());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initARouter();
        initCity();
    }

    public void initARouter(){
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init( this.getApplication() ); // 尽可能早，推荐在Application中初始化
    }

    public void initCity(){
        /**
         * 预先加载一级列表所有城市的数据
         */
        CityListLoader.getInstance().loadCityData(this);
        /**
         * 预先加载三级列表显示省市区的数据
         */
        CityListLoader.getInstance().loadProData(this);
        //refWatcher = LeakCanary.install(getApplication());
    }

    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        PersonalApplication application = (PersonalApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    //日期选择器

    public void showDateSelect(Activity activity, int themeId, Calendar calendar){

        DatePickerDialog dialog = new DatePickerDialog(activity, themeId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        dialog.setTitle(sdf.format(new Date().getTime()));
        dialog.show();
    }

    //性别选择
    //头像选择方式
    public void avatarSelect(){
        builder = new AlertDialog.Builder(PersonalMainActivity.this);
        View view = View.inflate(PersonalMainActivity.this, R.layout.personal_avatar_layout, null);
        builder.setView(view);
        view.findViewById(R.id.personal_from_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "选择照片");
            }
        });
        TextView cancel = view.findViewById(R.id.personal_cancle);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.personal_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/camera/activity").navigation();
            }
        });
        builder.show();
    }

}
