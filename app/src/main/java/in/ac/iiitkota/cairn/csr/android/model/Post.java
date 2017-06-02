package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class Post {
    private long post_id,smiles;
    private User author;
    private ArrayList<Comment> comments;
    private ArrayList<User> likers;
    private ArrayList<Image> images;
    private String text;
    private Long time;
    private long likes_count,comments_count;

    public Post(JSONObject post) throws JSONException {

        if(post.has("author")){
            User author=new User(post.getJSONObject("author"));
            setAuthor(author);
        }
        if(post.has("comments")){
            JSONArray comments_array=post.getJSONArray("comments");
            ArrayList<Comment> comments=new ArrayList<>();
            for(int i=0;i<comments_array.length();i++){
                Comment comment=new Comment(comments_array.getJSONObject(i));
                comments.add(comment);
            }
            setComments(comments);
        }
        if(post.has("images")){

              JSONArray imageArray = post.getJSONArray("images");
              ArrayList<Image> images = new ArrayList<>();
              for (int i = 0; i < imageArray.length(); i++) {
                  Image image = new Image(imageArray.getJSONObject(i));
                  images.add(image);
              }
              setImages(images);



        }
        if(post.has("likers")){

            JSONArray likersArray = post.getJSONArray("likers");
            ArrayList<User> likers = new ArrayList<>();
            for (int i = 0; i < likersArray.length(); i++) {
                User user = new User(likersArray.getJSONObject(i));
                likers.add(user);
            }
            setLikers(likers);



        }
        if(post.has("likes_count"))setLikes_count(post.getLong("likes_count"));
        if(post.has("comments_count"))setComments_count(post.getLong("comments_count"));
        if(post.has("post_id"))setPost_id(post.getLong("post_id"));
        if(post.has("smiles"))setSmiles(post.getLong("smiles"));
        if(post.has("text"))setText(post.getString("text"));
        if(post.has("time"))setTime(post.getLong("time"));




    }

    public long getPost_id() {
        return post_id;
    }
    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }
    public long getSmiles() {
        return smiles;
    }
    public void setSmiles(long smiles) {
        this.smiles = smiles;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public ArrayList<User> getLikers() {
        return likers;
    }

    public void setLikers(ArrayList<User> likers) {
        this.likers = likers;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Date getDate() {
        return new Date(time);
    }
    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
    public void setTime(Date time) {
        this.time = time.getTime();
    }
    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }
    public long getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(long likes_count) {
        this.likes_count = likes_count;
    }
}
