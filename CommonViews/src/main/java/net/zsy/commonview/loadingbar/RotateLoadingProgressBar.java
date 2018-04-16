package net.zsy.commonview.loadingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import net.zsy.commonview.R;

/**
 * Created by Android on 2018/4/11.
 */

public class RotateLoadingProgressBar extends View {

    private static final int DEFAULT_MIN_WIDTH = 400; //View默认最小宽度
    private static final float ringRaduisPercent = 0.65f; //圆环外圆半径占View最大半径的百分比
    private float ringWidthPercent = 0.2f; //圆环宽度占View最大半径的百分比

    private Paint paint; //画笔
    private Paint paintCircle; //画笔
    private float width; //自定义view的宽度
    private float height; //自定义view的高度
    private float radius; //自定义view的最大半径
    private RectF rectF;
    private float ringWidth;//圆环宽度

    private int startColor = Color.WHITE;
    private int endColor = Color.GRAY;
    private float duration = 1.5f;

    private RotateAnimation animation;

    public RotateLoadingProgressBar(Context context) {
        super(context);
        init();
    }

    public RotateLoadingProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        init();
    }

    public RotateLoadingProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        init();
    }

    private void initAttrs(Context context,AttributeSet attrs){
        TypedArray array =context.obtainStyledAttributes(attrs, R.styleable.RotateLoadingProgressBarAttrs);
        startColor = array.getColor(R.styleable.RotateLoadingProgressBarAttrs_startColor,Color.WHITE);
        endColor = array.getColor(R.styleable.RotateLoadingProgressBarAttrs_endColor,Color.GRAY);
        duration = array.getFloat(R.styleable.RotateLoadingProgressBarAttrs_duration,1.5f);
        float ringWidth = array.getFloat(R.styleable.RotateLoadingProgressBarAttrs_ringWidth,1f);
        ringWidthPercent *= ringWidth;
        array.recycle();
    }

    private void init(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(new SweepGradient(0, 0, startColor, endColor));

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeJoin(Paint.Join.ROUND);
        paintCircle.setStrokeCap(Paint.Cap.ROUND);
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(endColor);

        animation = new RotateAnimation(0,359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration((long) (duration * 1000));
        animation.setRepeatCount(RotateAnimation.INFINITE);//无限循环
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(rectF == null)
            return;

        canvas.translate(width / 2, height / 2);
        canvas.drawArc(rectF, 0, 360, false, paint);
        canvas.drawCircle(radius * ringRaduisPercent,0, ringWidth/2 , paintCircle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetParams();
    }

    private void resetParams() {
        width = getWidth();
        height = getHeight();
        radius = Math.min(width, height)/2;

        rectF = new RectF(-radius * ringRaduisPercent, -radius * ringRaduisPercent, radius * ringRaduisPercent, radius * ringRaduisPercent);
        ringWidth = radius * ringWidthPercent;//圆环宽度
        paint.setStrokeWidth(ringWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility == VISIBLE){
            startAnimation(animation);
        }else{
            clearAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(getVisibility() == VISIBLE){
            startAnimation(animation);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }
}
