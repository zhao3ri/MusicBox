package com.tyland.musicbox.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tyland.musicbox.IMusicService;
import com.tyland.musicbox.R;
import com.tyland.musicbox.model.Lrc;
import com.tyland.musicbox.util.Utils;

/**
 * Created by tyland on 2018/5/14.
 */
public class LrcView extends View {
    private Lrc lrc;
    private Paint gPaint;
    private Paint hPaint;
    private int width = 0;
    private int height = 0;
    private int currentPosition = 0;
    private IMusicService service;
    private int lastPosition = 0;
    private int lightColor;
    private int darkColor;
    private int mode = DEFAULT;
    public final static int DEFAULT = 0;
    public final static int KARAOKE = 1;

    public void setLightColor(int lightColor) {
        this.lightColor = lightColor;
    }

    public void setDarkColor(int darkColor) {
        this.darkColor = darkColor;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setService(IMusicService service) {
        this.service = service;
    }

    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
    }

    public LrcView(Context context) {
        this(context, null);
    }

    public LrcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LrcView);
        lightColor = ta.getColor(R.styleable.LrcView_lightColor, getResources().getColor(R.color.color_playing_lrc));
        darkColor = ta.getColor(R.styleable.LrcView_darkColor, getResources().getColor(android.R.color.white));
        mode = ta.getInt(R.styleable.LrcView_lrcMode, DEFAULT);
        ta.recycle();
        gPaint = new Paint();
        gPaint.setAntiAlias(true);
        gPaint.setColor(darkColor);
        gPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_music_lrc));
        gPaint.setTextAlign(Paint.Align.CENTER);
        hPaint = new Paint();
        hPaint.setAntiAlias(true);
        hPaint.setColor(lightColor);
        hPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_music_lrc));
        hPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (lrc == null || lrc.getLrcBeen().isEmpty()) {
            canvas.drawText(getResources().getString(R.string.no_lyrics), width / 2, height / 2, gPaint);
            return;
        }

        getCurrentPosition();

//        drawLrc1(canvas);
        int currentMillis = service.getCurrentPlayPosition();
        drawLrc2(canvas, currentMillis);
        long start = lrc.getLrcBeen().get(currentPosition).getStart();
        float v = (currentMillis - start) > 500 ? currentPosition * 80 : lastPosition * 80 + (currentPosition - lastPosition) * 80 * ((currentMillis - start) / 500f);
        setScrollY((int) v);
        if (getScrollY() == currentPosition * 80) {
            lastPosition = currentPosition;
        }
        postInvalidateDelayed(100);
    }

    private void drawLrc2(Canvas canvas, int currentMillis) {
        if (mode == DEFAULT) {
            for (int i = 0; i < lrc.getLrcBeen().size(); i++) {
                if (i == currentPosition) {
                    canvas.drawText(lrc.getLrcBeen().get(i).getLrc(), width / 2, height / 2 + 80 * i, hPaint);
                } else {
                    canvas.drawText(lrc.getLrcBeen().get(i).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
                }
            }
        } else {
            for (int i = 0; i < lrc.getLrcBeen().size(); i++) {
                canvas.drawText(lrc.getLrcBeen().get(i).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
            }
            String highLineLrc = lrc.getLrcBeen().get(currentPosition).getLrc();
            int highLineWidth = (int) gPaint.measureText(highLineLrc);
            int leftOffset = (width - highLineWidth) / 2;
            Lrc.LrcBean lrcBean = lrc.getLrcBeen().get(currentPosition);
            long start = lrcBean.getStart();
            long end = lrcBean.getEnd();
            int i = (int) ((currentMillis - start) * 1.0f / (end - start) * highLineWidth);
            if (i > 0) {
                Bitmap textBitmap = Bitmap.createBitmap(i, 80, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(highLineLrc, highLineWidth / 2, 80, hPaint);
                canvas.drawBitmap(textBitmap, leftOffset, height / 2 + 80 * (currentPosition - 1), null);
            }
        }
    }

    public void init() {
        currentPosition = 0;
        lastPosition = 0;
        setScrollY(0);
        invalidate();
    }

    private void drawLrc1(Canvas canvas) {
        String text = lrc.getLrcBeen().get(currentPosition).getLrc();
        canvas.drawText(text, width / 2, height / 2, hPaint);

        for (int i = 1; i < 10; i++) {
            int index = currentPosition - i;
            if (index > -1) {
                canvas.drawText(lrc.getLrcBeen().get(index).getLrc(), width / 2, height / 2 - 80 * i, gPaint);
            }
        }
        for (int i = 1; i < 10; i++) {
            int index = currentPosition + i;
            if (index < lrc.getLrcBeen().size()) {
                canvas.drawText(lrc.getLrcBeen().get(index).getLrc(), width / 2, height / 2 + 80 * i, gPaint);
            }
        }
    }

    private void getCurrentPosition() {
        try {
            int currentMillis = service.getCurrentPlayPosition();
            if (currentMillis < lrc.getLrcBeen().get(0).getStart()) {
                currentPosition = 0;
                return;
            }
            if (currentMillis > lrc.getLrcBeen().get(lrc.getLrcBeen().size() - 1).getStart()) {
                currentPosition = lrc.getLrcBeen().size() - 1;
                return;
            }
            for (int i = 0; i < lrc.getLrcBeen().size(); i++) {
                if (currentMillis >= lrc.getLrcBeen().get(i).getStart() && currentMillis < lrc.getLrcBeen().get(i).getEnd()) {
                    currentPosition = i;
                    return;
                }
            }
        } catch (Exception e) {
            postInvalidateDelayed(100);
        }
    }
}
