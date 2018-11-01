package com.runtoinfo.personal_center.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.runtoinfo.httpUtils.CenterEntity.CollectionEntity;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.adapter.CollectionAdapter;
import com.runtoinfo.personal_center.databinding.CollectionDataLayoutBinding;
import com.runtoinfo.youxiao.globalTools.adapter.CommonViewPagerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by QiaoJunChao on 2018/9/25.
 */

@SuppressWarnings("all")
@SuppressLint("ValidFragment")
public class CourseFragment extends BasePersonalFragment{

    public int type;
    public CollectionDataLayoutBinding binding;
    public List<CollectionEntity> list = new ArrayList<>();
    public CollectionAdapter collectionAdapter;
    public HttpUtils httpUtils;
    public CommonViewPagerAdapter commonViewPagerAdapter;


    public CourseFragment(int type){
        this.type = type;
    }

    public void setAdapter(CommonViewPagerAdapter adapter){
        this.commonViewPagerAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.collection_data_layout, container, false);
        httpUtils = new HttpUtils(getContext());
        requestServer();
        return binding.getRoot();
    }

    public void requestServer(){
        RequestDataEntity entity = new RequestDataEntity();
        entity.setUserId(spUtils.getInt(Entity.USER_ID));
        entity.setToken(spUtils.getString(Entity.TOKEN));
        entity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_COLLECTION_ME);
        entity.setType(type);
        httpUtils.getMyCollection(handler, entity);
    }
    public void initRecyclerData(){
        collectionAdapter = new CollectionAdapter(getActivity(), list, R.layout.personal_collection_item_layout, type);
        binding.collectionDataRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.collectionDataRecycler.setHasFixedSize(true);
        binding.collectionDataRecycler.setNestedScrollingEnabled(false);
//        binding.collectionDataRecycler.setItemLayout(R.id.item_layout);
//        binding.collectionDataRecycler.setItem_delete(R.id.delete_collection);
//        binding.collectionDataRecycler.setAdapter(collectionAdapter);
//        binding.collectionDataRecycler.setmListener(new UniversalRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                collectionAdapter.removeItem(position);
//            }
//        });
        binding.collectionDataRecycler.setAdapter(collectionAdapter);
        collectionAdapter.setOnDeleteClickListener(new CollectionAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                collectionAdapter.removeItem(position);
                binding.collectionDataRecycler.closeMenu();
            }
        });

    }

    public void setTitleNumber(int number){
        if (commonViewPagerAdapter != null){
            for (int i = 0; i < commonViewPagerAdapter.getCount(); i++){
                if (commonViewPagerAdapter.getItem(i) == CourseFragment.this){
                    String title = commonViewPagerAdapter.getPageTitle(i).toString();
                    // 在TabLayout的标题上添加（）
                    int posBracket = title.indexOf("(");
                    if(posBracket > 0)
                        title = title.substring(0, posBracket);
                    commonViewPagerAdapter.setPageTitle(i, String.format(Locale.CHINESE, "%s(%s)", title, number));
                }
            }
        }
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    try {
                        JSONArray items = (JSONArray) msg.obj;
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            CollectionEntity collectionEntity = new Gson().fromJson(item.toString(),
                                    new TypeToken<CollectionEntity>() {
                                    }.getType());
                            list.add(collectionEntity);
                        }
                        setTitleNumber(items.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (list.size() > 0) {
                        binding.collectionNothingLayout.setVisibility(View.GONE);
                        initRecyclerData();
                    }else{
                        binding.collectionNothingLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case 500:
                    break;
            }
        }
    };
}
