package almto3.androidtictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AndroidTicTacToe extends AppCompatActivity {

    private TicTacToeGame mGame;

    private Button mBoardButtons[];

    private TextView mInfoTextView;

    private TextView mResultHuman;
    private TextView mResultTie;
    private TextView mResultAndroid;

    private Button new_game;
    private Button reset;

    private boolean mGameOver;

    private int human;
    private int tie;
    private int android;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_tic_tac_toe);

        mInfoTextView = (TextView) findViewById(R.id.information);

        mResultHuman = (TextView) findViewById(R.id.result_human);
        mResultTie = (TextView) findViewById(R.id.result_tie);
        mResultAndroid = (TextView) findViewById(R.id.result_android);

        human = 0;
        tie = 0;
        android = 0;

        startNewGame();
    }

    // Set up the game board.
    private void startNewGame()
    {
        int winner;
        if (mGame != null)
            winner = mGame.checkForWinner();
        else
            winner = 0;
        if (winner == 1)
            tie ++;
        else if (winner == 2)
            human ++;
        else if (winner == 3)
            android++;

        mGameOver = false;

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);


        new_game = (Button) findViewById(R.id.new_game);
        reset = (Button) findViewById(R.id.reset);

        mGame = new TicTacToeGame();

        mGame.clearBoard();

        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        new_game.setOnClickListener(new ButtonClickListener2());

        mInfoTextView.setText(R.string.human_first);
        mResultHuman.setText(getString(R.string.human, human));
        mResultTie.setText(getString(R.string.tie, tie));
        mResultAndroid.setText(getString(R.string.android, android));
    }

    private void resetStats(){
        human = 0;
        tie = 0;
        android = 0;
        mResultHuman.setText(getString(R.string.human, human));
        mResultTie.setText(getString(R.string.tie, tie));
        mResultAndroid.setText(getString(R.string.android, android));
    }
    private void setMove(char player, int location) {
        String TAG = "setMove";
        if (mGameOver)
            return;
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        Log.d(TAG, "player is " + player);
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(getResources().getColor(R.color.xPieceColor));
        else
            mBoardButtons[location].setTextColor(getResources().getColor(R.color.oPieceColor));
    }

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            String TAG = "onClick";

            if (mBoardButtons[location].isEnabled()) {
                Log.d(TAG, location + " - Enabled");
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
                Log.d(TAG, "setMove called");
                int winner = mGame.checkForWinner();
                Log.d(TAG, "setMove called2");
                if (winner == 0) {
                    mInfoTextView.setText(R.string.android_turn);
                    int move = mGame.getComputerMove();
                    Log.d(TAG, "move = " + move);
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                Log.d(TAG, "setMove called3");
                mGameOver = true;
                if (winner == 0) {
                    mInfoTextView.setText(R.string.human_turn);
                    mGameOver = false;
                }
                else if (winner == 1)
                    mInfoTextView.setText(R.string.result_tie);

                else if (winner == 2)
                    mInfoTextView.setText(R.string.result_human_wins);

                else
                    mInfoTextView.setText(R.string.result_android_wins);
            }
            else
                Log.d(TAG, location + " - NOT ENABLED");
        }
    }

    private class ButtonClickListener2 implements View.OnClickListener {
        String TAG = "ButtonClickListener2";
        public ButtonClickListener2() {
            Log.d(TAG, "constructor");
        }
        public void onClick(View view) {
            Log.d(TAG, "onClick2");

            startNewGame();
            Log.d(TAG, "board cleared");
        }
    }

    private class ButtonClickListener3 implements View.OnClickListener {
        String TAG = "ButtonClickListener3";
        public ButtonClickListener3() {
            Log.d(TAG, "constructor");
        }
        public void onClick(View view) {
            Log.d(TAG, "onClick3");

            resetStats();
            Log.d(TAG, "resetstats");
        }
    }
}
