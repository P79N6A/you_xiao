package com.runtoinfo.information.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.information.R;
import com.runtoinfo.youxiao.globalTools.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/10.
 */

@SuppressWarnings("all")
public class SystemMessageAdapter extends RecyclerView.Adapter {

    public final static int TYPE_SYSTEM = 0;
    public final static int TYPE_MESSAGE = 1;
    public final static int TYPE_SCHOOL_NOTICE_PIC = 2;
    public final static int TYPE_SCHOOL_NOTICE_MESSAGE = 3;
    public Activity context;
    public List<SystemMessageEntity> dataList = new ArrayList<>();
    public HttpUtils httpUtils;

    public SystemMessageAdapter(Activity context, List<SystemMessageEntity> dataList) {
        this.context = context;
        this.dataList = dataList;
        httpUtils = new HttpUtils(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_SYSTEM:
                view = inflater.inflate(R.layout.sysytem_message_item_layout, parent, false);
                return new ViewHolder(view);
            case TYPE_MESSAGE:
                view = inflater.inflate(R.layout.course_player_item_layout, parent, false);
                return new CourseHolder(view);
            case TYPE_SCHOOL_NOTICE_PIC:
                view = inflater.inflate(R.layout.school_notice_picture_layout, parent, false);
                return new SchoolNoticePic(view);
            case TYPE_SCHOOL_NOTICE_MESSAGE:
                view = inflater.inflate(R.layout.school_notice_msg_layout, parent, false);
                return new SchoolNoticeMessage(view);
            default:
                view = inflater.inflate(R.layout.course_player_item_layout, parent, false);
                return new CourseHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SystemMessageEntity messageEntity = dataList.get(position);
        switch (messageEntity.getItemType()){
            case 0:
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.time.setText(messageEntity.getTime());
                viewHolder.title.setText(messageEntity.getTitle());
                viewHolder.pay_number.setText("支付单号:" + messageEntity.getPay_number());
                viewHolder.pay_details.setText("交易详情:" + messageEntity.getPay_details());
                viewHolder.pay_type.setText("交易方式:" + messageEntity.getPay_type());
                viewHolder.pay_time.setText("交易时间:" + messageEntity.getPay_time());
                viewHolder.pay_prices.setText("交易金额:" + messageEntity.getPay_price());
                break;
            case 1:
                CourseHolder courseHolder = (CourseHolder) holder;
                courseHolder.time.setText(messageEntity.getTime());
                courseHolder.message.setText(messageEntity.getMessage());
                courseHolder.title.setText(messageEntity.getTitle());
                break;
            case 2:
                SchoolNoticePic schoolNotice = (SchoolNoticePic) holder;
                httpUtils.postSrcPhoto(context, HttpEntity.IMAGE_HEAD + messageEntity.getCover(), schoolNotice.imageView);
                schoolNotice.time.setText(TimeUtil.timeType(messageEntity.getCreationTime(), 0));
                schoolNotice.message.setText(messageEntity.getMessage());
                break;
            case 3:
                SchoolNoticeMessage schoolNoticeMessage = (SchoolNoticeMessage) holder;
                schoolNoticeMessage.message.setText(messageEntity.getMessage());
                schoolNoticeMessage.time.setText(TimeUtil.timeType(messageEntity.getCreationTime(), 0));
                schoolNoticeMessage.title.setText(messageEntity.getTitle());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        SystemMessageEntity messageEntity = dataList.get(position);
        switch (messageEntity.getItemType()) {
            case 0:
                return TYPE_SYSTEM;
            case 1:
                return TYPE_MESSAGE;
            case 2:
                return TYPE_SCHOOL_NOTICE_PIC;
            case 3:
                return TYPE_SCHOOL_NOTICE_MESSAGE;
            default:
                return TYPE_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, time, pay_prices, pay_time, pay_type, pay_details, pay_number, message;
        public TextView go_where;

        @SuppressLint("CutPasteId")
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.system_message_item_title);
            time = itemView.findViewById(R.id.system_message_item_time);
            pay_prices = itemView.findViewById(R.id.system_msg_pay_price);
            pay_time = itemView.findViewById(R.id.system_pay_time);
            pay_type = itemView.findViewById(R.id.system_pay_type);
            pay_details = itemView.findViewById(R.id.system_pay_details);
            pay_number = itemView.findViewById(R.id.system_number);
            go_where = itemView.findViewById(R.id.system_go_where);
            go_where.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    class CourseHolder extends RecyclerView.ViewHolder {

        public TextView time, title, message, go_where;

        public CourseHolder(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.course_remind_time);
            title = itemView.findViewById(R.id.course_remind_title);
            message = itemView.findViewById(R.id.course_remind_message);
            go_where = itemView.findViewById(R.id.reminder_go_course);

            go_where.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    class SchoolNoticePic extends RecyclerView.ViewHolder {

        public TextView time, message;
        public ImageView imageView;

        @SuppressLint("CutPasteId")
        public SchoolNoticePic(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.school_notice_time);
            imageView = itemView.findViewById(R.id.school_notice_picture);
            message = itemView.findViewById(R.id.school_notice_message);
        }
    }

    class SchoolNoticeMessage extends RecyclerView.ViewHolder {

        public TextView time, title, message;

        public SchoolNoticeMessage(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.school_notice_msg_time);
            title = itemView.findViewById(R.id.school_notice_msg_title);
            message = itemView.findViewById(R.id.school_notice_msg_details);
        }
    }
}
