package elrond.com.jokesonus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import elrond.com.Backend.Database;
import elrond.com.Backend.LiteDatabaseHelper;

/**
 * Created by umarn on 2017-08-10.
 */

public class AddJokeFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AddJokeFragment frag = this;
    final LiteDatabaseHelper databaseHelper = new LiteDatabaseHelper(getContext());
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    final View view = inflater.inflate(R.layout.add_joke, null);
    final Spinner spinner = (Spinner) view.findViewById(R.id.tag_picker);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.tags_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    builder.setTitle(getString(R.string.add_a_joke_title))
        .setView(view)
        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
              EditText jokeEditText = (EditText) view.findViewById(R.id.add_joke_text);
              String  jokeString = jokeEditText.getText().toString();
              String jokeTag = spinner.getSelectedItem().toString().toUpperCase();
              int jokeId = Database.addJoke(jokeString, jokeTag);
              databaseHelper.addMyJoke(jokeId);
              // Update the list view on the main activity.
              MainActivity main = (MainActivity) frag.getActivity();
              main.updateJokesList();
          }
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
              // Update the list view on the main activity.
              MainActivity main = (MainActivity) frag.getActivity();
              main.updateJokesList();
          }
        });
    return builder.create();
  }
}