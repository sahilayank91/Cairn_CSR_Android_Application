package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class UserProfile {
    private Long user_id;
    private String summary, department,name;
    private Long member_since;

    public UserProfile(JSONObject user_profile) throws JSONException {
        if(user_profile.has("user_id"))setUser_id(user_profile.getLong("user_id"));
        if(user_profile.has("summary"))setSummary(user_profile.getString("summary"));
        if(user_profile.has("department"))setDepartment(user_profile.getString("department"));
        if(user_profile.has("member_sinceDate"))setMember_sinceTime(user_profile.getLong("member_sinceDate"));
        if(user_profile.has("user_name"))setName(user_profile.getString("user_name"));

    }
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public Date getMember_sinceDate() {
        return new Date(member_since);
    }
    public Long getMember_sinceTime(){
        return member_since;
    }
    public void setMember_sinceTime(Long member_since) {
        this.member_since = member_since;
    }
    public void setMember_since(Date member_since) {
        this.member_since = member_since.getTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
