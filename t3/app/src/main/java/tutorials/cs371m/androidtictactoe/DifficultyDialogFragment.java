package tutorials.cs371m.androidtictactoe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment; import android.content.DialogInterface; import android.os.Bundle;
import android.util.Log;

public class DifficultyDialogFragment extends DialogFragment {
    private static final String TAG = "Difficulty Dialog";
    private static final String DIFFICULTY_KEY = "difficulty";


    public static DifficultyDialogFragment newInstance(int difficulty) {
        Log.d(TAG, "newInstance()");
        DifficultyDialogFragment result = new DifficultyDialogFragment();

        Bundle args = new Bundle();
        args.putInt(DIFFICULTY_KEY , difficulty);
        result.setArguments(args);
        return result;
    }

    public int getSelectedDifficulty() {
        Log.d(TAG, "getSelectedDifficulty()");
        int tmp =  getArguments().getInt(DIFFICULTY_KEY, 0);
        Log.d(TAG, "getSelectedDifficulty() - " + tmp);
        return tmp;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        int currentDifficulty = getSelectedDifficulty();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.difficulty_choose)
                .setSingleChoiceItems(R.array.difficulty_levels, currentDifficulty,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getArguments().putInt(DIFFICULTY_KEY, which);
                        }
                    })
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                int newDifficulty = getArguments().getInt(DIFFICULTY_KEY, 0);
                Log.d(TAG, "User clicked okay. Changing difficulty to: " + newDifficulty);
                ((AndroidTicTacToe)getActivity()).setDifficulty(newDifficulty);
                Log.d(TAG, "User clicked okay. Changing difficulty to: " + newDifficulty);
                dismiss();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { // don't change difficulty
                        dismiss();
                    }
                });
        return builder.create();
    }
}
