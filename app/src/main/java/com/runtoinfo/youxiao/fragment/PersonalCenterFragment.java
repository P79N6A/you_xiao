package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.databinding.FragmentPersonalCenterBinding;
import com.runtoinfo.youxiao.ui.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class PersonalCenterFragment extends BaseFragment {

    public FragmentPersonalCenterBinding binding;
    public SimpleAdapter adapter;
    public Map<String, Object> map = new HashMap<>();
    public List<Map<String, Object>> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_personal_center, null);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_center, container, false);
        initData();
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initData(){
        dataList.clear();
        String[] from = {"img", "text"};
        int[] to = {R.id.grid_img_view, R.id.grid_text};
        int[] img = {R.drawable.personal_grid_course, R.drawable.personal_grid_class, R.drawable.personal_grid_leave, R.drawable.personal_grid_ours, R.drawable.personal_grid_feedback};
        String[] text = {"我的课程","上课记录","请假记录","关于我们","意见反馈"};
        for (int i = 0; i < img.length; i++)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("img", img[i]);
            map.put("text", text[i]);
            dataList.add(map);
        }
        adapter = new SimpleAdapter(getContext(), dataList, R.layout.fragment_personal_gridview_item, from, to);
        //myGridView = new MyGridView(getActivity());
        binding.personalGridview.setAdapter(adapter);

        binding.personalGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        binding.personalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
