package com.runto.cources.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;
    private Context context;

    public ArticleAdapter(Context context, String courseNum) {
        super(context);
        this.context = context;
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("当日课程", create(0));
        //map.put("每周热点", create(1));
        //map.put("最高评论", create(2));
        titles.add("当日课程共" + courseNum + "节");
        //titles.add("每周热点");
        //titles.add("最高评论");
        resetGroups(map,titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Article item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.course_time.setText(item.getCourse_time());
        h.course_teacher.setText(item.getCourse_teacher());
        h.course_address.setText(item.getCourse_address());
        h.course_progress.setProgress(12);
        h.course_home_work.setText(item.getCourse_home_work());
        h.course_progress_num.setText(item.getCourse_progress_num());

        h.tv_time.setText("时间：");
        h.tv_teacher.setText("老师：");
        h.tv_address.setText("地点：");
        h.tv_progress.setText("进度：");
        h.tv_homework.setText("作业：");

    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_teacher, tv_time,tv_address, tv_homework, tv_progress;
        private ProgressBar course_progress;
        private TextView course_time, course_address, course_teacher, course_home_work, course_progress_num, course_signIn_tv;
        private LinearLayout hand_homework, leave_layout, signIn_layout;
        private ImageView course_signIn_img;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            course_teacher = (TextView) itemView.findViewById(R.id.course_teacher_name);//老师姓名
            course_address = (TextView) itemView.findViewById(R.id.course_address);//教室类别
            course_home_work = itemView.findViewById(R.id.course_homework);//作业内容
            course_progress = itemView.findViewById(R.id.course_progress);//上课进度
            course_time = itemView.findViewById(R.id.course_time);
            course_progress_num = itemView.findViewById(R.id.course_progress_num);

            tv_time = itemView.findViewById(R.id.course_tv_time);
            tv_address = itemView.findViewById(R.id.course_tv_address);
            tv_teacher = itemView.findViewById(R.id.course_tv_teacher);
            tv_progress = itemView.findViewById(R.id.course_tv_progress);
            tv_homework = itemView.findViewById(R.id.course_tv_homework);

            hand_homework = itemView.findViewById(R.id.course_hand_homework_layout);
            leave_layout = itemView.findViewById(R.id.course_leave_layout);
            signIn_layout = itemView.findViewById(R.id.course_sign_in_layout);

            course_signIn_img = itemView.findViewById(R.id.course_sign_in_img);
            course_signIn_tv = itemView.findViewById(R.id.course_sign_in_tv);

            hand_homework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build("/course/handHomeWork").navigation();
                }
            });

            leave_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build("/course/leaveActivity").navigation();
                }
            });

            signIn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    course_signIn_tv.setText("已签");
                    course_signIn_tv.setTextColor(Color.parseColor("#999999"));
                }
            });
        }
    }


    private static Article create(String time, String address, String teacher, int progress, String homeWork, String progressNum,int id) {
        Article article = new Article();
        article.setCourse_time(time);
        article.setCourse_address(address);
        article.setCourse_teacher(teacher);
        article.setCourse_progress(progress);
        article.setCourse_home_work(homeWork);
        article.setCourse_progress_num(progressNum);
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();

        if (p == 0)
        {
            list.add(create("18:00-19:00", "小提琴室", "李老师", 12,"小提琴琴谱练习","12%",0));
            list.add(create("15:00-16:00", "美术", "黄老师", 8,"美术阶梯教室", "8%",0));
        }

        return list;
    }
}
