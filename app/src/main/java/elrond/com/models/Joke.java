package elrond.com.models;

import java.io.Serializable;

import elrond.com.Backend.Database;

/**
 * Created by umarn on 2017-08-02.
 */

public class Joke implements Serializable {

  private int jokeId;
  private String jokeString;
  private int upvotes;
  private int downvotes;
  private int tagId;

  public Joke(int jokeId, String jokeString, int tagId){
    this.jokeId = jokeId;
    this.jokeString = jokeString;
    this.tagId = tagId;
    this.upvotes = 0;
    this.downvotes = 0;
  }

  public int getJokeId(){
    return this.jokeId;
  }

  public void upvote(){
    Database.updateJoke(this.jokeId, "Upvotes", 1);
    this.upvotes += 1;
  }

  public void removeUpvote(){
    Database.updateJoke(this.jokeId, "Upvotes", -1);
    this.upvotes -=1;
  }

  public void setUpvotes(int upvotes){
    this.upvotes = upvotes;
  }

  public int getUpvotes(){
    return this.upvotes;
  }

  public void downvote(){
    Database.updateJoke(this.jokeId, "Downvotes", 1);
    this.downvotes +=1;
  }

  public void removeDownvote(){
    Database.updateJoke(this.jokeId, "Downvotes", -1);
    this.downvotes -= 1;
  }

  public void setDownvotes(int downvotes){
    this.downvotes = downvotes;
  }

  public int getDownvotes(){
    return this.downvotes;
  }

  public String getJokeString() {
    return this.jokeString;
  }

  public String toString(){
    return this.jokeString;
  }

}
