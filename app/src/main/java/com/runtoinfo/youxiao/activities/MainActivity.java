package com.runtoinfo.youxiao.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.FragmentAdapter;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.entity.SelectSchoolEntity;
import com.runtoinfo.youxiao.fragment.FineClassFragment;
import com.runtoinfo.youxiao.fragment.HomeFragment;
import com.runtoinfo.youxiao.fragment.PersonalCenterFragment;
import com.runtoinfo.youxiao.fragment.TopicsFragment;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;

import java.util.ArrayList;
import java.util.List;

@Route( path = "/main/mainActivity")
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener/*, View.OnClickListener*/{

    public ActivityMainBinding binding;

    private ArrayList<TextView> tv_menus;
    private ArrayList<ImageView> imgv_menus;
    private ViewPager mViewPager;

    private ArrayList<Fragment> mFragments;
    private FragmentAdapter mMainMenuAdapter;
    private String TAG = "MainActivity";
    private long firstTime = 0;

    public List<SelectSchoolEntity> schoolSelectList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    // 初始化控件
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        schoolSelectList = new Gson().fromJson(spUtils.getString(Entity.SCHOOL_DATA), new TypeToken<List<SelectSchoolEntity>>(){}.getType());
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
        mFragments.add(new HomeFragment(schoolSelectList));
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
}
