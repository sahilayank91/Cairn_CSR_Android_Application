package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * @author Shubhi
 *
 */

public class Comment {
private long comment_id;
private User author;
private String text;
private Long time;

    public Comment(JSONObject comment) throws JSONException {
		if(comment.has("author"))setAuthor(new User(comment.getJSONObject("author")));
		if(comment.has("comment_id"))setComment_id(comment.getLong("comment_id"));
		if(comment.has("text"))setText(comment.getString("text"));
		if(comment.has("time"))setTime(comment.getLong("time"));
    }

    public long getComment_id() {
	return comment_id;
}

public void setComment_id(long comment_id) {
	this.comment_id = comment_id;
}
public User getAuthor() {
	return author;
}

public void setAuthor(User author) {
	this.author = author;
}
public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}
public Long getTime() {
	return time;
}
public Date getDate() {
	return new Date(time);
}

public void setTime(Long time) {
	this.time = time;
}
public void setTime(Date time) {
this.time = time.getTime();
}

}
