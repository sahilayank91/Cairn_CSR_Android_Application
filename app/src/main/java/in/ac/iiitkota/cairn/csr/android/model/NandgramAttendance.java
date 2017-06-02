package in.ac.iiitkota.cairn.csr.android.model;

/**
 * Created by root on 27/5/17.
 */


        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Date;

public class NandgramAttendance {
    private long attendance_id,nandgram_id,user_id,image_id;
    private double longitude,latitude;
    private Long time;
    private Long head_count;
    private int slot;
    private String image_filename;



    public NandgramAttendance(JSONObject author) throws JSONException {
        if(author.has("user_id"))this.user_id = author.getLong("user_id");
        if(author.has("head_count"))this.head_count = author.getLong("head_count");
        if(author.has("filename"))this.image_filename = author.getString("filename");
    }

    public long getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(long attendance_id) {
        this.attendance_id = attendance_id;
    }
    public long getNandgram_id() {
        return nandgram_id;
    }

    public void setNandgram_id(long nandgram_id) {
        this.nandgram_id = nandgram_id;
    }
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public long getImage_id() {
        return image_id;
    }

    public void setImage_id(long image_id) {
        this.image_id = image_id;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public Long getHead_count() {
        return head_count;
    }

    public void setHead_count(Long head_count) {
        this.head_count = head_count;
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
    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }


    public void setImageName(String img_name){
        this.image_filename = img_name;
    }

    public String getImageName(){
        return image_filename;
    }

}
