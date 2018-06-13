package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.ActivityMainBinding;
import com.runtoinfo.youxiao.databinding.FragmentHomeBinding;
import com.runtoinfo.youxiao.ui.PopuMenu;
import com.runtoinfo.youxiao.ui.PopupWindowFragment;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class HomeFragment extends BaseFragment {

    //public ActivityMainBinding binding;
    public FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        binding.fragmentHomeImagview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<>();
                PopupWindowFragment popuMenu = new PopupWindowFragment(getContext());
                //Drawable drawable = getContext().getResources().getDrawable(R.drawable.fragment_popumenu);
                list.add("北京大学");
                list.add("清华大学");
                list.add("山东大学");
                list.add("复旦大学");
                list.add("吉林大学");
                popuMenu.showPopupWindows(list, Gravity.NO_GRAVITY, 20, 160);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
