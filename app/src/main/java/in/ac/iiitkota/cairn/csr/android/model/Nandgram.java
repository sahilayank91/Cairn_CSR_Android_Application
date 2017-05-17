package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SS Verma on 21-04-2017.
 */

public class Nandgram {
    private long nandgram_id;
    private String nandgram_name,address;
    private ArrayList<Long> users;
    private double longitude,latitude;


    public Nandgram(JSONObject nandgram) throws JSONException {
        if(nandgram.has("nandgram_id"))this.nandgram_id=nandgram.getLong("nandgram_id");
        if(nandgram.has("nandgram_name"))this.nandgram_name=nandgram.getString("nandgram_name");
        if(nandgram.has("address"))this.address=nandgram.getString("address");
        if(nandgram.has("longitude"))this.longitude=nandgram.getDouble("longitude");
        if(nandgram.has("latitude"))this.latitude=nandgram.getDouble("latitude");
        if(nandgram.has("users")){
            ArrayList<Long>user_ids=new ArrayList<>();
            JSONArray users=nandgram.getJSONArray("users");
            for(int i=0;i<users.length();i++){
                user_ids.add(users.getLong(i));
            }
            this.users=user_ids;
        }
    }

    public long getNandgram_id() {
        return nandgram_id;
    }
    public void setNandgram_id(long nandgram_id) {
        this.nandgram_id = nandgram_id;
    }
    public String getNandgram_name() {
        return nandgram_name;
    }
    public void setNandgram_name(String nandgram_name) {
        this.nandgram_name = nandgram_name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public ArrayList<Long> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<Long> users) {
        this.users = users;
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
}