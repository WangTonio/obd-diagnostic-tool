package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.Gauge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.R;

public final class Gauge extends View{

    private static final String TAG = "Gauge";
    private static final int colors[] =  {Color.BLUE,Color.RED};

    private Handler handler;

    // drawing tools
    private RectF rimRect;
    private Paint rimPaint;
    private Paint rimCirclePaint;

    private RectF faceRect;
    private Bitmap faceTexture;
    private Paint facePaint;
    private Paint rimShadowPaint;

    private Paint scalePaint;
    private RectF scaleRect;

    private Paint handPaint;
    private Path handPath;
    private Paint handScrewPaint;

    private Paint backgroundPaint;

    private Bitmap background; // holds the cached static part

    private int totalNicks;
    private int degreeRange;
    private float degreesPerNick;
     // the one in the top center (12 o'clock)
    private int minDegrees;
    private int maxDegrees;
    private int minVal;
    private int maxVal;
    private float stride;
    private boolean isGradientTemp;

    // hand dynamics -- all are angular expressed in F degrees
    private int centerDegree        = 0;
    private boolean handInitialized = false;
    private float handPosition      = -110;
    private float handTarget        = centerDegree;
    private float handVelocity      = 0.0f;
    private float handAcceleration  = 0.0f;
    private long lastHandMoveTime   = -1L;


    public Gauge(Context context) {
        super(context);
        init();
    }

