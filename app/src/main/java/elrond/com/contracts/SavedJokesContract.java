package elrond.com.contracts;

/**
 * Created by umarn on 2017-08-08.
 */

public final class SavedJokesContract {

  private SavedJokesContract() {}

  public static class SavedJokesInfo {
    public static final String TABLE_NAME = "saved_jokes";
    public static final String JOKE_ID = "joke_id";
    // boolean value
    public static final String FAVORITED = "was_favorite";
    // -1 for disliked, 0 for nothing, 1 for liked
    public static final String LIKED = "was_liked";
  }

  public static final String CREATE_TABLE = "CREATE TABLE " + SavedJokesInfo.TABLE_NAME + " (" +
      SavedJokesInfo.JOKE_ID + " INTEGER," +
      SavedJokesInfo.FAVORITED + " BOOLEAN," +
      SavedJokesInfo.LIKED + " INTEGER)";

  public static String addIntoDbQuery(Integer jokeId, Boolean favorite, Integer liked) {
    return "INSERT INTO " + SavedJokesInfo.TABLE_NAME + " VALUES (" + jokeId.toString() +
        ", " + favorite.toString().toUpperCase() + ", " + liked.toString() + ")";
  }

}
