package com.runtoinfo.youxiao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;
import com.runtoinfo.youxiao.entity.CourseTypeEntity;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public class BaseFragment extends Fragment {

//    /**
//     * 是否对用户可见
//     */
//    protected boolean mIsVisible;
//    /**
//     * 是否加载完成
//     * 当执行完onViewCreated方法后即为true
//     */
//    protected boolean mIsPrepare;
//
//    /**
//     * 是否加载完成
//     * 当执行完onViewCreated方法后即为true
//     */
//    protected boolean mIsImmersion;
//
//    protected ImmersionBar mImmersionBar;

    public SPUtils spUtils;
    public CourseTypeEntity courseTypeEntity;

    @Override
    public void onResume() {
        super.onResume();
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().pageAppear(getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SPUtils(getContext());
        courseTypeEntity = new CourseTypeEntity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MANService manService = MANServiceProvider.getService();
        manService.getMANPageHitHelper().pageDisAppear(getActivity());
//        if (mImmersionBar != null)
//            mImmersionBar.destroy();
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        if (isLazyLoad()) {
//            mIsPrepare = true;
//            mIsImmersion = true;
//            onLazyLoad();
//        } else {
//            initData();
//            if (isImmersionBarEnabled())
//                initImmersionBar();
//        }
//        initView();
//        setListener();
//    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
//        if (mIsVisible && mIsPrepare) {
//            mIsPrepare = false;
//            initData();
//        }
//        if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
//            initImmersionBar();
//        }
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    /*@SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }*/
}
