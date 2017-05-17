package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;



public class Image {
	private String filename,caption;
	private long image_id;
	private ArrayList<Long> likes;
	private Long time;

	public Image(JSONObject image) throws JSONException {
        if(image.has("caption"))setCaption(image.getString("caption"));
        if(image.has("time"))setTime(image.getLong("time"));
        if(image.has("image_id"))setImage_id(image.getLong("image_id"));
        if(image.has("filename"))setFilename(image.getString("filename"));

	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	public long getImage_id() {
		return image_id;
	}

	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}
	public ArrayList<Long> getLikes() {
		return likes;
	}

	public void setLikes(ArrayList<Long> likes) {
		this.likes = likes;
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

}
