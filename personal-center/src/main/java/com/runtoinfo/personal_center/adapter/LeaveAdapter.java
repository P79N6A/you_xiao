package com.runtoinfo.personal_center.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.runtoinfo.httpUtils.CenterEntity.LeaveRecordEntity;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.adapter.UniversalRecyclerAdapter;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

public class LeaveAdapter extends UniversalRecyclerAdapter<LeaveRecordEntity>{

    public LeaveAdapter(Context mContext, List<LeaveRecordEntity> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, LeaveRecordEntity leaveRecodEntity, int position) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间
        String iso8601 = TimeUtil.iso8601ToDate(leaveRecodEntity.getDate(), 1);
        holder.setText(R.id.record_leave_title, date.equals(iso8601)? "今日请假": iso8601);
        holder.setText(R.id.leave_course_name, "请假课程:" + leaveRecodEntity.getCourseName());
        holder.setText(R.id.leave_type, "请假原因:" + leaveRecodEntity.getReason());
        TextView textView = holder.getView(R.id.leave_status);
        String statues;
        switch (leaveRecodEntity.getStatus()){
            case 1:
                statues = "已提交";
                textView.setTextColor(Color.parseColor("#3aa6fe"));
                break;
            case 2:
                statues = "审核中";
                textView.setTextColor(Color.parseColor("#f08d00"));
                break;
            case 3:
                statues = "审核通过";
                textView.setTextColor(Color.parseColor("#3aa6fe"));
                break;
                default:
                    statues = "";
                    break;
        }
        holder.setText(R.id.leave_status, statues);
    }
}
