package com.runtoinfo.information.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.runtoinfo.httpUtils.HttpEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageEntity;
import com.runtoinfo.httpUtils.bean.SystemMessageGroupEntity;
import com.runtoinfo.httpUtils.utils.HttpUtils;
import com.runtoinfo.information.R;
import com.runtoinfo.information.databinding.InformationCourseChildLayoutBinding;
import com.runtoinfo.information.databinding.InformationHeadLayoutBinding;
import com.runtoinfo.information.databinding.SchoolNoticePictureChildLayoutBinding;
import com.runtoinfo.information.databinding.SystemOrSchoolNoticeChildLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiaoJunChao on 2018/9/10.
 */

@SuppressWarnings("all")
public class SystemMessageAdapter extends GroupedRecyclerViewAdapter{

    public final static int PAY_MESSAGE = 0;
    public final static int TYPE_MESSAGE = 1;
    public final static int TYPE_SCHOOL_NOTICE_PIC = 2;
    public final static int TYPE_SCHOOL_NOTICE_MESSAGE = 3;
    public Activity context;
    public List<SystemMessageGroupEntity> dataList = new ArrayList<>();
    public HttpUtils httpUtils;

    public SystemMessageAdapter(Activity context, List<SystemMessageGroupEntity> dataList) {
        super(context, true);
        this.context = context;
        this.dataList = dataList;
        httpUtils = new HttpUtils(context);
    }

//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = null;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        switch (viewType) {
//            case PAY_MESSAGE:
//                view = inflater.inflate(R.layout.pay_message_item_layout, parent, false);
//                return new ViewHolder(view);
//            case TYPE_MESSAGE:
//                view = inflater.inflate(R.layout.course_player_item_layout, parent, false);
//                return new CourseHolder(view);
//            case TYPE_SCHOOL_NOTICE_PIC:
//                view = inflater.inflate(R.layout.school_notice_picture_layout, parent, false);
//                return new SchoolNoticePic(view);
//            case TYPE_SCHOOL_NOTICE_MESSAGE:
//                view = inflater.inflate(R.layout.school_notice_msg_layout, parent, false);
//                return new SchoolNoticeMessage(view);
//            default:
//                view = inflater.inflate(R.layout.course_player_item_layout, parent, false);
//                return new CourseHolder(view);
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        SystemMessageEntity messageEntity = dataList.get(position);
//        switch (messageEntity.getItemType()){
//            case 0:
//                ViewHolder viewHolder = (ViewHolder) holder;
//                viewHolder.time.setText(messageEntity.getTime());
//                viewHolder.title.setText(messageEntity.getTitle());
//                viewHolder.pay_number.setText("支付单号:" + messageEntity.getPay_number());
//                viewHolder.pay_details.setText("交易详情:" + messageEntity.getPay_details());
//                viewHolder.pay_type.setText("交易方式:" + messageEntity.getPay_type());
//                viewHolder.pay_time.setText("交易时间:" + messageEntity.getPay_time());
//                viewHolder.pay_prices.setText("交易金额:" + messageEntity.getPay_price());
//                break;
//            case 1:
//                CourseHolder courseHolder = (CourseHolder) holder;
//                courseHolder.time.setText(messageEntity.getTime());
//                courseHolder.message.setText(messageEntity.getMessage());
//                courseHolder.title.setText(messageEntity.getTitle());
//                break;
//            case 2:
//                SchoolNoticePic schoolNotice = (SchoolNoticePic) holder;
//                httpUtils.postSrcPhoto(context, HttpEntity.IMAGE_HEAD + messageEntity.getCover(), schoolNotice.imageView);
//                schoolNotice.time.setText(TimeUtil.timeType(messageEntity.getCreationTime(), 0));
//                schoolNotice.message.setText(messageEntity.getMessage());
//                break;
//            case 3:
//                SchoolNoticeMessage schoolNoticeMessage = (SchoolNoticeMessage) holder;
//                schoolNoticeMessage.message.setText(messageEntity.getMessage());
//                schoolNoticeMessage.time.setText(TimeUtil.timeType(messageEntity.getCreationTime(), 0));
//                schoolNoticeMessage.title.setText(messageEntity.getTitle());
//                break;
//        }
//
//    }

//    @Override
//    public int getItemViewType(int position) {
//        List<SystemMessageEntity> messageEntityList = dataList.get(position).getItems();
//        if(messageEntityList.size() > 0) {
//            SystemMessageEntity messageEntity = dataList.get(position).getItems().get(0);
//            switch (messageEntity.getNotificationName()) {
//                case "system":
//                    return TYPE_SCHOOL_NOTICE_MESSAGE;
//                case "lessonReminder":
//                    return TYPE_MESSAGE;
//                case "campusNotice":
//                    if (!TextUtils.isEmpty(messageEntity.getCover()))
//                        return TYPE_SCHOOL_NOTICE_PIC;
//                    else
//                        return TYPE_SCHOOL_NOTICE_MESSAGE;
//                default:
//                    return TYPE_MESSAGE;
//            }
//        }
//        return -1;
//    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        List<SystemMessageEntity> messageEntityList = dataList.get(groupPosition).getItems();
        if(messageEntityList.size() > 0) {
            SystemMessageEntity messageEntity = messageEntityList.get(childPosition);
            switch (messageEntity.getNotificationName()) {
                case "system":
                    return TYPE_SCHOOL_NOTICE_MESSAGE;
                case "lessonReminder":
                    return TYPE_MESSAGE;
                case "campusNotice":
                    if (!TextUtils.isEmpty(messageEntity.getCover()))
                        return TYPE_SCHOOL_NOTICE_PIC;
                    else
                        return TYPE_SCHOOL_NOTICE_MESSAGE;
                default:
                    return TYPE_MESSAGE;
            }
        }
        return -1;
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(groupPosition).getItems().size();
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
        return R.layout.information_head_layout;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        switch (viewType) {
            case PAY_MESSAGE://支付信息，暂时没有此功能
                return R.layout.pay_message_item_layout;
            case TYPE_MESSAGE:
                return R.layout.information_course_child_layout;
            case TYPE_SCHOOL_NOTICE_PIC:
                return R.layout.school_notice_picture_child_layout;
            case TYPE_SCHOOL_NOTICE_MESSAGE:
                return R.layout.system_or_school_notice_child_layout;
            default:
                return R.layout.system_or_school_notice_child_layout;
        }
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        InformationHeadLayoutBinding headLayoutBinding = holder.getBinding();
        headLayoutBinding.courseRemindTime.setText(dataList.get(groupPosition).getKey());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        List<SystemMessageEntity> messList = dataList.get(groupPosition).getItems();
        if (messList.size() > 0) {
            SystemMessageEntity messageEntity = messList.get(childPosition);
            switch (messageEntity.getNotificationName()) {
//                case 0:
//                    ViewHolder viewHolder = (ViewHolder) holder;
//                    viewHolder.time.setText(messageEntity.getTime());
//                    viewHolder.title.setText(messageEntity.getTitle());
//                    viewHolder.pay_number.setText("支付单号:" + messageEntity.getPay_number());
//                    viewHolder.pay_details.setText("交易详情:" + messageEntity.getPay_details());
//                    viewHolder.pay_type.setText("交易方式:" + messageEntity.getPay_type());
//                    viewHolder.pay_time.setText("交易时间:" + messageEntity.getPay_time());
//                    viewHolder.pay_prices.setText("交易金额:" + messageEntity.getPay_price());
//                    break;
                case "lessonReminder":
                    InformationCourseChildLayoutBinding courseBinding = (InformationCourseChildLayoutBinding) holder.getBinding();
                    courseBinding.courseRemindTitle.setText(messageEntity.getTitle());
                    courseBinding.courseRemindMessage.setText(messageEntity.getMessage());
                    break;
                case "system":
                case "campusNotice":
                    if (!TextUtils.isEmpty(messageEntity.getCover())){
                        SchoolNoticePictureChildLayoutBinding pictureBinding = (SchoolNoticePictureChildLayoutBinding) holder.getBinding();
                        pictureBinding.schoolNoticeMessage.setText(messageEntity.getMessage());
                        Glide.with(context).load(HttpEntity.IMAGE_HEAD + messageEntity.getCover()).into(pictureBinding.schoolNoticePicture);
                    }else{
                        SystemOrSchoolNoticeChildLayoutBinding noticeBinding =
                                holder.getBinding();
                        noticeBinding.schoolNoticeMsgDetails.setText(messageEntity.getMessage());
                        noticeBinding.schoolNoticeMsgTitle.setText(messageEntity.getTitle());

                    }

            }
        }
    }

    /*class ViewHolder extends RecyclerView.ViewHolder {

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

    class CourseHolder extends BaseViewHolder {

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

    class SchoolNoticePic extends BaseViewHolder {

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

    class SchoolNoticeMessage extends BaseViewHolder {

        public TextView time, title, message;

        public SchoolNoticeMessage(View itemView) {
            super(itemView);

//            time = itemView.findViewById(R.id.school_notice_msg_time);
//            title = itemView.findViewById(R.id.school_notice_msg_title);
//            message = itemView.findViewById(R.id.school_notice_msg_details);
        }
    }*/
}
