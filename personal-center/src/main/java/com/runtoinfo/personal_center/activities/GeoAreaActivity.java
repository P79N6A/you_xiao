package com.runtoinfo.personal_center.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.GeoAreaEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.ActivityGeoAreaBinding;
import com.runtoinfo.youxiao.globalTools.adapter.ListViewAdapter;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/personal/GeoAreaActivity")
public class GeoAreaActivity extends BaseActivity {

    public ActivityGeoAreaBinding binding;
    public Context context;
    public RequestDataEntity requestDataEntity;
    public List<GeoAreaEntity> dataList = new ArrayList<>();
    public ListViewAdapter listViewAdapter;
    public final List<GeoAreaEntity> province = new ArrayList<>();//省
    public final List<GeoAreaEntity> city = new ArrayList<>();//市
    public final List<GeoAreaEntity> district = new ArrayList<>();//区
    public final List<GeoAreaEntity> street = new ArrayList<>();//街道
    public int level = 0;
    public String code;
    public HttpUtils httpUtils;
    public ArrayList<String> areaCode = new ArrayList<>();
    public ArrayList<String> addressName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(GeoAreaActivity.this, R.layout.activity_geo_area);
        setStatusBar(binding.geoAreaTitleLayout);
        context = GeoAreaActivity.this;
        httpUtils = new HttpUtils(context);
        initRequestData();
        requestServer("0");
        initEvent();
    }

    public void initEvent(){
        binding.geoAreaCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoAreaActivity.this.finish();
                province.clear();
                city.clear();
                district.clear();
                street.clear();
                areaCode.clear();
                addressName.clear();
            }
        });

        binding.geoAreaSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == null){
                    switch (level){
                        case 1:
                            DialogMessage.showToast(context, "请选择省份");
                            break;
                        case 2:
                            DialogMessage.showToast(context, "请选择城市");
                            break;
                        case 3:
                            DialogMessage.showToast(context, "请选择区县");
                            break;
                        case 4:
                            DialogMessage.showToast(context, "请选择街道");
                            break;
                    }
                    return;
                }
                Intent intent = new Intent(GeoAreaActivity.this, PersonalMainActivity.class);
                intent.putStringArrayListExtra("code", areaCode);
                intent.putStringArrayListExtra("name", addressName);
                setResult(1002, intent);
                GeoAreaActivity.this.finish();
            }
        });
    }

    public void initRequestData(){
        requestDataEntity = new RequestDataEntity();
        requestDataEntity.setToken(spUtils.getString(Entity.TOKEN));
        requestDataEntity.setUrl(HttpEntity.MAIN_URL + HttpEntity.GET_GEO_AREA);
    }

    public void initListViewData(final List<GeoAreaEntity> list){
        binding.geoAreaListView.setLayoutManager(new LinearLayoutManager(this));
        listViewAdapter = new ListViewAdapter(GeoAreaActivity.this, list, R.layout.activity_geoarea_item);
        binding.geoAreaListView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();

        listViewAdapter.setOnItemClickListener(new UniversalRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GeoAreaEntity geoAreaEntity = listViewAdapter.getDataList().get(position);
                addressName.add(geoAreaEntity.getName());
                code = geoAreaEntity.getCode();
                areaCode.add(code);
                level = geoAreaEntity.getLevel();
                requestServer(code);
            }
        });


    }

    public void requestServer(String code){
        dataList.clear();
        requestDataEntity.setCode(code);
        httpUtils.getGeoArea(handler, requestDataEntity, dataList);
    }

    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (dataList.size() > 0){
                        AnalyticalDataList();
                    }else{
                        DialogMessage.showToast(context, "没有更多数据");
                    }
                    break;
                case 400:
                    //DialogMessage.showToast(context, "数据获取失败");
                    break;
                case 404:
                    //DialogMessage.showToast(context, "请重新登录");
                    break;
                case 500:
                    //DialogMessage.showToast(context, "服务器错误");
                    break;
            }
        }
    };

    public void AnalyticalDataList(){
        GeoAreaEntity geoAreaEntity = dataList.get(0);
        level = geoAreaEntity.getLevel();
        switch (level){
            case 1:
                province.clear();
                province.addAll(dataList);
                initListViewData(province);
                break;
            case 2:
                city.clear();
                city.addAll(dataList);
                initListViewData(city);
                break;
            case 3:
                district.clear();
                district.addAll(dataList);
                initListViewData(district);
                break;
            case 4:
                street.clear();
                street.addAll(dataList);
                initListViewData(street);
                break;
        }
    }

    public void selectData(int level){
        switch (level){
            case 1:
                if (province.size() > 0)
                initListViewData(province);
                break;
            case 2:
                if (city.size() > 0)
                initListViewData(city);
                break;
            case 3:
                if (district.size() > 0)
                initListViewData(district);
                break;
            case 4:
                if (street.size() > 0)
                initListViewData(street);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (level > 1){
            level-=1;
            selectData(level);
        }else
            super.onBackPressed();
    }
}
