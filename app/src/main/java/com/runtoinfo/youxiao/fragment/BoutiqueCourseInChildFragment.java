package com.runtoinfo.youxiao.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.adapter.BoutiqueInChildRecyclerAdapter;
import com.runtoinfo.youxiao.databinding.FragmentBoutiqueCourseInChildBinding;
import com.runtoinfo.youxiao.entity.BoutiqueRecycler;
import com.runtoinfo.youxiao.ui.GridAutofitLayoutManager;
import com.runtoinfo.youxiao.ui.GridSpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26 0026.
 */

public class BoutiqueCourseInChildFragment extends BaseFragment {

    public FragmentBoutiqueCourseInChildBinding binding;
    public BoutiqueInChildRecyclerAdapter adapter;
    public List<BoutiqueRecycler> recyclerList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_boutique_course_in_child, container, false);
        initView();
        return binding.getRoot();
    }

    public void initView(){

        String[] title = new String[]{"钢琴培训从入门到精通","钢琴培训从入门到精通","钢琴培训从入门到精通","钢琴培训从入门到精通"};
        String[] time = new String[]{"7月20日 10:00","7月21日 11:00","8月30日 08:00","9月1日 10:00"};
        String[] price = new String[]{"¥450","¥450","¥450","¥450"};
        String[] num = new String[]{"300人购买","300人购买","300人购买","300人购买"};
        int[] drawable = new int[]{R.drawable.boutique_recycler_1, R.drawable.boutique_recycler_2, R.drawable.boutique_recycler_1, R.drawable.boutique_recycler_2};

        for (int i = 0; i < time.length; i++){
            BoutiqueRecycler recycler = new BoutiqueRecycler();
            recycler.setDrawable(getActivity().getResources().getDrawable(drawable[i]));
            recycler.setTitle(title[i]);
            recycler.setPrice(price[i]);
            recycler.setNumber(num[i]);
            recycler.setTime(time[i]);
            recyclerList.add(recycler);
        }
        binding.boutiqueInChildRecycler.setHasFixedSize(true);
        binding.boutiqueInChildRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        binding.boutiqueInChildRecycler.addItemDecoration(new GridSpacesItemDecoration(30, true));
        adapter = new BoutiqueInChildRecyclerAdapter(getContext(), recyclerList);
        binding.boutiqueInChildRecycler.setAdapter(adapter);

        binding.boutiqueInChildRecycler.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new BoutiqueInChildRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        ARouter.getInstance().build("/course/boutiqueCourseDetails").navigation();
                        break;
                    case 1:
                        ARouter.getInstance().build("/electronic/electronicScore").navigation();
                        break;
                    default:
                        ARouter.getInstance().build("/course/boutiqueCourseDetails").navigation();
                        break;
                }
            }
        });
    }

}
