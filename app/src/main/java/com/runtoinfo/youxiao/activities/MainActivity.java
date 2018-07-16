package com.runtoinfo.youxiao.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.FragmentAdapter;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.fragment.FineClassFragment;
import com.runtoinfo.youxiao.fragment.HomeFragment;
import com.runtoinfo.youxiao.fragment.PersonalCenterFragment;
import com.runtoinfo.youxiao.fragment.TopicsFragment;

import java.util.ArrayList;

@Route( path = "/main/mainAcitivity")
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener/*, View.OnClickListener*/{

    public ActivityMainBinding binding;

    private ArrayList<TextView> tv_menus;
    private ArrayList<ImageView> imgv_menus;
    private ViewPager mViewPager;

    private ArrayList<Fragment> mFragments;
    private FragmentAdapter mMainMenuAdapter;
    private String TAG = "MainActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    // 初始化控件
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
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

        //initEvent();
    }

    // 初始化数据
    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new FineClassFragment());
        mFragments.add(new TopicsFragment());
        mFragments.add(new PersonalCenterFragment());
        mMainMenuAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        setMenuSelector(0); // 默认选中第一个菜单项“微信”
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

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_bottomMenu_chat:
//                setMenuSelector(0);
//                break;
//            case R.id.ll_bottomMenu_addressBook:
//                setMenuSelector(1);
//                break;
//            case R.id.ll_bottomMenu_discovery:
//                setMenuSelector(2);
//                break;
//            case R.id.ll_bottomMenu_me:
//                setMenuSelector(3);
//                break;
//
//        }
//    }

    /**
     * 选中指定的菜单项并显示对应的Fragment
     *
     * @param index
     */
    private void setMenuSelector(int index) {
        reSetSelected();
        tv_menus.get(index).setSelected(true);
        imgv_menus.get(index).setSelected(true);
        mViewPager.setCurrentItem(index);
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

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
