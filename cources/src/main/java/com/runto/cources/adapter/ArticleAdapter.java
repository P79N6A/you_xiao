package com.runto.cources.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.runto.cources.R;
import com.runto.cources.bean.Article;
import com.runto.cources.group.GroupRecyclerAdapter;
import com.runto.cources.ui.BaseView;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.adapter.BaseViewHolder;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.IntentDataType;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;
import com.runtoinfo.youxiao.globalTools.views.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.co.dolphin_com.sscore.LoadWarning;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, CourseEntity> {

    setOnClickListeners signInListener;
    setOnClickListeners handHomeWorkListener;
    setOnClickListeners leaveListener;

    private Context context;
    public SPUtils spUtils;
    public Handler handler;
    public HttpUtils httpUtils;
    LinkedHashMap<String, List<CourseEntity>> map = new LinkedHashMap<>();
    List<String> titles = new ArrayList<>();
    List<CourseEntity> dataList = new ArrayList<>();

    public ArticleAdapter(Context context, List<CourseEntity> dataList) {
        super(context);
        this.context = context;
        this.dataList = dataList;
        httpUtils = new HttpUtils(context);
        spUtils = new SPUtils(context);

        map.put("今日课程", dataList);
        if (dataList.size() == 1){
            if (dataList.get(0).getType() != 1){
                titles.add("今日课程共" + dataList.size() + "节");
            }
        }else{
            titles.add("今日课程共" + dataList.size() + "节");
        }
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }



    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holders, final CourseEntity courseEntity, final int position) {
        BaseViewHolder holder = (BaseViewHolder) holders;
        switch (courseEntity.getType()){
            case 1:
                holder.getView(R.id.no_course_layout).setVisibility(View.VISIBLE);
                holder.getView(R.id.course_details_layout).setVisibility(View.GONE);
                holder.setText(R.id.course_no_message, courseEntity.getCourseMessage());
                break;
            case 0:
                holder.setText(R.id.course_name, courseEntity.getCourseName())
                        .setText(R.id.course_teacher_name, courseEntity.getTeacherName())
                        .setText(R.id.course_address, courseEntity.getClassroomName())
                        .setText(R.id.course_time, courseEntity.getStartTime().split(" ")[1].substring(0, 5) + "-" + courseEntity.getEndTime().split(" ")[1].substring(0, 5))
                        .setText(R.id.course_homework, courseEntity.getHomeworkRequirement())
                        .setText(R.id.course_progress_num, String.valueOf(courseEntity.getProgress()) + "%");
                ((ProgressBar)holder.getView(R.id.course_progress)).setProgress(courseEntity.getProgress());

                holder.setText(R.id.course_tv_time, "时间:")
                        .setText(R.id.course_tv_address, "地点:")
                        .setText(R.id.course_tv_homework, "作业:")
                        .setText(R.id.course_tv_teacher, "老师:")
                        .setText(R.id.course_tv_progress, "进度:");

                if (courseEntity.isSignIn()){
                    holder.setText(R.id.course_sign_in_tv, "已签");
                    ((TextView) holder.getView(R.id.course_sign_in_tv)).setTextColor(Color.parseColor("#999999"));
                }else{
                    holder.setText(R.id.course_sign_in_tv, "签到");
                    ((TextView) holder.getView(R.id.course_sign_in_tv)).setTextColor(Color.parseColor("#666666"));
                }

                String now = TimeUtil.getNowDate();
                String time = courseEntity.getDate();
                int result = now.compareTo(time);
                switch (result){
                    case -1:
                        holder.getView(R.id.course_leave_layout).setEnabled(true);
                        holder.getView(R.id.course_sign_in_layout).setEnabled(false);
                        break;
                    case 0:
                        holder.getView(R.id.course_sign_in_layout).setEnabled(true);
                        holder.getView(R.id.course_leave_layout).setEnabled(true);
                        break;
                    case 1:
                        holder.getView(R.id.course_leave_layout).setEnabled(false);
                        holder.getView(R.id.course_sign_in_layout).setEnabled(false);
                        break;
                }

                String homeWork = courseEntity.getHomeworkRequirement();
                if (!TextUtils.isEmpty(homeWork)){
                    holder.getView(R.id.course_hand_homework_layout).setEnabled(true);
                }else{
                    holder.getView(R.id.course_hand_homework_layout).setEnabled(false);
                }

                if (handHomeWorkListener != null) {
                    holder.getView(R.id.course_hand_homework_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handHomeWorkListener.onLayoutClick(v, position, courseEntity);
                        }
                    });
                }

                if (signInListener != null) {
                    holder.getView(R.id.course_sign_in_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signInListener.onLayoutClick(v, position, courseEntity);
                        }
                    });
                }

                if (leaveListener != null) {
                    holder.getView(R.id.course_leave_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            leaveListener.onLayoutClick(v, position, courseEntity);
                        }
                    });
                }
                break;
        }

    }

    public interface setOnClickListeners{
        void onLayoutClick(View view, int position, CourseEntity entity);
    }

    public void setSignInListener(setOnClickListeners listener){
        this.signInListener = listener;
    }

    public void setHandWorkListener(setOnClickListeners clickListener){
        this.handHomeWorkListener = clickListener;
    }

    public void setLeaveListener(setOnClickListeners leaveListener){
        this.leaveListener = leaveListener;
    }

}
