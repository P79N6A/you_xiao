package com.runto.cources.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
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
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.CourseEntity;
import com.runtoinfo.httpUtils.bean.RequestDataEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.youxiao.globalTools.utils.DialogMessage;
import com.runtoinfo.youxiao.globalTools.utils.Entity;
import com.runtoinfo.youxiao.globalTools.utils.SPUtils;
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

public class ArticleAdapter extends GroupRecyclerAdapter<String, Article> {


    private RequestManager mLoader;
    private Context context;
    private List<CourseEntity> dataList = new ArrayList<>();
    private View.OnClickListener onClickListener;
    public SPUtils spUtils;
    public Handler handler;
    public HttpUtils httpUtils;

    public ArticleAdapter(Context context, List<CourseEntity> dataList, Handler handler) {
        super(context);
        this.context = context;
        this.dataList = dataList;
        httpUtils = new HttpUtils(context);
        spUtils = new SPUtils(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日课程", create(dataList));
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
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, final Article item, int position) {
        final ArticleViewHolder h = (ArticleViewHolder) holder;
        if (item.getType() == 0) {
            h.course_name.setText(item.getCourse_name());
            h.course_time.setText(item.getCourse_time());
            h.course_teacher.setText(item.getCourse_teacher());
            h.course_address.setText(item.getCourse_address());
            h.course_progress.setProgress(item.getCourse_progress());
            h.course_home_work.setText(item.getCourse_home_work());
            h.course_progress_num.setText(item.getCourse_progress_num());

            h.tv_time.setText("时间：");
            h.tv_teacher.setText("老师：");
            h.tv_address.setText("地点：");
            h.tv_progress.setText("进度：");
            h.tv_homework.setText("作业：");
            if (item.isSignIn()){
                h.course_signIn_tv.setText("已签");
                h.course_signIn_tv.setTextColor(Color.parseColor("#999999"));
            }else{
                h.course_signIn_tv.setText("签到");
                h.course_signIn_tv.setTextColor(Color.parseColor("#666666"));
            }

            h.signIn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("token", spUtils.getString(Entity.TOKEN));
                    dataMap.put("CourseInstId", item.getCourseInsId());
                    dataMap.put("url", HttpEntity.MAIN_URL + HttpEntity.POST_SIGNIN_COURSE);
                    httpUtils.postSignIn(handler,dataMap);
                }
            });
        }else{
            h.no_course_layout.setVisibility(View.VISIBLE);
            h.course_item_layout.setVisibility(View.GONE);
            h.no_course_message.setText(item.getCourse_message());
        }

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        h.course_signIn_tv.setText("已签");
                        h.course_signIn_tv.setTextColor(Color.parseColor("#999999"));
                        break;
                    case 400:
                        DialogMessage.showToast(context, "签到失败");
                        break;
                    case 404:
                        DialogMessage.showToast(context, "数据解析错误");
                        break;
                    case 500:
                        DialogMessage.showToast(context, "服务器异常");
                        break;
                }
            }
        };
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_teacher, tv_time,tv_address, tv_homework, tv_progress;
        private ProgressBar course_progress;
        private TextView course_time, course_address, course_teacher, course_home_work, course_progress_num, course_signIn_tv, course_name;
        private LinearLayout hand_homework, leave_layout, signIn_layout;
        private ImageView course_signIn_img;
        private LinearLayout course_item_layout;
        private LinearLayout no_course_layout;
        private TextView no_course_message;

        private ArticleViewHolder(View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
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

            course_item_layout = itemView.findViewById(R.id.course_details_layout);
            no_course_layout = itemView.findViewById(R.id.no_course_layout);
            no_course_message = itemView.findViewById(R.id.course_no_message);

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

//            signIn_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    course_signIn_tv.setText("已签");
//                    course_signIn_tv.setTextColor(Color.parseColor("#999999"));
//                }
//            });
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

    private static Article create(CourseEntity entity){
        Article article = new Article();
        if (entity.getType() != 1) {
            article.setCourse_time(entity.getStartTime().split(" ")[1].substring(0, 5) + "-" + entity.getEndTime().split(" ")[1].substring(0, 5));
            article.setCourse_address(entity.getClassroomName());
            article.setCourse_teacher(entity.getTeacherName());
            article.setCourse_progress(entity.getProgress());
            article.setCourse_home_work(entity.getHomeworkRequirement());
            article.setCourse_progress_num(entity.getProgress() + "%");
            article.setCourse_name(entity.getCourseName());
            article.setCourse_message(entity.getCourseMessage());
            article.setType(entity.getType());
            article.setCourseInsId(entity.getCourseInstId());
            article.setSignIn(entity.isSignIn());
        }else{
            article.setType(entity.getType());
            article.setCourse_message(entity.getCourseMessage());
        }
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();

        if (p == 0)
        {
            //list.add(create("", "", "", 0,"","",0));
            //list.add(create("15:00-16:00", "美术", "黄老师", 8,"美术阶梯教室", "8%",0));
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.setCourseName("暂无课程");
            list.add(create(courseEntity));
        }

        return list;
    }
    private static List<Article> create(List<CourseEntity> dataList){
        List<Article> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++){
            list.add(create(dataList.get(i)));
        }
        return list;
    }
}
