package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;




public class Feedback {
	private Long feedback_id;
	private User user,admin;
	private Integer reaction;
	private  String comment;
	private boolean completed;
	private Long time;
    private String text;

	@Override
	public boolean equals(Object obj) {
		Feedback feedback=(Feedback)obj;
		if(feedback.getFeedback_id()==this.getFeedback_id())
		return true;
		else return false;
	}

	public Feedback(JSONObject feedback) throws JSONException {
		if(feedback.has("time"))setTime(feedback.getLong("time"));
		if(feedback.has("feedback_id"))setFeedback_id(feedback.getLong("feedback_id"));
		if(feedback.has("user"))setUser(new User(feedback.getJSONObject("user")));
		if(feedback.has("admin"))setAdmin(new User(feedback.getJSONObject("admin")));
		if(feedback.has("reaction"))setReaction(feedback.getInt("reaction"));
		if(feedback.has("comment"))setComment(feedback.getString("comment"));
		if(feedback.has("completed"))setCompleted(feedback.getBoolean("completed"));
        if(feedback.has("text"))setText(feedback.getString("text"));
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
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public Integer getReaction() {
		return reaction;
	}

	public void setReaction(Integer reaction) {
		this.reaction = reaction;
	}
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public Long getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(Long feedback_id) {
		this.feedback_id = feedback_id;
	}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
