package com.custom.circleprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Leone on 2014/7/10.
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2014/7/10
 */
public class CircleProgressBar extends View {

    /** 画Progress各个组件所需要的画笔 **/
    private Paint mPaint;
    /** 进度圆环的宽度 **/
    private float mRingWidth;
    /** 进度圆环的一般状态颜色 **/
    private int mRingNormalColor;
    /** 进度圆环的进度颜色 **/
    private int mRingProgressColor;
    /** 进度文字的大小 **/
    private float mProgressTextSize;
    /** 进度文字的颜色 **/
    private int mProgressTextColor;
    /** 进度文字的显示与否 **/
    private boolean mProgressTextVisibility;
    /** 组件显示成填充类型 **/
    private static final int STYLE_FILL = 1;
    /** 组件显示成画笔类型 **/
    private static final int STYLE_STAROKE = 0;
    /** 组件所需显示的类型：fill或者stroke **/
    private int mProgressBarStyle;
    /** 默认的进度字体大小 **/
    private static final int DEFAULT_PROGRESS_TEXT_SIZE = 16;
    /** 默认的进度圈宽度 **/
    private static final int DEFAULT_RING_CIRCLE_WIDTH = 8;
    /** 进度 **/
    private int mProgress;
    /** 设置最大进度 **/
    private int mMaxProgress;
    /** 默认最大进度 **/
    private static final int DEFAULT_MAX_PROGRESS = 100;
    /** 圆的度数 360度 **/
    private static final int CIRCLE_ANGLE = 360;

    /**
     * CircleProgressBar
     * @param context context
     * @param attrs attrs
     */
    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * CircleProgressBar
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        float density = getResources().getDisplayMetrics().density;
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mProgressBarStyle = typeArray.getInt(R.styleable.CircleProgressBar_circleStyle, STYLE_STAROKE);
        mProgressTextColor = typeArray.getColor(R.styleable.CircleProgressBar_progressTextColor, Color.BLACK);
        mProgressTextSize = typeArray.getDimension(R.styleable.CircleProgressBar_progressTextSize, (int) (DEFAULT_PROGRESS_TEXT_SIZE * density + 0.5f));
        mProgressTextVisibility = typeArray.getBoolean(R.styleable.CircleProgressBar_progressTextVisibility, true);
        mRingWidth = typeArray.getDimension(R.styleable.CircleProgressBar_ringWidth, (int) (DEFAULT_RING_CIRCLE_WIDTH * density));
        mRingNormalColor = typeArray.getColor(R.styleable.CircleProgressBar_ringNormalColor, Color.GRAY);
        mRingProgressColor = typeArray.getColor(R.styleable.CircleProgressBar_ringProgressColor, Color.YELLOW);
        mMaxProgress = typeArray.getInt(R.styleable.CircleProgressBar_maxProgress, DEFAULT_MAX_PROGRESS);
        typeArray.recycle();
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 首先画外围进度圈
        int center = getWidth() / 2;
        int radius = (int) (center - mRingWidth / 2);
        mPaint.setColor(mRingNormalColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(center, center, radius, mPaint);

        // 画文字进度，设置到进度圈中间
        if (!mProgressTextVisibility || mProgress == 0 || mProgressBarStyle == STYLE_FILL) {
            mPaint.setStrokeWidth(0);
            mPaint.setColor(mProgressTextColor);
            mPaint.setTextSize(mProgressTextSize);
            // 设置字体
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
            StringBuilder percentBuilder = new StringBuilder((int) (((float) mProgress / (float) mMaxProgress) * 100));
            percentBuilder.append("%");
            // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
            float textWidth = mPaint.measureText(percentBuilder.toString());
            canvas.drawText(percentBuilder.toString(), center - textWidth / 2, center + textWidth / 2, mPaint);
        }

        // 画圆环进度圈
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setColor(mRingProgressColor);
        RectF rectF = new RectF(center - radius, center - radius, center + radius, center + radius);
        switch (mProgressBarStyle) {
            case STYLE_FILL:
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawArc(rectF, 0, CIRCLE_ANGLE * mProgress / mMaxProgress, true, mPaint);
                break;
            case STYLE_STAROKE:
            default:
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF, 0, CIRCLE_ANGLE * mProgress / mMaxProgress, false, mPaint);
                break;
        }
    }


    /**
     * 设置进度
     * @param progress progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            mProgress = 0;

        } else if (progress > mMaxProgress) {
            mProgress = mMaxProgress;
        } else {
            mProgress = progress;
        }
        postInvalidate();
    }

    /**
     * 获取进度
     * @return 进度
     */
    public synchronized int getProgress() {
        return mProgress;
    }

    /**
     * 获取最大进度
     * @return maxProgress
     */
    public synchronized int getMaxProgress() {
        return mMaxProgress;
    }
}
