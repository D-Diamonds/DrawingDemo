package com.example.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawingView extends View{
    private Paint paint;
    private Path path;
    private ArrayList<float[]> rectangles = new ArrayList<>();
    private ArrayList<float[]> roundedRectangles = new ArrayList<>();
    private ArrayList<float[]> circles = new ArrayList<>();
    private ArrayList<float[]> lines = new ArrayList<>();
    private ArrayList<String> shapeRenderOrder = new ArrayList<>(); 
    private float x = -99999;
    private float y = -99999;
    private float x2 = -99999;
    private float y2 = -99999;
    private String drawType;
    private int color;
    private int toggleButtonColor;

    private final String DEFAULT_DRAWTYPE = "Rectangle";

    private void setpaint() {
        this.paint = new Paint();
        setColor(Color.GREEN);
        setStokeStyle("Square");
    }

    public void setDrawingType(String shape) {
        this.drawType = shape;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(this.color);
    }

    public void setToggleButtonColor(int color) {
        this.toggleButtonColor = color;
    }

    private void setDrawType(String type) {
        this.drawType = type;
    }

    public void undo() {
        if (this.shapeRenderOrder.size() > 0 ) {
            String lastDrawn = this.shapeRenderOrder.get(this.shapeRenderOrder.size() - 1);
            if (lastDrawn.equals("Rectangle"))
                this.rectangles.remove(this.rectangles.size() - 1);
            if (lastDrawn.equals("Rounded Rectangle"))
                this.roundedRectangles.remove(this.roundedRectangles.size() - 1);
            if (lastDrawn.equals("Circle"))
                this.circles.remove(this.circles.size() - 1);
            if (lastDrawn.equals("Line"))
                this.lines.remove(this.lines.size() - 1);
            this.shapeRenderOrder.remove(this.shapeRenderOrder.size() - 1);
            invalidate();
            System.out.println("Undid: " + lastDrawn);
            //setColor(findViewById(R.id.toggleButton.).getCol);
        }
    }

    private void setStokeStyle(String style) {
        if (style.equals("Rounded")) {
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(50);
        }
        else if (style.equals("Line")) {
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(10);
        }
        else {
            this.paint.setStrokeJoin(Paint.Join.MITER);
            this.paint.setStrokeCap(Paint.Cap.SQUARE);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setStrokeWidth(50);
        }
        //System.out.println("Style Changed: " + style);
    }

    public DrawingView(Context context) {
        super(context);
        setpaint();
        setDrawType(DEFAULT_DRAWTYPE);
        this.path = new Path();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setpaint();
        setDrawType(DEFAULT_DRAWTYPE);
        this.path = new Path();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setpaint();
        setDrawType(DEFAULT_DRAWTYPE);
        this.path = new Path();
    }


    private void resetSavedCoords() {
        this.x = -99999;
        this.y = -99999;
        this.x2 = -99999;
        this.y2 = -99999;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(this.path, this.paint);
        
        int rectCount = 0;
        int circCount = 0;
        int rrectCount = 0;
        int lineCount = 0;
        for(int i = 0; i < this.shapeRenderOrder.size(); i++) {
            if (this.shapeRenderOrder.get(i).equals("Rectangle")) {
                float[] rect = this.rectangles.get(rectCount);
                setColor((int) rect[4]);
                setStokeStyle("Square");
                canvas.drawRect(rect[0], rect[1], rect[2], rect[3], this.paint);
                rectCount++;
            }
            else if (this.shapeRenderOrder.get(i).equals("Rounded Rectangle")) {
                float[] rect = this.roundedRectangles.get(rrectCount);
                setColor((int) rect[4]);
                setStokeStyle("Rounded");
                canvas.drawRect(rect[0], rect[1], rect[2], rect[3], this.paint);
                rrectCount++;
            }
            else if (this.shapeRenderOrder.get(i).equals("Circle")) {
                float[] circ = this.circles.get(circCount);
                setColor((int) circ[4]);
                setStokeStyle("Circle");
                canvas.drawCircle(circ[0], circ[1], (float) Math.sqrt(Math.pow((circ[0] - circ[2]), 2) + Math.pow((circ[1] - circ[3]), 2)), this.paint);
                circCount++;
            }
            else if (this.shapeRenderOrder.get(i).equals("Line")) {
                float[] line = this.lines.get(lineCount);
                setColor((int) line[4]);
                setStokeStyle("Rounded");
                canvas.drawLine(line[0], line[1], line[2], line[3], this.paint);
                lineCount++;
            }
        }

//        for (float[] coords : this.rectangles) {
//            this.paint.setColor((int) coords[4]);
//            canvas.drawRect(coords[0], coords[1], coords[2], coords[3], this.paint);
//        }
//        for (float[] coords : this.circles) {
//            this.paint.setColor((int) coords[4]);
//            canvas.drawCircle(coords[0], coords[1], (float) Math.sqrt(Math.pow((coords[0] - coords[2]), 2) + Math.pow((coords[1] - coords[3]), 2)), this.paint);
//        }
        setColor(this.toggleButtonColor);

        resetSavedCoords();
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (x == -99999 && !this.drawType.equals("Path")) {
                this.x = event.getX();
                this.y = event.getY();
            }
            else if (!this.drawType.equals("Path")){
                this.x2 = event.getX();
                this.y2 = event.getY();
                if (this.drawType.equals("Rectangle"))
                    this.rectangles.add(new float[]{this.x, this.y, this.x2, this.y2, this.paint.getColor()});
                else if (this.drawType.equals("Circle"))
                    this.circles.add(new float[]{this.x, this.y, this.x2, this.y2, this.paint.getColor()});
                else if (this.drawType.equals("Rounded Rectangle"))
                    this.roundedRectangles.add(new float[]{this.x, this.y, this.x2, this.y2, this.paint.getColor()});
                else if (this.drawType.equals("Line"))
                    this.lines.add(new float[]{this.x, this.y, this.x2, this.y2, this.paint.getColor()});
                this.shapeRenderOrder.add(this.drawType);
                System.out.println("Added new shape: " + this.drawType);
                System.out.println("Color used: " + this.color);
                invalidate();
            }
            else
                path.moveTo(event.getX(), event.getY());
        }
        else if (action == MotionEvent.ACTION_MOVE && this.drawType.equals("Path")) {
            path.lineTo(event.getX(), event.getY());
            invalidate();
        }
        return true;
    }
}
