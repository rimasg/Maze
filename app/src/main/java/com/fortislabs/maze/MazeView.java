package com.fortislabs.maze;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.fortislabs.maze.utils.Constants;

/**
 * Created by Okis on 2015.05.08.
 */
public class MazeView extends View {
    private MazeGraph mazeGraph;
    private Paint tilePaint, bgPaint, txtPaint;
    private Bitmap tileBitmap;
    private Canvas tileCanvas;
    private int screenWidth, screeHeight;
    private int tileSize;
    private int cols, rows;

    private GestureDetector gestureDetector;
    private Context context;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screeHeight = h;

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawBitmap(tileBitmap, 0, 0, bgPaint);
        canvas.restore();
    }

    private void init() {
        setTileSize();

        mazeGraph = new MazeGraph(screenWidth, screeHeight, tileSize);
        cols = mazeGraph.getCols();
        rows = mazeGraph.getRows();

        initBitmap(screenWidth, screeHeight, tileSize);
        drawGrid(cols, rows, tileSize);


        setupGestureDetector();
    }

    private void setTileSize() {
        float displayScale = getResources().getDisplayMetrics().density;
        tileSize = (int) ((float) Constants.TILE_SIZE * displayScale);
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.d(Constants.TAG, "onDown: " + e.toString());
                setObstacle(e.getX(), e.getY());
                return true;
            }
        });

    }

    private void setObstacle(float x, float y) {
        int tileX = (int) (x / tileSize);
        int tileY = (int) (y / tileSize);

        if (tileX > 0 && tileX < cols - 1 && tileY > 0 && tileY < rows - 1) {
            mazeGraph.getNodesGrid()[tileX][tileY].obstacle = mazeGraph.getNodesGrid()[tileX][tileY].visited = !mazeGraph.getNodesGrid()[tileX][tileY].obstacle;
        }

        drawGrid(cols, rows, tileSize);
        invalidate();
    }

    public void drawGrid() {
        drawGrid(cols, rows, tileSize);
    }

    public void drawGrid(int cols, int rows, int tileSize) {
        initBitmap(screenWidth, screeHeight, tileSize);
        //
        tileCanvas.save();
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                drawTile(x, y, tileSize);
            }
        }
        tileCanvas.restore();
    }

    private void drawTile(int x, int y, int tileSize) {
        final boolean obstacle = mazeGraph.getNodesGrid()[x][y].obstacle;
        final boolean visited = mazeGraph.getNodesGrid()[x][y].visited;
        // Log.i(Constants.TAG, "X: " + x + " Y: " + y);
        int posX = x * tileSize;
        int posY = y * tileSize;
        final Resources res = getResources();
        tilePaint.setColor(res.getColor(R.color.brown));
        if (obstacle) {
            tilePaint.setStyle(Paint.Style.FILL);
        } else {
            tilePaint.setStyle(Paint.Style.STROKE);
        }

        tileCanvas.drawRect(posX, posY, posX + tileSize, posY + tileSize, tilePaint);

        drawSPath(posX, posY, tileSize, visited, obstacle);
        final String fCost = String.valueOf(mazeGraph.getNodesGrid()[x][y].getFCost());
        tileCanvas.drawText(fCost,
                posX + tileSize / 2 - txtPaint.measureText(fCost) / 2,
                posY + tileSize / 2 + txtPaint.getTextSize() / 2, txtPaint);
    }

    public void drawSPath(int posX, int posY, int tileSize, boolean visited, boolean obstacle) {
        if (visited && !obstacle) {
            final Resources res = getResources();
            tilePaint.setColor(res.getColor(R.color.olive));
            tilePaint.setStyle(Paint.Style.FILL);
            tileCanvas.drawCircle(posX + tileSize / 2, posY + tileSize / 2, tileSize / 2, tilePaint);
        }
    }

    private void initBitmap(int screenWidth, int screeHeight, int tileSize) {
        final Resources res = getResources();
        bgPaint = new Paint();
        bgPaint.setColor(res.getColor(R.color.gray));
        tilePaint = new Paint();
        tilePaint.setColor(res.getColor(R.color.orange));
        txtPaint = new Paint();
        txtPaint.setColor(Color.BLACK);
        txtPaint.setTextSize(tileSize * 0.6f);
        tileBitmap = Bitmap.createBitmap(screenWidth, screeHeight, Bitmap.Config.ARGB_8888);
        tileCanvas = new Canvas(tileBitmap);
    }

    public MazeGraph getMazeGraph() {
        return mazeGraph;
    }
}
