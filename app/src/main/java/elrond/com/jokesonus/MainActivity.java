package elrond.com.jokesonus;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;


import elrond.com.Backend.Database;
import elrond.com.Backend.LiteDatabaseHelper;
import elrond.com.adapters.JokeAdapter;
import elrond.com.tags.JokeTags;
import elrond.com.tags.ViewNames;

public class MainActivity extends AppCompatActivity {

  private static String currentTag = JokeTags.STUPID.name().toUpperCase();
  private static ViewNames currentView = ViewNames.MAIN;
  private JokeAdapter adapter;
  Button titleBtn;
  ListView jokesLV;
  ArrayList jokes;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.main_menu:
          currentView = ViewNames.MAIN;
          updateJokesList();
          return true;
        case R.id.favorites:
          currentView = ViewNames.FAVORITES;
          updateJokesList();
          return true;
        case R.id.my_jokes:
          currentView = ViewNames.MYJOKES;
          updateJokesList();
          return true;
      }
      return false;
    }

  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // Set up toolbar
    titleBtn = (Button) findViewById(R.id.tag_select_btn);
    titleBtn.setText(currentTag);
    // Set up bottom navigation
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    // Set up jokes fragment

    this.jokesLV = (ListView) findViewById(R.id.jokes_list_view);
    jokes  = Database.getJokesByTag(MainActivity.currentTag);
    adapter = new JokeAdapter(getApplicationContext(), jokes);
    this.jokesLV.setAdapter(adapter);
  }

  public void tagDialog(View view) {
    DialogFragment tagDialog = new TagDialogFragment();
    tagDialog.show(getSupportFragmentManager(), "newer");
  }

  public static void setJokeTag(JokeTags jt) {
    MainActivity.currentTag = jt.name();
  }

  public static void setJokeTag(String jt) {
    MainActivity.currentTag = jt;
  }

  private void getFavJokes() {

  }

  protected void updateJokesList() {

    LiteDatabaseHelper liteDatabaseHelper;
    switch (currentView) {
      case FAVORITES:
        liteDatabaseHelper = new LiteDatabaseHelper(getApplicationContext());
        jokes = Database.getJokeByIds(liteDatabaseHelper.getFavJokes(), MainActivity.currentTag);
        break;
      case MYJOKES:
        liteDatabaseHelper = new LiteDatabaseHelper(getApplicationContext());
        jokes = Database.getJokeByIds(liteDatabaseHelper.getMyJokes(), MainActivity.currentTag);
        break;
      default:
        jokes = Database.getJokesByTag(MainActivity.currentTag);
        break;
    }
    adapter.clear();
    adapter.addAll(jokes);
    adapter.notifyDataSetChanged();
    jokesLV.setAdapter(adapter);
  }


  public void openAddFrag(View view) {
    DialogFragment tagDialog = new AddJokeFragment();
    tagDialog.show(getSupportFragmentManager(), "add_joke");
  }
}
