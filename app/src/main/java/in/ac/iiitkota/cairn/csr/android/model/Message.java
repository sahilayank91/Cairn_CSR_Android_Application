package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by SS Verma on 15-03-2017.
 */

public class Message {
   private String text;
    private Long message_id;
    private User author;
    private Long time;
    public String getText() {
        return text;
    }
    public Message(){

    }

    public Message(JSONObject message) throws JSONException {
        if(message.has("text")) setText(message.getString("text"));
        if(message.has("message_id")) setMessage_id(message.getLong("message_id"));
        if(message.has("time")) setTime(message.getLong("time"));
        if(message.has("author"))setAuthor(new User(message.getJSONObject("author")));

    }
    public void setText(String text) {
        this.text = text;
    }

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}