package com.global.coffer.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.global.coffer.map.utils.DrawState;

import java.util.ArrayDeque;
import java.util.Deque;

public class DrawingViewV2 extends View {

    /**
     * 绘制路径的画笔
     */
    private final Paint mRoutePaint = new Paint();

    /**
     * 用于填充区域的画笔
     */
    private final Paint mFillPaint = new Paint();   // 用于填充区域的画笔

    /**
     * 当前的渲染路径
     */
    private final Path mCurrentPath = new Path();

    /**
     * 最终需要组合的区域
     */
    private final Path mCombinedPath = new Path();

    /**
     * 使用队列记录每次绘制的路径信息
     */
    private final Deque<Path> mPathsList = new ArrayDeque<>();

    /**
     * 当前绘制状态
     */
    private DrawState mCurrentState = DrawState.Default;

    public DrawingViewV2(Context context) {
        super(context);
        init();
    }

    public DrawingViewV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawingViewV2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mRoutePaint.setColor(Color.BLUE);
        mRoutePaint.setStyle(Paint.Style.STROKE);
        mRoutePaint.setStrokeWidth(10);
        mRoutePaint.setAntiAlias(true);

        mFillPaint.setColor(Color.RED);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(mCurrentState == DrawState.Finish){
            for(Path path : mPathsList){
                mCombinedPath.addPath(path);
            }
            canvas.drawPath(mCombinedPath,mFillPaint);
        }

        for(Path p: mPathsList){
            canvas.drawPath(p, mRoutePaint);
        }
        // 绘制路径
        canvas.drawPath(mCurrentPath, mRoutePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!checkCanDraw()){
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 移动到触摸点
                mCurrentPath.moveTo(x,y);
                return true;
            case MotionEvent.ACTION_MOVE:
                // 连接路径到触摸点
                mCurrentPath.lineTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                mPathsList.addLast(new Path(mCurrentPath));
                mCurrentPath.reset();
                break;
        }
        // 请求重新绘制
        invalidate();
        return true;
    }

    private boolean checkCanDraw(){
        return mCurrentState == DrawState.Start;
    }

    /**
     * 设置绘制状态
     * @param status 状态
     */
    public void setDrawStatus(DrawState status){
        mCurrentState = status;
        if(mCurrentState == DrawState.Finish){
            // 完成绘制后，将图形区域混合
            invalidate();
        }
    }

    /**
     * 撤销上次的绘制的路径
     */
    public void cancelLastDrawAction(){
        if(mPathsList.isEmpty()){
            return;
        }
        mPathsList.removeLast();
        invalidate();
    }

    /**
     * 清空路径
     */
    public void clearPath(){
        mPathsList.clear();
        mCombinedPath.reset();
        invalidate();
    }
}
