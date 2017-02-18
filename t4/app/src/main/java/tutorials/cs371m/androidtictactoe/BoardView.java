package tutorials.cs371m.androidtictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class BoardView extends View {
    // Width of the board grid lines
    public static final int GRID_LINE_WIDTH = 6;

    private Bitmap mHumanBitmap;
    private Bitmap mComputerBitmap;

    private Paint mPaint;

    private TicTacToeGame mGame;


        // most important constructor for a custom View
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    // optional constructors, generally not needed
    public BoardView(Context context) {
        super(context);
        initialize();
    }
    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void setGame(TicTacToeGame game) {

        mGame = game;
    }

    public void initialize() {
        mHumanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.human);
        mComputerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Make thick, light gray lines
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(GRID_LINE_WIDTH);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.d("onDraw", "1");
        super.onDraw(canvas);
        int boardWidth = getWidth();
        int boardHeight = getHeight();

        canvas.drawLine( (float)(boardWidth*0.33), (float)(boardHeight*0.0), (float)(boardWidth*0.33), (float)(boardHeight*1.0), mPaint);
        canvas.drawLine( (float)(boardWidth*0.66), (float)(boardHeight*0.0), (float)(boardWidth*0.66), (float)(boardHeight*1.0), mPaint);

        canvas.drawLine( (float)(boardWidth*0.0), (float)(boardHeight*0.33), (float)(boardWidth*1.0), (float)(boardHeight*0.33), mPaint);
        canvas.drawLine( (float)(boardWidth*0.0), (float)(boardHeight*0.66), (float)(boardWidth*1.0), (float)(boardHeight*0.66), mPaint);

        Rect drawingRect = new Rect();

        // Draw all the X and O images
        for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
            int col = i % 3;
            int row = i / 3;


            // Define the boundaries of a destination rectangle for the image
            drawingRect.left = col*(boardWidth/3) + (boardWidth/30);
            drawingRect.top = row*(boardHeight/3) + (boardHeight/30);
            drawingRect.right = (col)*(boardWidth/3) + boardWidth/4;
            drawingRect.bottom = (row)*(boardHeight/3) + boardHeight/4;

            if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.HUMAN_PLAYER) {
                //Log.d("mHumanBitmap", "col = " + col + " ,row = " + row);
                canvas.drawBitmap(mHumanBitmap, null, drawingRect, null);
            }
            else if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.COMPUTER_PLAYER) {
                //Log.d("mComputerBitmap", "col = " + col + " ,row = " + row);
                canvas.drawBitmap(mComputerBitmap, null, drawingRect, null);
            }
        }
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight()); setMeasuredDimension(size, size);
    }
    public int getBoardCellWidth() {
        return getWidth() / 3;
    }
    public int getBoardCellHeight() {
        return getHeight() / 3;
    }


}