    public Gauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        grabCustomAttributes(context, attrs);
        init();
    }

    public Gauge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        grabCustomAttributes(context, attrs);
        init();
    }

    public void grabCustomAttributes(Context context, AttributeSet attrs){
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.Gauge);
        try{
            minDegrees = arr.getInteger(R.styleable.Gauge_minDeg,0);
            maxDegrees = arr.getInteger(R.styleable.Gauge_maxDeg,0);
            minVal = arr.getInteger(R.styleable.Gauge_minVal,0);
            maxVal = arr.getInteger(R.styleable.Gauge_maxVal,0);
            totalNicks = arr.getInteger(R.styleable.Gauge_totalTicks,0);
            stride = arr.getFloat(R.styleable.Gauge_stride,0.0f);
            isGradientTemp =  arr.getBoolean(R.styleable.Gauge_tempGradient,false);
            degreeRange = maxDegrees - minDegrees;
            degreesPerNick = (float)degreeRange / totalNicks;
        } catch (Exception ex) {

        } finally {
            arr.recycle();
        }
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);

        handInitialized = bundle.getBoolean("handInitialized");
        handPosition = bundle.getFloat("handPosition");
        handTarget = bundle.getFloat("handTarget");
        handVelocity = bundle.getFloat("handVelocity");
        handAcceleration = bundle.getFloat("handAcceleration");
        lastHandMoveTime = bundle.getLong("lastHandMoveTime");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("handInitialized", handInitialized);
        state.putFloat("handPosition", handPosition);
        state.putFloat("handTarget", handTarget);
        state.putFloat("handVelocity", handVelocity);
        state.putFloat("handAcceleration", handAcceleration);
        state.putLong("lastHandMoveTime", lastHandMoveTime);
        return state;
    }

    private void init() {
        handler = new Handler();
        setLayerType(Gauge.LAYER_TYPE_SOFTWARE, null);
        initDrawingTools();
    }


    private void initDrawingTools() {

        rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        // the linear gradient is a bit skewed for realism
        rimPaint = new Paint();
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f,
                Color.rgb(0xf0, 0xf5, 0xf0),
                Color.rgb(0x30, 0x31, 0x30),
                Shader.TileMode.CLAMP));

        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.STROKE);
        rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
        rimCirclePaint.setStrokeWidth(0.005f);

        float rimSize = 0.02f;
        faceRect = new RectF();
        faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
                rimRect.right - rimSize, rimRect.bottom - rimSize);

        faceTexture = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.gaugebackground2);
        BitmapShader paperShader = new BitmapShader(faceTexture,
                Shader.TileMode.MIRROR,
                Shader.TileMode.MIRROR);
        Matrix paperMatrix = new Matrix();
        facePaint = new Paint();
        facePaint.setFilterBitmap(true);
        paperMatrix.setScale(1.0f / faceTexture.getWidth(),
                1.0f / faceTexture.getHeight());
        paperShader.setLocalMatrix(paperMatrix);
        facePaint.setStyle(Paint.Style.FILL);
        facePaint.setShader(paperShader);

        rimShadowPaint = new Paint();
        rimShadowPaint.setShader(new RadialGradient(0.5f, 0.5f, faceRect.width() / 2.0f,
                new int[] { 0x00000000, 0x00000500, 0x50000500 },
                new float[] { 0.96f, 0.96f, 0.99f },
                Shader.TileMode.MIRROR));
        rimShadowPaint.setStyle(Paint.Style.FILL);

        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(Color.WHITE);
        scalePaint.setStrokeWidth(0.005f);
        scalePaint.setAntiAlias(true);
        if( isGradientTemp ) scalePaint.setShader(new RadialGradient(0.7f,1.7f,faceRect.width()/2,colors,null,Shader.TileMode.CLAMP));
        scalePaint.setTextSize(0.045f);
        scalePaint.setLinearText(true);
        scalePaint.setTypeface(Typeface.SANS_SERIF);
        scalePaint.setTextScaleX(0.8f);

        scalePaint.setTextAlign(Paint.Align.CENTER);

        float scalePosition = 0.11f;
        scaleRect = new RectF();
        scaleRect.set(faceRect.left + scalePosition, faceRect.top + scalePosition,
                faceRect.right - scalePosition, faceRect.bottom - scalePosition);

        handPaint = new Paint();
        handPaint.setAntiAlias(true);
        handPaint.setColor(Color.rgb(255,102,0));
        handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
        handPaint.setStyle(Paint.Style.FILL);

        handPath = new Path();
        handPath.moveTo(0.5f, 0.5f + 0.2f);
        handPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);
        handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
        handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
        handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
        handPath.lineTo(0.5f, 0.5f + 0.2f);
        handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);

        handScrewPaint = new Paint();
        handScrewPaint.setAntiAlias(true);
        handScrewPaint.setColor(0xff493f3c);
        handScrewPaint.setStyle(Paint.Style.FILL);

        setHandTarget(minVal);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);
    }

    public int getMaxVal() {
        return this.maxVal;
    }

    public int getMinDegrees() {
        return minDegrees;
    }

    public int getMaxDegrees() {
        return maxDegrees;
    }

    public int getMinVal() {
        return minVal;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
        Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    private int chooseDimension(int mode, int size) {

        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            return getPreferredSize();
        }
    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 300;
    }

    private void drawRim(Canvas canvas) {
        // first, draw the metallic body
        canvas.drawOval(rimRect, rimPaint);
        // now the outer rim circle
        canvas.drawOval(rimRect, rimCirclePaint);
    }

    private void drawFace(Canvas canvas) {;
        canvas.drawOval(faceRect, facePaint);
        // draw the inner rim circle
        canvas.drawOval(faceRect, rimCirclePaint);
        // draw the rim shadow inside the face
        canvas.drawOval(faceRect, rimShadowPaint);
    }

    private void drawScale(Canvas canvas) {
        float start = minVal;
        String valueString;
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(minDegrees,0.5f,0.5f);
        for (int i = 0; i < totalNicks+1; ++i) {
            float y1 = scaleRect.top;
            float y3 = y1 - 0.045f;
            if (i%2 != 0){
                canvas.drawLine(0.5f, y1, 0.5f, y3+.025f, scalePaint);
            }else {
                canvas.drawLine(0.5f, y1, 0.5f, y3, scalePaint);
                if(minVal == 11.0){
                    valueString = Float.toString(start);
                }else{
                    valueString = Integer.toString((int)start);
                }

                canvas.drawText(valueString, 0.5f, y3 - 0.015f, scalePaint);
                start+=stride;
            }
            canvas.rotate(degreesPerNick, 0.5f, 0.5f);

        }
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {

        if (background == null) {
            Log.w(TAG, "Background not created");
        } else {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "Size changed to " + w + "x" + h);

        regenerateBackground();
    }

    private void regenerateBackground() {

        // free the old bitmap
        if (background != null) {
            background.recycle();
        }

        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);
        float scale = (float) getWidth();
        backgroundCanvas.scale(scale, scale);

        drawRim(backgroundCanvas);
        drawFace(backgroundCanvas);
        drawScale(backgroundCanvas);

    }

    private boolean handNeedsToMove() {

        return Math.abs(handPosition - handTarget) > 0.01f;
    }

    private void drawHand(Canvas canvas) {
        Log.d(TAG,"drawHand");
        if (handInitialized) {
            Log.d(TAG," " + handPosition);
            float handAngle = handPosition /*degreeToAngle(handPosition)*/;
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(handAngle, 0.5f, 0.5f);
            canvas.drawPath(handPath, handPaint);
            canvas.restore();
            canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG,"onDraw");
        drawBackground(canvas);

        float scale = (float) getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);

        drawHand(canvas);
        canvas.restore();

        if (handNeedsToMove()) {
            moveHand();
        }
    }

    private void moveHand() {

        if (! handNeedsToMove()) {
            return;
        }

        if (lastHandMoveTime != -1L) {
            long currentTime = System.currentTimeMillis();
            float delta = (currentTime - lastHandMoveTime) / 1000.0f;

            float direction = Math.signum(handVelocity);
            if (Math.abs(handVelocity) < 90.0f) {
                handAcceleration = 5.0f * (handTarget - handPosition);
            } else {
                handAcceleration = 0.0f;
            }
            handPosition += handVelocity * delta;
            handVelocity += handAcceleration * delta;
            if ((handTarget - handPosition) * direction < 0.01f * direction) {
                handPosition = handTarget;
                handVelocity = 0.0f;
                handAcceleration = 0.0f;
                lastHandMoveTime = -1L;
            } else {
                lastHandMoveTime = System.currentTimeMillis();
            }
            invalidate();
        } else {
            lastHandMoveTime = System.currentTimeMillis();
            moveHand();
        }

    }

    public void setHandTarget(float temperature) {
        handTarget = valueToDegree(temperature);
        handInitialized = true;
        invalidate();
    }

    private float valueToDegree(float x){
        if(x <= minVal) return minDegrees;
        if(x >= maxVal) return maxDegrees;
        return (((x - minVal) * (maxDegrees - minDegrees)) / (maxVal - minVal)) + minDegrees;
    }
}
