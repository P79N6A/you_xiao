package com.runto.cources.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 简单周视图
 * Created by QiaoJunChao on 2017/11/29.
 */

public class SimpleWeekView extends WeekView {
    private int mRadius;
    public int mH;
    public int mW;
    private Paint mSchemeBasicPaint = new Paint();

    public SimpleWeekView(Context context) {
        super(context);
        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xff333333);
        mSchemeBasicPaint.setFakeBoldText(true);
        mH = dipToPx(getContext(), 2);
        mW = dipToPx(getContext(), 2);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2 + mW / 2;
        int cy = mItemHeight / 2 + mH / 2;
        canvas.drawCircle(cx, cy, mRadius, calendar.isCurrentDay()? mCurDayBackPaint : mSelectedPaint);
        if (calendar.isCurrentDay()) {
            mSelectTextPaint.setColor(0xffffffff);
        }else{
            mSelectTextPaint.setColor(0xff333333);
        }
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());
        canvas.drawCircle(x + mItemWidth / 2, mItemHeight - mH, mH, mSchemeBasicPaint );
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            if (calendar.isCurrentDay()){
                canvas.drawCircle(cx, cy, mRadius, mCurDayBackPaint);
            }
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mSchemeTextPaint);

        } else {
            if (calendar.isCurrentDay()){
                canvas.drawCircle(cx, cy, mRadius, mCurDayBackPaint);
            }
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mCurMonthTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
