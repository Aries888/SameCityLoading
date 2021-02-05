package com.example.samecityloading.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.samecityloading.R;
import com.example.samecityloading.view.ShapeView;

/**
 * Created by Administrator on 2021/2/4 0004 - 下午 4:40
 */
public class ShapeLoading extends LinearLayout {
    private ShapeView mShapeView;
    private ImageView mIndication;
    private Context mContext;
    private int mTractionDistant = 0;
    private final long ANIMATION_DURATION = 350;
    private boolean isStopAnimation = false;

    public ShapeLoading(Context context) {
        this(context, null);
    }

    public ShapeLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mTractionDistant = dp2x(80);
        initLayout();
    }

    private int dp2x(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private void initLayout() {
        View.inflate(mContext, R.layout.load_view, this);
        mShapeView = (ShapeView) findViewById(R.id.shapeView);
        mIndication = (ImageView) findViewById(R.id.indication);
        post(new Runnable() {
            @Override
            public void run() {
                startFallAnimator();
            }
        });
    }

    private void startFallAnimator() {
        if (isStopAnimation) {
            return;
        }
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(mShapeView, "translationY", 0, mTractionDistant);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mIndication, "scaleX", 1f, 0.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(translationYAnimator, scaleXAnimator);
        animatorSet.setInterpolator(new AccelerateInterpolator());  //加速
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //下落完毕后，上抛
                startUpAnimation();
            }
        });
        animatorSet.start();
    }

    private void startUpAnimation() {
        if (isStopAnimation) {
            return;
        }
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mShapeView, "translationY", mTractionDistant, 0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mIndication, "scaleX", 0.3f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(translationY, scaleX);
        animatorSet.setInterpolator(new DecelerateInterpolator());  //减速
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startFallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //旋转动画
                startRotationAnimator();
                //改变形状
                mShapeView.exchange();
            }
        });
        animatorSet.start();
    }

    private void startRotationAnimator() {
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()) {
            case Circle:
            case Square:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, 180);
                break;
            case Triangle:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView, "rotation", 0, -120);
                break;
        }
        rotationAnimator.setDuration(ANIMATION_DURATION);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        mShapeView.clearAnimation();
        mIndication.clearAnimation();

        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
            removeAllViews();
        }

        isStopAnimation = true;
    }
}
