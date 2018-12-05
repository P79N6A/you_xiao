package com.runtoinfo.youxiao.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.bean.VersionEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.BuildConfig;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.FragmentAdapter;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.fragment.FineClassFragment;
import com.runtoinfo.youxiao.fragment.HomeFragment;
import com.runtoinfo.youxiao.fragment.PersonalCenterFragment;
import com.runtoinfo.youxiao.fragment.TopicsFragment;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.views.ViewPager;
import com.runtoinfo.youxiao.service.DownloadManagerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myapplication.MyApplication;

@SuppressWarnings("all")
@Route( path = "/main/mainActivity")
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener /*, View.OnClickListener*/{

    public ActivityMainBinding binding;

    private ArrayList<TextView> tv_menus;
    private ArrayList<ImageView> imgv_menus;
    private ViewPager mViewPager;

    private ArrayList<Fragment> mFragments;
    private FragmentAdapter mMainMenuAdapter;
    private String TAG = "MainActivity";
    private long firstTime = 0;
    private HttpUtils httpUtils;

    public FragmentManager fragmentManager;
    public android.support.v4.app.FragmentTransaction fragmentTransaction;
    public PersonalCenterFragment personalCenterFragment;

    public List<SelectSchoolEntity> schoolSelectList;
    public DownloadManagerService.DownloadBinder mDownloadBinder;

    // 初始化控件
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
            fragmentManager = getSupportFragmentManager();
        ((MyApplication) getApplication()).initPushService(MyApplication.getInstance());
        ((MyApplication) getApplication()).initPushAuxiliaryChannel();
        httpUtils = new HttpUtils(this);
        initBottomMenu();
        checkVersionFromServer();
        try {
            schoolSelectList = new Gson().fromJson(spUtils.getString(Entity.SCHOOL_DATA),
                    new TypeToken<List<SelectSchoolEntity>>() {
                    }.getType());
        }catch (JsonSyntaxException e){
            Log.e("MainActivity", e.toString());
        }
    }

    public void initBottomMenu(){
        tv_menus = new ArrayList<TextView>();
        tv_menus.add(binding.tvBottomMenuChat);
        tv_menus.add(binding.tvBottomMenuAddressbook);
        tv_menus.add(binding.tvBottomMenuDiscovery);
        tv_menus.add(binding.tvBottomMenuMe);
        imgv_menus = new ArrayList<ImageView>();
        imgv_menus.add(binding.imgvBottomMenuChat);
        imgv_menus.add(binding.imgvBottomMenuAddressbook);
        imgv_menus.add(binding.imgvBottomMenuDiscovery);
        imgv_menus.add(binding.imgvBottomMenuMe);
        mViewPager = binding.mainViewPager;
    }

    // 初始化数据
    @Override
    protected void initData() {
        personalCenterFragment = new PersonalCenterFragment();
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.getInstance(schoolSelectList));
        mFragments.add(new FineClassFragment());
        mFragments.add(new TopicsFragment());
        mFragments.add(personalCenterFragment);
        mMainMenuAdapter = new FragmentAdapter(fragmentManager, mFragments);
        setMenuSelector(0); // 默认选中第一个菜单项“微信”
        initEvent();

    }

    // 初始化事件
    private void initEvent() {
        mViewPager.setAdapter(mMainMenuAdapter);
        mViewPager.setOnPageChangeListener(this);
        binding.llBottomMenuChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuSelector(0);
            }
        });
        binding.llBottomMenuAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuSelector(1);
            }
        });
        binding.llBottomMenuDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuSelector(2);
            }
        });
        binding.llBottomMenuMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMenuSelector(3);
            }
        });
    }

    /**
     * 选中指定的菜单项并显示对应的Fragment
     *
     * @param index
     */
    private void setMenuSelector(int index) {
        reSetSelected();
        tv_menus.get(index).setSelected(true);
        imgv_menus.get(index).setSelected(true);
        mViewPager.setCurrentItem(index, false);
    }

    /**
     * 重置底部菜单所有ImageView和TextView为未选中状态
     */
    private void reSetSelected() {
        for (int i = 0; i < tv_menus.size(); i++) {
            tv_menus.get(i).setSelected(false);
            imgv_menus.get(i).setSelected(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setMenuSelector(arg0);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

   public List<SelectSchoolEntity> getSchoolList(){
        return schoolSelectList;
   }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime < 1000) {
            finished();
        } else {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = System.currentTimeMillis();
        }
    }
    //获取版本号提示是否更新
    public void checkVersionFromServer(){
        RequestDataEntity requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.CHECK_VERSION);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("Platform", 1);
        dataMap.put("Sorting", "Version desc");

        httpUtils.checkVersion(handler, requestDataEntity, dataMap);
    }

    public void showUpdate(String details,final String downloadUrl){
        final Dialog dialog = DialogMessage.showDialogWithLayout(MainActivity.this, R.layout.check_version_layout);
        dialog.show();
        dialog.findViewById(R.id.check_version_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadManagerService.class);
                startService(intent);
                bindService(intent, mConnection, BIND_AUTO_CREATE);

                if (Build.VERSION.SDK_INT >= 26) {
                    //来判断应用是否有权限安装apk
                    boolean installAllowed = getPackageManager().canRequestPackageInstalls();
                    //有权限
                    if (installAllowed) {
                        //安装apk
                        if (mDownloadBinder != null) {
                            mDownloadBinder.startDownload(downloadUrl);
                        }
                    } else {
                        //无权限 申请权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 200);
                    }
                } else {
                    if (mDownloadBinder != null) {
                        mDownloadBinder.startDownload(downloadUrl);
                    }
                }
            }
        });
        ((TextView) dialog.findViewById(R.id.check_version_details)).setText(details);
    }

    /**
     * 设置状态栏半透明
     */
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,80, null);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    VersionEntity version = (VersionEntity) msg.obj;
                    String [] versions = version.getVersion().split(".");
                    StringBuilder strVersion = new StringBuilder("");
                    for (String s : versions){
                        strVersion.append(s);
                    }
                    String[] locationVersion = BuildConfig.VERSION_NAME.split(".");
                    StringBuilder locVersion = new StringBuilder("");
                    for (String l : locationVersion){
                        locVersion.append(l);
                    }
                    if (Integer.parseInt(strVersion.toString()) > Integer.parseInt(locVersion.toString())){
                        showUpdate(version.getDescription(), version.getiOSUpgradePath());
                    }
                    break;
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadBinder = (DownloadManagerService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadBinder = null;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ResultCode", "RequesetCode:" + String.valueOf(requestCode) + "; ResultCode:" + String.valueOf(resultCode));
        PersonalCenterFragment fragment = (PersonalCenterFragment) ARouter.getInstance().build("/fragment/personalFragment").navigation();
        if (fragment == null) return;
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
