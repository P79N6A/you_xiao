package com.runtoinfo.youxiao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.runtoinfo.youxiao.R;
import com.runtoinfo.youxiao.entity.CourseEntity;

import java.util.List;

/**
 * Created by qiaojunchao on 2018/7/4 0004.
 */

public class CoursePunchAdapter extends RecyclerView.Adapter {

    public Context context;
    public List<CourseEntity> list;

    public CoursePunchAdapter(Context context, List<CourseEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_recyclerview_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CourseHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CourseHolder holder = (CourseHolder) viewHolder;
        CourseEntity entity = list.get(position);
        holder.courseName.setText(entity.getCourseName());
        holder.courseTime.setText(entity.getCourseTime());
        holder.imageView.setBackground(entity.getBitmap());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView courseName;//课程名称
        public TextView courseTime;//上课时间
        public TextView courseDetails;//查看详情
        public TextView courseSign;//签到

        public CourseHolder(View itemView) {
            super(itemView);

            imageView =(ImageView) itemView.findViewById(R.id.home_img_course);
            courseName =(TextView) itemView.findViewById(R.id.home_course_name);
            courseTime =(TextView) itemView.findViewById(R.id.home_course_time);
            courseDetails =(TextView) itemView.findViewById(R.id.home_details);
            courseSign =(TextView) itemView.findViewById(R.id.home_course_sign);

            courseDetails.setOnClickListener(this);
            courseSign.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.home_details:
                    Log.e("TAG", "查看详情");
                    ARouter.getInstance().build("/cources/colorfulActivity").navigation();
                    break;
                case R.id.home_course_sign:
                    Log.e("TAG", "签到");
                    courseSign.setText("已签");
                    courseSign.setBackgroundResource(R.drawable.home_sign_finish);
                    break;
            }
        }
    }
}
