package com.runtoinfo.youxiao.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.bean.CourseDataEntity;
import com.runtoinfo.youxiao.entity.CourseTypeEntity;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 是否对用户可见
     */
    protected boolean isVisible;

    public SPUtils spUtils;
    public CourseTypeEntity courseTypeEntity;
    public String jsonResult;
    public List<CourseDataEntity> courseDataList = new ArrayList<>();

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
        //测试代码用，属于假数据
        //jsonResult = DensityUtil.getRawFiles(getActivity(), R.raw.json);
        //getRawFileJson();
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
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();



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
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    public void refresh(Fragment fragment){
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    /**
     * 假数据初始化
     */
    public void getRawFileJson() {
        List<CourseDataEntity> tempList = new ArrayList<>();
        if (jsonResult != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray jsonArray = result.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    CourseDataEntity courseDataEntity = new Gson().fromJson(item.toString(), new TypeToken<CourseDataEntity>() {}.getType());
                    tempList.add(courseDataEntity);
                }
                courseDataList.addAll(tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
