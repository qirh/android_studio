package tutorials.cs371m.androidtictactoe;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class AndroidTicTacToe extends AppCompatActivity {

    private static final String TAG = "Tic Tac Toe Activity";

    // Represents the internal state of the game
    private TicTacToeGame mGame;

    private boolean mHumanGoesFirst;

    // is the game over or not?
    private boolean mGameOver;

    // Buttons making up the board
    private Button mBoardButtons[];

    // Various text display
    private TextView mInfoTextView;

    // tracks how many time each outcome occurs (human wins,
    // tie, android wins
    private WinData mWinData;

    // displays for the number of each outcome
    private TextView[] mOutcomeCounterTextViews;

    BoardView mBoardView;

    private SoundPool mSounds;
    private int mHumanMoveSoundID;
    private int mComputerMoveSoundID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_tic_tac_toe);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.boardView);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        mHumanGoesFirst = true;
        mWinData = new WinData();
        initOutcomeTextViews();
        startNewGame();
    }

    private void initOutcomeTextViews() {
        mOutcomeCounterTextViews = new TextView[3];
        mOutcomeCounterTextViews[0] = (TextView) findViewById(R.id.human_wins_tv);
        mOutcomeCounterTextViews[1] = (TextView) findViewById(R.id.ties_tv);
        mOutcomeCounterTextViews[2] = (TextView) findViewById(R.id.android_wins_tv);
        Log.d(TAG, "text view array: " + Arrays.toString(mOutcomeCounterTextViews));
    }

    // Set up the game board.
    private void startNewGame() {
        mGame.clearBoard();
        mBoardView.invalidate(); // Leads to a redraw of the board view
        mGameOver = false;
        //snip...
    }

    private void setMove(char player, int location, int soundID) {
        mGame.setMove(player, location);
        mBoardView.invalidate();
        mSounds.play(soundID, 1, 1, 1, 0, 1);
    }

    private void computerMove() {


        mInfoTextView.setText(R.string.computer_turn);

        Handler handler = new Handler(); handler.postDelayed(new Runnable() {
            public void run() {
                Log.d("DELAY", "Hello");
                int move = mGame.getComputerMove();
                setMove(TicTacToeGame.COMPUTER_PLAYER, move, mComputerMoveSoundID);
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.human_turn);
                } else {
                    handleEndGame(winner);
                }
            }
        }, new Random().nextInt(500) + 500);
        /*
        int move = mGame.getComputerMove();
        setMove(TicTacToeGame.COMPUTER_PLAYER, move, mComputerMoveSoundID);
        int winner = mGame.checkForWinner();
        if (winner == 0) {
            mInfoTextView.setText(R.string.human_turn);
        } else {
            handleEndGame(winner);
        }
        */
    }

    private void handleEndGame(int winner) {
        WinData.Outcome outcome;
        if (winner == 1) {
            outcome = WinData.Outcome.TIE;
            mInfoTextView.setText(R.string.result_tie);
        } else if (winner == 2) {
            outcome = WinData.Outcome.HUMAN;
            mInfoTextView.setText(R.string.result_human_wins);
        }
        else {
            outcome = WinData.Outcome.ANDROID;
            mInfoTextView.setText(R.string.result_computer_wins);
        }
        mWinData.incrementWin(outcome);
        int index = outcome.ordinal();
        Log.d(TAG, "text view array: " + Arrays.toString(mOutcomeCounterTextViews));
        String display = "" + mWinData.getCount(outcome);
        mOutcomeCounterTextViews[index].setText(display);
        mGameOver = true;
        mHumanGoesFirst = !mHumanGoesFirst;
    }

    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled() && !mGameOver) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location, mComputerMoveSoundID);
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    computerMove();
                }
                else {
                    handleEndGame(winner);
                }
            }
        }
    }

    // Code below this point was added in tutorial 3.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                int currentDifficulty = mGame.getDifficultyLevel().ordinal();
                DifficultyDialogFragment difficultyDialogFragment
                        = DifficultyDialogFragment.newInstance(currentDifficulty);
                difficultyDialogFragment.show(fm, "difficulty");
                return true;
            case R.id.quit:
                QuitDialogFragment quitDialogFragment = new QuitDialogFragment();
                quitDialogFragment.show(fm, "quit");
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Set the difficulty. Presumably called by DifficultyDialogFragment;
     * @param difficulty The new difficulty for the game.
     */
    public void setDifficulty(int difficulty) {
        // check bounds;
        if (difficulty < 0 || difficulty >= TicTacToeGame.DifficultyLevel.values().length) {
            Log.d(TAG, "Unexpected difficulty: " + difficulty + "." +
                    " Setting difficulty to Easy / 0.");
            difficulty = 0; // if out of bounds set to 0
        }
        TicTacToeGame.DifficultyLevel newDifficulty
                = TicTacToeGame.DifficultyLevel.values()[difficulty];

        mGame.setDifficultyLevel(newDifficulty);
        String message = "Difficulty set to " +
                newDifficulty.toString().toLowerCase() + " .";

        // Display the selected difficulty level
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();

    }

    // Listen for touches on the board. Only apply move if game not over.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (!mGameOver) {
                // Determine which cell was touched
                int col = (int) event.getX() / mBoardView.getBoardCellWidth();
                int row = (int) event.getY() / mBoardView.getBoardCellHeight();
                int pos = row * 3 + col;
                // is that an open spot?
                if (mGame.getBoardOccupant(pos) == TicTacToeGame.OPEN_SPOT) {
                    // make the human move
                    setMove(TicTacToeGame.HUMAN_PLAYER, pos, mHumanMoveSoundID);
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        computerMove();
                    } else {
                        handleEndGame(winner);
                    }
                }
            }
            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        mHumanMoveSoundID = mSounds.load(this, R.raw.human_mov, 1); // Context, id of resource, priority (currently no effect)
        mComputerMoveSoundID = mSounds.load(this, R.raw.computer_move, 1);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "in onPause");
        if(mSounds != null) {
            mSounds.release();
            mSounds = null;
        }
    }
}
