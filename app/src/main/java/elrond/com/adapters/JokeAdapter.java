package elrond.com.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import elrond.com.Backend.LiteDatabaseHelper;
import elrond.com.jokesonus.R;
import elrond.com.models.Joke;

/**
 * The adapter for the list of jokes this list view will use.
 *
 * Created by umarn on 2017-08-02.
 */

public class JokeAdapter extends ArrayAdapter<Joke> {

  public JokeAdapter(Context context, ArrayList<Joke> jokes) {
    super(context, 0, jokes);
  }

  @Override
  public View getView(int positon, View view, ViewGroup parent) {

    if (view == null) {
      view = LayoutInflater.from(getContext()).inflate(R.layout.jokes_view_item, parent, false);
    }

    // Set up the joke text, the number of up votes and downvotes
    TextView jokesText = (TextView) view.findViewById(R.id.joke_text);
    final TextView upvotesText = (TextView) view.findViewById(R.id.number_of_upvotes);
    final TextView downText = (TextView) view.findViewById(R.id.number_of_downvotes);

    final Joke joke = getItem(positon);
    jokesText.setText(joke.getJokeString());
    upvotesText.setText(String.valueOf(joke.getUpvotes()));
    downText.setText(String.valueOf(joke.getDownvotes()));
    final LiteDatabaseHelper databaseHelper = new LiteDatabaseHelper(getContext());

    final ImageButton thumbsdown = (ImageButton) view.findViewById(R.id.thumbs_down_btn);
    final ImageButton thumbsup = (ImageButton) view.findViewById(R.id.thumbs_up_btn);
    final ImageButton favStar = (ImageButton) view.findViewById(R.id.favorite_btn);

    // downvote the joke on button click
    thumbsdown.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Integer[] details = databaseHelper.getJokeDetails(joke.getJokeId());
        if (details != null) {
          int likeStatus = details[1];
          if (likeStatus == 1){
            joke.removeUpvote();
            upvotesText.setText(String.valueOf(joke.getUpvotes()));
            thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_unselected)));
            joke.downvote();
            downText.setText(String.valueOf(joke.getDownvotes()));
            thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dislikeColor)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), -1);
          } else if(likeStatus == 0){
            joke.downvote();
            downText.setText(String.valueOf(joke.getDownvotes()));
            thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dislikeColor)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), -1);
          } else {
            joke.removeDownvote();
            downText.setText(String.valueOf(joke.getDownvotes()));
            thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_unselected)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), 0);
          }
        } else {
          joke.downvote();
          downText.setText(String.valueOf(joke.getDownvotes()));
          thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dislikeColor)));
          databaseHelper.likeOrDislikeJoke(joke.getJokeId(), -1);
        }
      }
    });

    // upvote the joke on button click
    thumbsup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Integer[] details = databaseHelper.getJokeDetails(joke.getJokeId());
        if (details != null) {
          int likeStatus = details[1];
          if (likeStatus == -1) {
            joke.removeDownvote();
            downText.setText(String.valueOf(joke.getDownvotes()));
            thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_unselected)));
            joke.upvote();
            upvotesText.setText(String.valueOf(joke.getUpvotes()));
            thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.likeColor)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), 1);
          } else if(likeStatus == 0){
            joke.upvote();
            upvotesText.setText(String.valueOf(joke.getUpvotes()));
            thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.likeColor)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), 1);
          } else {
            joke.removeUpvote();
            upvotesText.setText(String.valueOf(joke.getUpvotes()));
            thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_unselected)));
            databaseHelper.likeOrDislikeJoke(joke.getJokeId(), 0);
          }
        } else {
          joke.upvote();
          upvotesText.setText(String.valueOf(joke.getUpvotes()));
          thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.likeColor)));
          databaseHelper.likeOrDislikeJoke(joke.getJokeId(), 1);
        }
      }
    });

    // fav or unfav a joke on button click
    favStar.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View v){
        Integer[] details = databaseHelper.getJokeDetails(joke.getJokeId());
        if (details != null){
          boolean isFavorite = details[0] > 0;
          if (!isFavorite){
            databaseHelper.favoriteJoke(joke.getJokeId(), 1);
            favStar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.favStarColor)));
          } else {
            databaseHelper.favoriteJoke(joke.getJokeId(), 0);
            favStar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.button_unselected)));
          }
        } else {
          databaseHelper.favoriteJoke(joke.getJokeId(), 1);
          favStar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.favStarColor)));
        }
      }
    });

    // Change the image button colors
    Integer[] details = databaseHelper.getJokeDetails(joke.getJokeId());
    if (details != null) {
      boolean isFavorite = details[0] > 0;
      if (isFavorite){
        favStar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.favStarColor)));
      }

      int likeStatus = details[1];
      // Edit the colors here.
      switch (likeStatus) {
        case 1:
          thumbsup.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.likeColor)));
          break;
        case -1:
          thumbsdown.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.dislikeColor)));
          break;
        default:
          // Do nothing
          break;
      }
    }
    return view;
  }

}
