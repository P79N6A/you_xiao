package com.runtoinfo.youxiao.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.activities.LoginActivity;
import com.runtoinfo.youxiao.activities.MainActivity;
import com.runtoinfo.youxiao.databinding.FragmentPersonalCenterBinding;
import com.runtoinfo.youxiao.ui.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

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
        //View view = View.inflate(getActivity(), R.layout.fragment_personal_center, null);
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
                Log.e("GridView", adapter.getItem(position).toString());
                String json = new Gson().toJson(adapter.getItem(position));
                try {
                    JSONObject object = new JSONObject(json);
                    switch (object.getString("text").replaceAll("\\s*", ""))
                    {
                        case "我的课程":
                            ARouter.getInstance().build("/cources/colorfulActivity").navigation();
                            break;
                        case "上课记录":
                            break;
                        case "请假记录":
                            break;
                        case "关于我们":
                            break;
                        case "意见反馈":
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/personal/personalMain").navigation();
            }
        });
    }
}
