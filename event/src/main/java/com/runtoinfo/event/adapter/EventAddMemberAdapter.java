package com.runtoinfo.event.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.runtoinfo.event.R;
import com.runtoinfo.httpUtils.bean.AddMemberBean;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by QiaoJunChao on 2018/8/21.
 */

public class EventAddMemberAdapter extends UniversalRecyclerAdapter<AddMemberBean> {

    public List<AddMemberBean> dataList;
    public Context context;
    public int layoutId;

    public EventAddMemberAdapter(Context mContext, List<AddMemberBean> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.context = mContext;
        this.layoutId = mLayoutId;
        this.dataList = mDatas;
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, final AddMemberBean bean, final int position) {

        EditText name = holder.getView(R.id.activity_add_name);
        EditText memberType = holder.getView(R.id.activity_add_call);
        EditText phone = holder.getView(R.id.activity_add_phone);

        holder.getView(R.id.activity_delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        if (name.getTag() instanceof TextWatcher) {
            name.removeTextChangedListener((TextWatcher) name.getTag());
        }
        name.setText(bean.getName());

        TextWatcher nameTextWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    bean.setName("");
                } else {
                    bean.setName(s.toString());
                }
            }
        };
        name.addTextChangedListener(nameTextWatch);
        name.setTag(nameTextWatch);

///////////////////////////////////////////////////////////////////////////////////////////////////
        if (memberType.getTag() instanceof TextWatcher) {
            memberType.removeTextChangedListener((TextWatcher) memberType.getTag());
        }
        memberType.setText(bean.getMemberType());

        TextWatcher ageTextWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    bean.setMemberType("");
                } else {
                    bean.setMemberType(s.toString());
                }
            }
        };
        memberType.addTextChangedListener(ageTextWatch);
        memberType.setTag(ageTextWatch);
//////////////////////////////////////////////////////////////////////////////////////////////////
        if (phone.getTag() instanceof TextWatcher) {
            phone.removeTextChangedListener((TextWatcher) phone.getTag());
        }
        phone.setText(bean.getPhoneNumber());

        TextWatcher addressTextWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    bean.setPhoneNumber("");
                } else {
                    bean.setPhoneNumber(s.toString());
                }
            }
        };
        phone.addTextChangedListener(addressTextWatch);
        phone.setTag(addressTextWatch);
    }



    public TextWatcher setNameTextWatch(final AddMemberBean bean){
        TextWatcher nameTextWatch = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                bean.setName(s.toString());
            }
        };
        return nameTextWatch;
    }

    public TextWatcher setMemberTextWatch(final AddMemberBean bean){
        TextWatcher memberWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                bean.setMemberType(s.toString());
            }
        };
        return memberWatcher;
    }

    public TextWatcher setPhoneTextWatch(final AddMemberBean bean){
        TextWatcher phoneNum = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                bean.setPhoneNumber(s.toString());
            }
        };
        return phoneNum;
    }


    public void addAllItem(List<AddMemberBean> list){
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public List<AddMemberBean> gatList(){
        return dataList;
    }

    public interface saveAddBean{
        void saveBean(List<AddMemberBean> list);
    }
}
