package elrond.com.Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import elrond.com.contracts.MyJokesContract;
import elrond.com.contracts.SavedJokesContract;

/**
 * Created by umarn on 2017-08-08.
 */

public class LiteDatabaseHelper extends SQLiteOpenHelper {

  private static final int DATABASE_VERSION = 3;
  private static final String DATABASE_NAME = "saved_data.db";

  public LiteDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(SavedJokesContract.CREATE_TABLE);
    sqLiteDatabase.execSQL(MyJokesContract.CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SavedJokesContract.SavedJokesInfo.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyJokesContract.MyJokesInfo.TABLE_NAME);
    onCreate(sqLiteDatabase);
  }

  public long favoriteJoke(int id, int favOrUnfav) {
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(SavedJokesContract.SavedJokesInfo.FAVORITED, favOrUnfav);
    if (getJokeDetails(id) == null) {
      // The joke is not in our database, add it.
      contentValues.put(SavedJokesContract.SavedJokesInfo.JOKE_ID, id);
      contentValues.put(SavedJokesContract.SavedJokesInfo.LIKED, 0);
        return sqLiteDatabase.insert(SavedJokesContract.SavedJokesInfo.TABLE_NAME, null, contentValues);
    } else {
      // Joke is in the database right now, so update it.
      return sqLiteDatabase.update(SavedJokesContract.SavedJokesInfo.TABLE_NAME,
              contentValues,
              SavedJokesContract.SavedJokesInfo.JOKE_ID + " = ?",
              new String[] {String.valueOf(id)});
    }
  }

  public long likeOrDislikeJoke(int id, int likeOrDislike) {
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(SavedJokesContract.SavedJokesInfo.LIKED, likeOrDislike);
    if (getJokeDetails(id) == null) {
      // Joke not in database, must add
      contentValues.put(SavedJokesContract.SavedJokesInfo.JOKE_ID, id);
      contentValues.put(SavedJokesContract.SavedJokesInfo.FAVORITED, Boolean.FALSE);
      return sqLiteDatabase.insert(SavedJokesContract.SavedJokesInfo.TABLE_NAME, null, contentValues);
    } else {
      return sqLiteDatabase.update(SavedJokesContract.SavedJokesInfo.TABLE_NAME, contentValues,
          SavedJokesContract.SavedJokesInfo.JOKE_ID + " = ?", new String[] {String.valueOf(id)});
    }
  }

  public long addMyJoke(int id){
    SQLiteDatabase sqLiteDatabase = getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(MyJokesContract.MyJokesInfo.JOKE_ID, id);
    return sqLiteDatabase.insert(MyJokesContract.MyJokesInfo.TABLE_NAME, null, contentValues);
  }


  public Integer[] getJokeDetails(int id) {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +
        SavedJokesContract.SavedJokesInfo.TABLE_NAME + " WHERE " +
          SavedJokesContract.SavedJokesInfo.JOKE_ID + " = " + String.valueOf(id), null);
    Integer[] values = null;
    if (cursor.moveToFirst()) {
      int favorite = cursor.getInt(cursor.getColumnIndex(SavedJokesContract.SavedJokesInfo.FAVORITED));
      int liked = cursor.getInt(cursor.getColumnIndex(SavedJokesContract.SavedJokesInfo.LIKED));
      values =  new Integer[2];
      values[0] = favorite;
      values[1] = liked;
    }
    cursor.close();
    return values;
  }

  public List<Integer> getFavJokes() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SavedJokesContract.SavedJokesInfo.TABLE_NAME +
    " WHERE " + SavedJokesContract.SavedJokesInfo.FAVORITED + " = " + String.valueOf(1), null);

    List<Integer> jokeIds = new ArrayList<>();
    while (cursor.moveToNext()) {
      int jokeId = cursor.getInt(cursor.getColumnIndex(SavedJokesContract.SavedJokesInfo.JOKE_ID));
      jokeIds.add(jokeId);
    }
    cursor.close();
    return jokeIds;
  }

  public List<Integer> getMyJokes() {
    SQLiteDatabase sqLiteDatabase = getReadableDatabase();
    Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MyJokesContract.MyJokesInfo.TABLE_NAME, null);
    List<Integer> jokeIds = new ArrayList<>();
    while (cursor.moveToNext()) {
      jokeIds.add(cursor.getInt(cursor.getColumnIndex(MyJokesContract.MyJokesInfo.JOKE_ID)));
    }
    cursor.close();
    return jokeIds;
  }
}
