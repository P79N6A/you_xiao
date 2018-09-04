package com.runtoinfo.information.adapter;

import android.content.Context;
import android.os.Handler;

import com.runtoinfo.teacher.CPRCBean.MyCommentEntity;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;

import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/4.
 */

public class MyCommentAdapter extends UniversalRecyclerAdapter<MyCommentEntity> {

    public MyCommentAdapter(Handler handler, Context context, List<MyCommentEntity> mDatas, int mLayoutId) {
        super(handler, context, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, MyCommentEntity myCommentEntity, int position) {

    }
}
