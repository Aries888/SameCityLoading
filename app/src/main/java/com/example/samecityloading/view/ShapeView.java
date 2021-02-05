package com.example.samecityloading.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.samecityloading.R;

/**
 * Created by Administrator on 2021/2/4 0004 - 下午 5:01
 */
public class ShapeView extends View {
    private Paint mPaint;
    private Shape mCurrentShape = Shape.Circle;
    private Path mPath;

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        switch (mCurrentShape) {
            case Circle:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.circle));
                canvas.drawCircle(center, center, center, mPaint);
                break;
            case Square:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.rect));
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
                break;
            case Triangle:
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.triangle));
                if (mPath == null) {
                    mPath = new Path();
                    mPath.moveTo(center, 0);
                    mPath.lineTo(0, (float) (center * Math.sqrt(3)));
                    mPath.lineTo(getWidth(), (float) (center * Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath, mPaint);
                break;
        }
    }

    /**
     * 切换形状
     */
    public void exchange() {
        switch (mCurrentShape) {
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
        invalidate();
    }

    /**
     * 获取当前的形状
     */

    public Shape getCurrentShape() {
        return mCurrentShape;
    }

    public enum Shape {
        Circle, Square, Triangle
    }
}
