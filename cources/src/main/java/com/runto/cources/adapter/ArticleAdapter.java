package com.runto.cources.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public ArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        LinkedHashMap<String, List<Article>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        map.put("今日课程", create(0));
        //map.put("每周热点", create(1));
        //map.put("最高评论", create(2));
        titles.add("今日课程");
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
        h.cources_teacher_name.setText(item.getCourse_teacher_name());
        h.cources_violin_room.setText(item.getCourse_class_room());
        h.class_progress.setText(item.getCourse_class_progress());
        h.cources_violin_sheet.setText(item.getCourse_home_work());

        h.tv_work.setText("作业：");
        h.tv_teacher.setText("老师：");
        h.tv_class_room.setText("教室：");
        h.tv_schedule.setText("进度：");

        h.mImageView.setBackgroundResource(item.getImage_id());
//        mLoader.load(item.getImgUrl())
//                .asBitmap()
//                .centerCrop()
//                .into(h.mImageView);
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_teacher,
                cources_teacher_name;
        private ImageView mImageView;
        private TextView tv_schedule;
        private TextView class_progress;
        private TextView tv_class_room, cources_violin_room, tv_work, cources_violin_sheet;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            cources_teacher_name = (TextView) itemView.findViewById(R.id.cources_teacher_name);//老师姓名
            cources_violin_room = (TextView) itemView.findViewById(R.id.cources_violin_room);//教室类别
            cources_violin_sheet = itemView.findViewById(R.id.cources_violin_sheet);//作业内容
            class_progress = itemView.findViewById(R.id.cources_progress);//上课进度
            tv_schedule = itemView.findViewById(R.id.tv_schedule);
            tv_teacher = itemView.findViewById(R.id.tv_teacher);
            tv_class_room = itemView.findViewById(R.id.tv_class_room);
            tv_work = itemView.findViewById(R.id.tv_work);

            mImageView = (ImageView) itemView.findViewById(R.id.cources_statue);
        }
    }


    private static Article create(String name, String class_name, String progress, String room, int id) {
        Article article = new Article();
        article.setCourse_teacher_name(name);
        article.setCourse_class_room(room);
        article.setCourse_class_progress(progress);
        article.setCourse_home_work(room);
        if (id != 0) {
            article.setImage_id(id);
        }
        return article;
    }

    private static List<Article> create(int p) {
        List<Article> list = new ArrayList<>();

        if (p == 0)
        {
            list.add(create("黄磊", "English", "第40节/共60节","大课堂",0));
            list.add(create("黄磊", "English", "第40节/共60节","大课堂",0));
            list.add(create("黄磊", "English", "第40节/共60节","大课堂",0));
        }

        return list;
    }
}
