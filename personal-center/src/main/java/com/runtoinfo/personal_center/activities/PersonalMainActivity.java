package com.runtoinfo.personal_center.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citythreelist.CityBean;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalMainBinding;
import com.runtoinfo.personal_center.ui.SelectPictureDialog;
import com.runtoinfo.youxiao.globalTools.timepicker.CustomDatePicker;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Route(path = "/personal/personalMain")
public class PersonalMainActivity extends BaseActivity {

    private ActivityPersonalMainBinding binding;
    //在自己的Application中添加如下代码
    private RefWatcher refWatcher;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(PersonalMainActivity.this, R.layout.activity_personal_main);
        initEvent();
        initDatePicker();
    }

    public void initEvent(){
        //头像
        binding.personalRelativeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarSelect(1);
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
                initSexSelectionDialog();
            }
        });
        //生日
        binding.personalRelativeBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDateSelect(PersonalMainActivity.this, 3, Calendar.getInstance());
                customDatePicker1.show(binding.personalEditBirth.getText().toString());
            }
        });
        //地区
        binding.personalRelativeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/cityView/province").navigation(PersonalMainActivity.this, 1001);
            }
        });

        binding.personalInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void initSexSelectionDialog(){
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.personal_sex_selection);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);

        dialog.findViewById(R.id.personal_sex_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.personalEditSex.setText("男");
            }
        });

        dialog.findViewById(R.id.personal_sex_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.personalEditSex.setText("女");
            }
        });

        dialog.findViewById(R.id.personal_sex_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001 && data!=null)
        {
            switch (resultCode)
            {
                case 1002:
                    CityBean area = data.getParcelableExtra("area");
                    CityBean city = data.getParcelableExtra("city");
                    CityBean province = data.getParcelableExtra("province");
                    binding.personalEditArea.setText(province.getName() + " " + city.getName() + " "+ area.getName());
                    break;
                case 121:
                    String path = data.getStringExtra("path");
                    Log.e("PATH", path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    binding.personalEditTx.setImageBitmap(bitmap);
                    break;
                case RESULT_OK:
                    selectPic(data);
                    break;
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
        // 预先加载一级列表所有城市的数据
        CityListLoader.getInstance().loadCityData(this);
        //预先加载三级列表显示省市区的数据
        CityListLoader.getInstance().loadProData(this);
        refWatcher = LeakCanary.install(getApplication());
    }

    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        PersonalApplication application = (PersonalApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    //时间选择器
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        binding.personalEditBirth.setText(now.split(" ")[0]);
        //currentTime.setText(now);

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                binding.personalEditBirth.setText(time.split(" ")[0]);
            }
        }, "1970-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动

//        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
//            @Override
//            public void handle(String time) { // 回调接口，获得选中的时间
//                currentTime.setText(time);
//            }
//        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//        customDatePicker2.showSpecificTime(true); // 显示时和分
//        customDatePicker2.setIsLoop(true); // 允许循环滚动
    }

    //性别选择
    //头像选择方式
    public void avatarSelect(int type){
        if (type == 1) {
            final SelectPictureDialog dialog = new SelectPictureDialog(this, R.style.dialog, R.layout.course_add_home_work, 1);
            dialog.show();
        }else{
            SelectPictureDialog dialog = new SelectPictureDialog(this, R.style.dialog, R.layout.personal_sex_selection, 2);
            dialog.show();
        }
    }

    private void selectPic(Intent intent) {
        Uri selectImageUri = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        binding.personalEditTx.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }


    //日期选择器 （系统控件）
//    public void showDateSelect(Activity activity, int themeId, Calendar calendar){
//
//        DatePickerDialog dialog = new DatePickerDialog(activity, themeId, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//            }
//        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
//        dialog.setTitle(sdf.format(new Date().getTime()));
//        dialog.show();
//    }

}
