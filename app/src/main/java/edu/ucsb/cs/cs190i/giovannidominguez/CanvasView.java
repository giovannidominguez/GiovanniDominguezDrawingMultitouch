package edu.ucsb.cs.cs190i.giovannidominguez;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.RectF;
import java.util.Random;
import java.util.ArrayList;


/**
 * Created by Giovanni on 4/21/2017.
 */

public class CanvasView extends View {
    public static final int maxFingers = 5;

    private Path[] currFingerPaths = new Path[maxFingers];
    private Paint touchDraw;
    private ArrayList<Path> completedPaths;
    //private ArrayList <Path> myPath;
    //private ArrayList <Paint> myPaint;
    //private float mX, mY;
    //private static final float TOLERANCE = 5;
    Context context;
    private ArrayList <Color> myColors;
    private int sizeStroke = 0;
    private RectF pathBounds = new RectF();


    public CanvasView(Context context) {
        super(context);
        this.context = context;
    }

    public CanvasView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        completedPaths = new ArrayList<Path>();
        touchDraw = new Paint();
        touchDraw.setAntiAlias(true);
        Random rnd = new Random();
        int randomColor = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        touchDraw.setColor(randomColor);
        touchDraw.setStyle(Paint.Style.STROKE);
        touchDraw.setStrokeWidth(sizeStroke);
        touchDraw.setStrokeCap(Paint.Cap.BUTT);
        //myColors.add(Color.BLACK);
        //colors = {Color.rgb(255, 0, 0), Color.rgb(0,255,0), Color.rgb(255, 102, 255), Color.rgb(0, 0, 255), Color.rgb(0, 0, 0)};


    }

    private void addPath(){

        touchDraw = new Paint();
        touchDraw.setAntiAlias(true);
        touchDraw.setAntiAlias(true);
        Random rnd = new Random();
        int randomColor = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        touchDraw.setColor(randomColor);
        touchDraw.setStyle(Paint.Style.STROKE);
        touchDraw.setStrokeWidth(sizeStroke);
        touchDraw.setStrokeCap(Paint.Cap.BUTT);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void changeSize(int progress){
        sizeStroke = progress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Path completedPath : completedPaths) {
            canvas.drawPath(completedPath, touchDraw);
        }

        for (Path fingerPath : currFingerPaths) {
            if (fingerPath != null) {
                canvas.drawPath(fingerPath, touchDraw);
            }
        }
    }

    public void clearCanvas(){
        for (int i = 0; i < completedPaths.size(); ++i) {
            completedPaths.get(i).reset();
        }

        invalidate();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
            int pointerCount = event.getPointerCount();
            int maxPointerCount = pointerCount > maxFingers ? maxFingers : pointerCount;
            int actionIndex = event.getActionIndex();
            int action = event.getActionMasked();
            int id = event.getPointerId(actionIndex);

            if ((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) && id < maxFingers) {
                currFingerPaths[id] = new Path();
                currFingerPaths[id].moveTo(event.getX(actionIndex), event.getY(actionIndex));
            } else if ((action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) && id < maxFingers) {
                currFingerPaths[id].setLastPoint(event.getX(actionIndex), event.getY(actionIndex));
                this.addPath();
                completedPaths.add(currFingerPaths[id]);
                currFingerPaths[id].computeBounds(pathBounds, true);
                invalidate((int) pathBounds.left, (int) pathBounds.top,
                        (int) pathBounds.right, (int) pathBounds.bottom);
                currFingerPaths[id] = null;
            }

            for(int i = 0; i < maxPointerCount; i++) {
                if(currFingerPaths[i] != null) {
                    int index = event.findPointerIndex(i);
                    currFingerPaths[i].lineTo(event.getX(index), event.getY(index));
                    currFingerPaths[i].computeBounds(pathBounds, true);
                    invalidate((int) pathBounds.left, (int) pathBounds.top,
                            (int) pathBounds.right, (int) pathBounds.bottom);
                }
            }

            return true;
        }

}














