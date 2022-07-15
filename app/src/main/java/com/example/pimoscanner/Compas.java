package com.example.pimoscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class Compas extends View {

    private final Paint mPaint;
    private final Bitmap mBitmap;
    private final Matrix mMatrix;
    // Used for orientation of the compass
    private float mAngle = 0;

    public Compas(Context context) {
        super(context);

        mPaint = new Paint();
        mMatrix = new Matrix();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eaf_compass);
    }


    /**
     * Updates the angle, in degrees, at which the compass is draw within this
     * view.
     */
    public void setRotationAngle(double angle) {
        // Save the new rotation angle.
        mAngle = (float) angle;
        // Force the compass to re-paint itself.
        postInvalidate();
    }

    /**
     * Draws the compass image at the current angle of rotation on the canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Reset the matrix to default values.
        mMatrix.reset();

        // Pass the current rotation angle to the matrix. The center of rotation
        // is set to be the center of the bitmap.
        mMatrix.postRotate(-mAngle, mBitmap.getHeight() / 2, mBitmap.getWidth() / 2);

        // Use the matrix to draw the bitmap image of the compass.
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);

        super.onDraw(canvas);
    }
}
