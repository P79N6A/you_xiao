package com.runtoinfo.personal_center.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runtoinfo.youxiao.globalTools.utils.SPUtils;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class BasePersonalFragment extends Fragment {

    public SPUtils spUtils;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spUtils = new SPUtils(getContext());
    }
}
