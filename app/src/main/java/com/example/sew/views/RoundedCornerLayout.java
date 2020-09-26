package com.example.sew.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sew.R;

public class RoundedCornerLayout extends LinearLayout {
    private Bitmap maskBitmap;
    private Paint paint;
    private float cornerRadius;
    int outerBackgroundColor;

    public RoundedCornerLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornerLayout);
            try {
                outerBackgroundColor = a.getColor(R.styleable.RoundedCornerLayout_outerBackground, getResources().getColor(R.color.md_grey_50));
            } catch (Exception ignored) {
            }
        }
        if (outerBackgroundColor == 0)
            outerBackgroundColor = getResources().getColor(R.color.md_grey_50);
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (maskBitmap == null) {
            // This corner radius assumes the image width == height and you want it to be circular
            // Otherwise, customize the radius as needed
            cornerRadius = canvas.getHeight() / 2;
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }

        canvas.drawBitmap(maskBitmap, 0f, 0f, paint);
    }

    private Bitmap createMask(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(outerBackgroundColor); // Set Mask Color Here

        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);

        int borderWidth = 2; //Set Border Width Here
        int withMinusable = borderWidth / 2;
        Paint border = new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(borderWidth);
        border.setColor(getResources().getColor(R.color.divider_color)); //Set Border Color here
        canvas.drawRoundRect(new RectF(withMinusable, withMinusable, width - withMinusable, height - withMinusable), cornerRadius, cornerRadius, border);

        return mask;
    }
}