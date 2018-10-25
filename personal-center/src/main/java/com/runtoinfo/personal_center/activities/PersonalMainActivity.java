package com.runtoinfo.personal_center.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citythreelist.CityBean;
import com.runtoinfo.httpUtils.CenterEntity.PersonalInformationEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.PersonalCenterEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityPersonalMainBinding;
import com.runtoinfo.personal_center.ui.SelectPictureDialog;
import com.runtoinfo.youxiao.globalTools.timepicker.CustomDatePicker;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Route(path = "/personal/personalMain")
public class PersonalMainActivity extends BaseActivity {

    private ActivityPersonalMainBinding binding;
    //在自己的Application中添加如下代码
    private RefWatcher refWatcher;
    private CustomDatePicker customDatePicker1, customDatePicker2;
    public HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(PersonalMainActivity.this, R.layout.activity_personal_main);
        setStatusBar(R.color.dialog_button_text_color);
        httpUtils = new HttpUtils(this);
        checkPermission();
        initData();
        initEvent();
        initDatePicker();
    }

    public void initData() {
        Glide.with(this).load(HttpEntity.IMAGE_HEAD + spUtils.getString(Entity.AVATAR)).into(binding.personalEditTx);
        binding.personalUserName.setText(spUtils.getString(Entity.NAME));
        binding.personalEditSex.setText(spUtils.getInt(Entity.GENDER) == 0 ? "男" : "女");
        binding.personalEditBirth.setText(TimeUtil.iso8601ToDate(spUtils.getString(Entity.BIRTHDAY), 1));
        binding.personalEditArea.setText(spUtils.getString(Entity.ADDRESS));
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
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
                hideView(true);
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
                customDatePicker1.show(binding.personalEditBirth.getText().toString());
            }
        });
        //地区
        binding.personalRelativeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/GeoAreaActivity").navigation(PersonalMainActivity.this, 1001);
            }
        });

        binding.personalInformationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.userNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView(false);
            }
        });

        binding.userNameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.userNameNew.getText().toString())){
                    hideView(false);
                    PersonalInformationEntity entity = new PersonalInformationEntity();
                    entity.setName(binding.userNameNew.getText().toString());
                    requestOwnServer(entity);
                }
            }
        });

        binding.userNameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.userNameNew.setText("");
            }
        });
    }

    public void hideView(boolean flag){
        binding.personalTitleLayout.setVisibility(flag?View.GONE:View.VISIBLE);
        binding.personalSettingLayout.setVisibility(flag?View.GONE:View.VISIBLE);
        binding.updateUserNameLayout.setVisibility(flag?View.VISIBLE:View.GONE);
        setStatusBar(binding.userNameTitleLayout);
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
                PersonalInformationEntity entity = new  PersonalInformationEntity();
                entity.setGender(0);
                requestOwnServer(entity);
            }
        });

        dialog.findViewById(R.id.personal_sex_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                binding.personalEditSex.setText("女");
                PersonalInformationEntity entity = new  PersonalInformationEntity();
                entity.setGender(1);
                requestOwnServer(entity);
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
                    ArrayList<String> name = data.getStringArrayListExtra("name");
                    ArrayList<String> areaCode = data.getStringArrayListExtra("code");
                    StringBuilder result = new StringBuilder("");
                    for (int i = 0; i < name.size(); i++){
                        result.append(name.get(i));
                    }
                    binding.personalEditArea.setText(result.toString());
                    PersonalInformationEntity entity = new PersonalInformationEntity();
                    switch (areaCode.size()){
                        case 1:
                            entity.setProvince(areaCode.get(0));
                            break;
                        case 2:
                            entity.setCity(areaCode.get(1));
                            entity.setProvince(areaCode.get(0));
                            break;
                        case 3:
                            entity.setCity(areaCode.get(1));
                            entity.setProvince(areaCode.get(0));
                            entity.setDistrict(areaCode.get(2));
                            break;
                        case 4:
                            entity.setCity(areaCode.get(1));
                            entity.setProvince(areaCode.get(0));
                            entity.setDistrict(areaCode.get(2));
                            entity.setStreet(areaCode.get(3));
                            break;
                    }
                    requestOwnServer(entity);
                    break;
                case 121:
                    String path = data.getStringExtra("path");
                    Log.e("PATH", path);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    binding.personalEditTx.setImageBitmap(bitmap);
                    requestFile();
                    break;
                case RESULT_OK:
                    selectPic(data);
                    break;
            }
        }
    }

    /**
     * 给阿里服务传递文件 获取文件路径
     */
    public void requestFile(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.POST_ALI_ONE_FILE);
        httpUtils.postOneFile(handler, entity);
    }

    /**
     * 给自己服务器上传返回的文件路径
     */
    public void requestOwnServer(PersonalInformationEntity entity){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.UPDATE_USER_INFORMATION);
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUserId(spUtils.getInt(Entity.USER_ID));
        httpUtils.updateUserInfor(handler, requestDataEntity, entity);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String path = msg.obj.toString();
                    PersonalInformationEntity entity = new PersonalInformationEntity();
                    entity.setAvatar(path);
                    requestOwnServer(entity);
                    break;
                case 1:
                    DialogMessage.showToast(PersonalMainActivity.this, "修改成功");
                    break;
                case 400:
                    DialogMessage.showToast(PersonalMainActivity.this, "修改失败");
                    break;
                case 500:
                    DialogMessage.showToast(PersonalMainActivity.this, "请求失败");
                    break;
            }
        }
    };

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
                PersonalInformationEntity entity = new PersonalInformationEntity();
                entity.setBirthDay(time.split(" ")[0]);
                requestOwnServer(entity);
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

    @Override
    public void onBackPressed() {
        if (binding.updateUserNameLayout.getVisibility() == View.VISIBLE){
            hideView(false);
        }else super.onBackPressed();
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
