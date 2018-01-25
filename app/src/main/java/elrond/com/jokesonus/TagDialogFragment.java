package elrond.com.jokesonus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by umarn on 2017-08-04.
 *
 * Used to create a dialog to pick a tag.
 */

public class TagDialogFragment extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final TagDialogFragment frag = this;
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.dialog_title)
        .setItems(R.array.tags_array, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            // Set the new tag
            String tag = getResources().getStringArray(R.array.tags_array)[i].toUpperCase();
            MainActivity.setJokeTag(tag);
            Button tagButton = (Button) getActivity().findViewById(R.id.tag_select_btn);
            tagButton.setText(tag);
            // Update the list view on the main activity.
            MainActivity main = (MainActivity) frag.getActivity();
            main.updateJokesList();
          }
        });
    return builder.create();
  }
}
