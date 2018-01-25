package elrond.com.Backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import elrond.com.models.Joke;

/**
 * This is a class used to gather data from the sql database
 * Created by User on 2017-08-04.
 */

public class Database {

    public static ArrayList<Joke> getJokesByTag(String tag){
        SQLWriter sqlWriter = new SQLWriter();
        String query = "select * from Jokes inner join Tags on Jokes.TagID=Tags.ID where TagValue like ?";
        ArrayList<Joke> listOfJokes = null;
        try {
            PreparedStatement stmt = sqlWriter.conn.prepareStatement(query);
            stmt.setString(1, "%"+tag.toUpperCase()+"%");
            ResultSet rs = stmt.executeQuery();
            listOfJokes = convertResultSetToJokes(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sqlWriter.closeConnection();
        return listOfJokes;
    }

    private static ArrayList<Joke> convertResultSetToJokes(ResultSet rs){
        ArrayList<Joke> listOfJokes = new ArrayList<>();
        //convert each row in the result to joke object and add to list
        try {
            while (rs.next()) {
                int jokeId = Integer.parseInt(rs.getString(1));
                String jokeString = rs.getString(2);
                int upvotes = Integer.parseInt(rs.getString(3));
                int downvotes = Integer.parseInt(rs.getString(4));
                int tagId = Integer.parseInt(rs.getString(5));
                Joke joke = new Joke(jokeId, jokeString, tagId);
                joke.setDownvotes(downvotes);
                joke.setUpvotes(upvotes);
                listOfJokes.add(joke);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return listOfJokes;
    }

    public static int addJoke(String jokeString, String jokeTag){
        int jokeId = -1;
        SQLWriter sqlWriter = new SQLWriter();
        try{
            String sql = "insert into Jokes values (?,0,0, (select ID from Tags where TagValue like ?))";
            PreparedStatement stmt = sqlWriter.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, jokeString);
            stmt.setString(2, "%"+jokeTag.toUpperCase()+"%");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                jokeId = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        sqlWriter.closeConnection();
        return jokeId;
    }

    public static void updateJoke(int jokeId, String colName, int incOrdec){
        SQLWriter sqlWriter = new SQLWriter();
        try{
            String sign = "";
            if (incOrdec == 1){
                sign = "+";
            } else {
                sign = "-";
            }
            String sql ="UPDATE Jokes SET "+colName+" = "+colName+" "+sign+" 1 WHERE JokeId=?";
            PreparedStatement stmt = sqlWriter.conn.prepareStatement(sql);
            stmt.setInt(1, jokeId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sqlWriter.closeConnection();
    }

    public static ArrayList<Joke> getJokeByIds(List<Integer> jokeIds, String tag) {
        if (jokeIds.isEmpty()) {
            return new ArrayList<>();
        }
        SQLWriter sqlWriter = new SQLWriter();
        String jokesIdSQL = "(";
        for (Integer id : jokeIds) {
            jokesIdSQL += String.valueOf(id) + ",";
        }
        jokesIdSQL = jokesIdSQL.substring(0, jokesIdSQL.lastIndexOf(",")) + ")";
        String query = "select * from Jokes inner join Tags on Jokes.TagID=Tags.ID where TagValue like ? and JokeId in " + jokesIdSQL;

        ArrayList<Joke> listOfJokes = new ArrayList<>();
        try {
            PreparedStatement stmt = sqlWriter.conn.prepareStatement(query);
            stmt.setString(1, "%"+tag.toUpperCase()+"%");
            ResultSet rs = stmt.executeQuery();
            listOfJokes = convertResultSetToJokes(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        sqlWriter.closeConnection();
        return listOfJokes;
    }

}