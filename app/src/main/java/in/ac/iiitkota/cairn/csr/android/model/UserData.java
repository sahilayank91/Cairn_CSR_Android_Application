package in.ac.iiitkota.cairn.csr.android.model;

import android.content.Context;

import in.ac.iiitkota.cairn.csr.android.SharedPreferenceSingleton;

/**
 * Created by joey on 2/12/16.
 */
public class UserData {
    private static UserData ourInstance = new UserData();
    private String username, email, contact,department;
    private int account_level;
    private Long user_id;
    private Context context;
    Boolean verify;


    private UserData() {
    }

    public static UserData getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UserData();
            ourInstance.getUserData(context);
        }

        try {
            Long id= ourInstance.getUser_id();
            if(id==null)throw new Exception();
        } catch (Exception e) {
            ourInstance = null;
            ourInstance = new UserData();
            ourInstance.getUserData(context);
        }

        return ourInstance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        SharedPreferenceSingleton.getInstance(context).put("username", username);
    }



    //    public void initUserData(String data, Context context) throws Exception {
//        this.context = context;
//        JSONObject userdata = new JSONObject(data);
//
//        setUser_id(userdata.getLong("id"));
//        setUsername(userdata.getString("username"));
//        setEmail(userdata.getString("email"));
//        setContact(userdata.getString("contact"));
//
//    }
    public void initUserData(User user,Context context)  {
        this.context = context;


        setUser_id(user.getUser_id());
        setUsername(user.getName());
        setEmail(user.getEmail());
        setContact(user.getPhone());
        setAccount_level(user.getAccount_level());

    }

    public boolean getUserData(Context context) {
        this.context = context;
        try {

            this.user_id = SharedPreferenceSingleton.getInstance(context).getLong("user_id");
            this.username = SharedPreferenceSingleton.getInstance(context).getString("username");
            this.contact = SharedPreferenceSingleton.getInstance(context).getString("contact");
            this.email = SharedPreferenceSingleton.getInstance(context).getString("email");
            this.account_level=SharedPreferenceSingleton.getInstance(context).getInt("account_level");
            this.verify = SharedPreferenceSingleton.getInstance(context).getBoolean("verified");
            this.department = SharedPreferenceSingleton.getInstance(context).getString("department");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if(this.user_id==0 || this.verify==false)return false;
        return true;
    }

    public User getUser(){
        User user=new User();
        user.setEmail(this.email);
        user.setUser_id(this.user_id);
        user.setName(this.username);
        user.setPhone(this.contact);
        user.setAccount_level(this.account_level);
        return user;
    }
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
        SharedPreferenceSingleton.getInstance(context).put("user_id", user_id);
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        SharedPreferenceSingleton.getInstance(context).put("email", email);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
        SharedPreferenceSingleton.getInstance(context).put("contact", contact);
    }

    public void setAccount_level(int account_level) {
        this.account_level = account_level;
        SharedPreferenceSingleton.getInstance(context).put("account_level", account_level);
    }

    public void setVerify(Boolean verify){
        this.verify = verify;
    }

    public Boolean getVerify(){
        return this.verify;
    }
}
