package com.runto.cources.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.runto.cources.R;
import com.runto.cources.activities.BoutiqueCourseDetails;
import com.runto.cources.adapter.ListViewAdapter;
import com.runto.cources.databinding.FragmentCourseListBinding;
import com.runtoinfo.httpUtils.bean.ChildContent;
import com.runtoinfo.httpUtils.bean.CourseChildData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

@SuppressLint("ValidFragment")
public class CourseListFragment extends Fragment {

    public FragmentCourseListBinding binding;
    public ListViewAdapter simpleAdapter;
    public List<String> dataList = new ArrayList<>();
    public CourseChildData msg;

    public CourseListFragment(CourseChildData msg){
        this.msg = msg;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course_list, container, false);
        initData();
        return binding.getRoot();
    }

    public void initData(){
        List<ChildContent> childContents = msg.getItems();
        for (int i = 0; i < childContents.size(); i++){
            ChildContent content = childContents.get(i);
            dataList.add(content.getName());
        }
        simpleAdapter = new ListViewAdapter(getContext(), dataList);
        binding.boutiqueCourseListListView.setAdapter(simpleAdapter);
        binding.boutiqueCourseListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ARouter.getInstance().build("/electronic/electronicScore").navigation();
                BoutiqueCourseDetails details = (BoutiqueCourseDetails) getActivity();
                details.videoView("http://pic.ibaotu.com/00/20/08/96e888piCHck.mp4");
            }
        });
    }
}
