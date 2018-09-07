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

    public List<AddMemberBean> dataList = new ArrayList<>();
    public List<AddMemberBean> newList = new ArrayList<>();
    public Context context;
    public int layoutId;
    public List<Map<String, String>> mapList = new ArrayList<>();

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

        if (name.getTag() instanceof TextWatcher){
            name.removeTextChangedListener((TextWatcher) name.getTag());
        }

        if (memberType.getTag() instanceof TextWatcher){
            name.removeTextChangedListener((TextWatcher) memberType.getTag());
        }

        if (phone.getTag() instanceof TextWatcher){
            name.removeTextChangedListener((TextWatcher) phone.getTag());
        }

        holder.getView(R.id.activity_delete_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        if (!TextUtils.isEmpty(bean.getName()) && !TextUtils.isEmpty(bean.getMemberType()) && !TextUtils.isEmpty(bean.getPhoneNumber())){
            holder.setText(R.id.activity_add_name, bean.getName())
                    .setText(R.id.activity_add_call, bean.getMemberType())
                    .setText(R.id.activity_add_phone, bean.getPhoneNumber());
        }else{

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

            name.addTextChangedListener(nameTextWatch);
            name.setTag(nameTextWatch);

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

            memberType.addTextChangedListener(memberWatcher);
            memberType.setTag(memberWatcher);

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

            phone.addTextChangedListener(phoneNum);
            phone.setTag(phoneNum);
        }
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
