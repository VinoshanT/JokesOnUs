package elrond.com.contracts;

/**
 * Created by umarn on 2017-08-08.
 */

public final class MyJokesContract {

  private MyJokesContract() {}

  public static class MyJokesInfo {
    public static final String TABLE_NAME = "my_jokes";
    public static final String JOKE_ID = "joke_id";
  }

  public static final String CREATE_TABLE = "CREATE TABLE " +
      MyJokesContract.MyJokesInfo.TABLE_NAME + " (" +
      MyJokesContract.MyJokesInfo.JOKE_ID + " INTEGER)";
}
