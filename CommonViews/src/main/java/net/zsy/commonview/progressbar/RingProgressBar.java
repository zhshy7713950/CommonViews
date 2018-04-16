package net.zsy.commonview.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import net.zsy.commonview.R;

/**
 * Created by Android on 2018/4/11.
 */

public class RingProgressBar extends View {

    private static final int DEFAULT_MIN_WIDTH = 400; //View默认最小宽度
    private float ringWidthPercent = 0.2f; //圆环宽度占View最大半径的百分比

    private Paint paint;
    private int curProgress = 0;
    private static final String CENT = "%";
    private StringBuilder progressStr;
    private float ringWidth = 12;
    private static final float minDegrees = 3.6f;
    private float width; //自定义view的宽度
    private float height; //自定义view的高度
    private static final int minMargin = 5;
    private float outerFrameRadius;
    private RectF rectF;
    private float textHeight;
    private float textWidth;
    private int backgroundColor = Color.GRAY;
    private int progressBarColor = Color.WHITE;
    private int textColor = Color.WHITE;

    public RingProgressBar(Context context) {
        super(context);
        init();
    }

    public RingProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        init();
    }

    public RingProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
        init();
    }

    private void initAttrs(Context context,AttributeSet attrs){
        TypedArray array =context.obtainStyledAttributes(attrs, R.styleable.RingProgressBarAttrs);
        backgroundColor = array.getColor(R.styleable.RingProgressBarAttrs_backgroundColor,Color.GRAY);
        progressBarColor = array.getColor(R.styleable.RingProgressBarAttrs_progressBarColor,Color.WHITE);
        textColor = array.getColor(R.styleable.RingProgressBarAttrs_textColor,Color.WHITE);
        float ringWidth = array.getFloat(R.styleable.RingProgressBarAttrs_ringWidthPercent,1f);
        ringWidthPercent *= ringWidth;
        array.recycle();
    }

    public void setProgress(int progress){
        this.curProgress = progress;
        if (curProgress > 100){
            curProgress = 100;
        }
        invalidate();
    }

    public void reset(){
        setProgress(0);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        progressStr = new StringBuilder();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(rectF == null) return;

        canvas.translate(width / 2, height / 2);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(ringWidth);
        paint.setColor(backgroundColor);
        canvas.drawArc(rectF,0, 360,false,paint);

        paint.setColor(progressBarColor);
        canvas.drawArc(rectF,0, minDegrees*curProgress,false,paint);

        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        progressStr.delete(0,progressStr.length());
        progressStr.append(curProgress).append(CENT);
        textWidth = paint.measureText(progressStr.toString());
        canvas.drawText(progressStr.toString(),-textWidth/2,textHeight/2,paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();

        outerFrameRadius = width/2 - minMargin;
        ringWidth = outerFrameRadius * ringWidthPercent;

        rectF = new RectF(-outerFrameRadius + ringWidth/2 - 1,-outerFrameRadius + ringWidth/2 - 1,
                outerFrameRadius - ringWidth/2 + 1,outerFrameRadius - ringWidth/2 + 1);
        paint.setTextSize((outerFrameRadius - ringWidth) * 3/4);
        Rect bounds = new Rect();
        progressStr.delete(0,progressStr.length());
        progressStr.append(curProgress).append(CENT);
        paint.getTextBounds(progressStr.toString(),0,progressStr.length(),bounds);
        textHeight = bounds.height();
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
}
