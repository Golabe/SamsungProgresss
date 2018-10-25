package top.golabe.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SamsungProgressView extends View {
    private static final String TAG = "SamsungProgressView";
    private static final int DEFAULT_SIZE = 100;
    private int max;
    private int min;
    private int progress;
    private float border;
    private int bgColor;
    private int progressColor;
    private int progressBgColor;
    private float textSize;
    private int textColor;
    private Point mCenterPoint;
    private String suffix;
    private Context mContext;
    private int mW;
    private int mH;
    private int mPadding;

    private Paint mTextPaint;
    private Paint mProgressPaint;
    private Paint mConfirmBgPaint;
    private int mRadius;
    private Path mProgressBgPath;
    private RectF mRectF;
    private int mTotalProgress;
    private int rightAlpha = 0;
    private int circleProgress = 0;
    private int duration;


    public SamsungProgressView(Context context) {
        this(context, null);
    }

    public SamsungProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SamsungProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        init();
    }


    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SamsungProgressView);
            progress = a.getInteger(R.styleable.SamsungProgressView_samsung_progress, 0);
            max = a.getInteger(R.styleable.SamsungProgressView_samsung_max, 100);
            min = a.getInteger(R.styleable.SamsungProgressView_samsung_min, 0);
            border = a.getDimension(R.styleable.SamsungProgressView_samsung_border, 6);
            bgColor = a.getColor(R.styleable.SamsungProgressView_samsung_bg_color, Color.parseColor("#FFFFFF"));
            progressColor = a.getColor(R.styleable.SamsungProgressView_samsung_progress_color, Color.parseColor("#ff8661"));
            progressBgColor = a.getColor(R.styleable.SamsungProgressView_samsung_progress_bg_color, Color.parseColor("#eddad7"));
            textColor = a.getColor(R.styleable.SamsungProgressView_samsung_text_color, Color.parseColor("#ff8661"));
            textSize = a.getDimension(R.styleable.SamsungProgressView_samsung_text_size, 16);
            suffix = a.getString(R.styleable.SamsungProgressView_samsung_suffix);
            duration=a.getInteger(R.styleable.SamsungProgressView_samsung_duration,300);
            a.recycle();
        }
        checkProgress();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(sp2px(textSize));

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(progressBgColor);
        mProgressPaint.setStrokeWidth(dp2px(border / 6));
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mConfirmBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mConfirmBgPaint.setStyle(Paint.Style.FILL);
        mConfirmBgPaint.setColor(progressColor);

        mProgressBgPath = new Path();
        mCenterPoint = new Point();
        mRectF = new RectF();
        mTotalProgress = Math.abs( min)+Math.abs(max);
        Log.d(TAG, "init: "+mTotalProgress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wModel = MeasureSpec.getMode(widthMeasureSpec);
        int hModel = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int minSize = Math.min(widthSize, heightSize);
        if (wModel == MeasureSpec.EXACTLY && hModel == MeasureSpec.EXACTLY) {
            setMeasuredDimension(minSize, minSize);
        } else {
            setMeasuredDimension(dp2px(DEFAULT_SIZE), dp2px(DEFAULT_SIZE));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mProgressBgPath.reset();
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        mW = w;
        mH = h;
        mPadding = w / 16;
        mRadius = w / 2 - mPadding;
        mRectF.set(mPadding, mPadding, w - mPadding, h - mPadding);
        mProgressBgPath.addCircle(mCenterPoint.x, mCenterPoint.y, mRadius, Path.Direction.CW);
    }

    private boolean isAnimEnd = false;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressBg(canvas);
        drawText(canvas);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, circleProgress, mConfirmBgPaint);
        if (progress == 100 && !isAnimEnd) {

            ObjectAnimator circleAnimator = ObjectAnimator.ofInt(this, "circleProgress", 0, mRadius);
            ObjectAnimator rightAnimator = ObjectAnimator.ofInt(this, "rightAlpha", 0, 255);
            circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    isAnimEnd = true;
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(circleAnimator, rightAnimator);
            animatorSet.setDuration(duration);
            animatorSet.start();

        }
        if (isAnimEnd) {
            mTextPaint.setColor(Color.parseColor("#FFFFFF"));
            drawText(canvas);
            mTextPaint.setColor(progressColor);
            reset();
        }

    }



    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(suffix)) {
            suffix = "%";
        }
        String str2 = progress + suffix;
        float x = getWidth() / 2 - mTextPaint.measureText(str2) / 2;
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float y = getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2;
        canvas.drawText(str2, x, y, mTextPaint);

    }

    private void drawProgressBg(Canvas canvas) {

        canvas.save();
        canvas.clipPath(mProgressBgPath, Region.Op.UNION);
        canvas.drawPath(mProgressBgPath, mProgressPaint);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStrokeWidth(border);
        canvas.drawArc(mRectF, -90, 360F/mTotalProgress*mTotalProgress/100F*progress, false, mProgressPaint);
        canvas.restore();
        //画笔重置为进度条背景样式
        mProgressPaint.setColor(progressBgColor);
        mProgressPaint.setStrokeWidth(dp2px(border / 6));
    }

    private int dp2px(float dp) {
        float v = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * v + 0.5F);
    }

    private int sp2px(float sp) {
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale * sp + 0.5F);
    }

    private void checkProgress() {
        if (progress < 0) {
            this.progress = 0;
        } else if (progress > 100) {
            this.progress = 100;
        }
    }
    private void reset() {
        circleProgress = 0;
        rightAlpha = 0;
        isAnimEnd = false;

    }

    public int getRightAlpha() {
        return rightAlpha;
    }

    private void setRightAlpha(int rightAlpha) {
        this.rightAlpha = rightAlpha;
        invalidate();
    }

    public int getCircleProgress() {
        return circleProgress;
    }

    private void setCircleProgress(int circleProgress) {
        this.circleProgress = circleProgress;
        invalidate();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        checkProgress();
        invalidate();
    }

    public float getBorder() {
        return border;
    }

    public void setBorder(float border) {
        this.border = border;
        invalidate();

    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public int getProgressBgColor() {
        return progressBgColor;
    }

    public void setProgressBgColor(int progressBgColor) {
        this.progressBgColor = progressBgColor;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        invalidate();
    }
}
