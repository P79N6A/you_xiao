package com.runtoinfo.personal_center.adapter;

import android.content.Context;
import android.graphics.Color;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.runtoinfo.httpUtils.CenterEntity.LeaveGroupEntity;
import com.runtoinfo.httpUtils.CenterEntity.LeaveRecordEntity;
import com.runtoinfo.personal_center.R;
import com.runtoinfo.personal_center.databinding.LeaveRecordChildLayoutBinding;
import com.runtoinfo.personal_center.databinding.LeaveRecordHeardLayoutBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/5.
 */

@SuppressWarnings("all")
public class LeaveAdapter extends GroupedRecyclerViewAdapter{

    public List<LeaveGroupEntity> groupEntityList;
    public List<LeaveRecordEntity> childRecordList;
    public LeaveAdapter(Context mContext, List<LeaveGroupEntity> mDatas, int mLayoutId) {
        super(mContext, true);
        this.groupEntityList = mDatas;
    }

    //@Override
//    protected void convert(Context mContext, BaseViewHolder holder, LeaveRecordEntity leaveRecodEntity, int position) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        String date = df.format(new Date());// new Date()为获取当前系统时间
//        //String iso8601 = TimeUtil.iso8601ToDate(leaveRecodEntity.getDate(), 1);
//        holder.setText(R.id.record_leave_title, date.compareTo(leaveRecodEntity.getDate()) == 0? "今日请假": leaveRecodEntity.getDate());
//        holder.setText(R.id.leave_course_name, "请假课程:" + leaveRecodEntity.getClassName());
//        holder.setText(R.id.leave_type, "请假原因:" + leaveRecodEntity.getReason());
//        TextView textView = holder.getView(R.id.leave_status);
//        textView.setTextColor(Color.parseColor("#3aa6fe"));
//        String statues;
//        switch (leaveRecodEntity.getStatus()){
//            case 1:
//                statues = "已提交";
//                //textView.setTextColor(Color.parseColor("#3aa6fe"));
//                break;
//            case 2:
//                statues = "审核通过";
//                //textView.setTextColor(Color.parseColor("#3aa6fe"));
//                break;
//            case 3:
//                statues = "审核拒绝";
//                //textView.setTextColor(Color.parseColor("#3aa6fe"));
//                break;
//                default:
//                    statues = "";
//                    break;
//        }
//        holder.setText(R.id.leave_status, statues);
//    }

    @Override
    public int getGroupCount() {
        return groupEntityList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupEntityList.get(groupPosition).getItems().size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.leave_record_heard_layout;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.leave_record_child_layout;
    }

    @Override
    public void onBindHeaderViewHolder(com.donkingliang.groupedadapter.holder.BaseViewHolder holder, int groupPosition) {
        LeaveRecordHeardLayoutBinding binding = (LeaveRecordHeardLayoutBinding) holder.getBinding();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间
        LeaveGroupEntity child = groupEntityList.get(groupPosition);
        binding.recordLeaveTitle.setText(date.compareTo(child.getKey()) == 0? "今日请假": child.getKey());
    }

    @Override
    public void onBindFooterViewHolder(com.donkingliang.groupedadapter.holder.BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(com.donkingliang.groupedadapter.holder.BaseViewHolder holder, int groupPosition, int childPosition) {
        LeaveRecordChildLayoutBinding binding = (LeaveRecordChildLayoutBinding) holder.getBinding();
        List<LeaveRecordEntity> child = groupEntityList.get(groupPosition).getItems();
        LeaveRecordEntity leaveRecordEntity = child.get(childPosition);
        binding.leaveCourseName.setText("请假课程：" + leaveRecordEntity.getClassName());
        binding.leaveType.setText("请假原因：" + leaveRecordEntity.getReason());
        binding.leaveStatus.setTextColor(Color.parseColor("#3aa6fe"));
        String statues;
        switch (leaveRecordEntity.getStatus()){
            case 1:
                statues = "已提交";
                //textView.setTextColor(Color.parseColor("#3aa6fe"));
                break;
            case 2:
                statues = "审核通过";
                //textView.setTextColor(Color.parseColor("#3aa6fe"));
                break;
            case 3:
                statues = "审核拒绝";
                //textView.setTextColor(Color.parseColor("#3aa6fe"));
                break;
            default:
                statues = "";
                break;
        }
        binding.leaveStatus.setText(statues);
    }
}
