package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;




public class Team {
	private long team_id;
	private String name,description;
	private ArrayList<User> members=new ArrayList<>(),admins=new ArrayList<>();
	private ArrayList<Message>messages=new ArrayList<>();



    public Team(JSONObject team) throws JSONException {
		if(team.has("team_id"))this.team_id=team.getLong("team_id");
		if(team.has("name"))this.name=team.getString("name");
		if(team.has("description"))this.description=team.getString("description");
		if(team.has("members")){
			JSONArray members_array =team.getJSONArray("members");
			for (int i = 0; i < members_array.length(); i++) {
				members.add(new User(members_array.getJSONObject(i)));

			}

		}
		if(team.has("admins")){
			JSONArray admins_array =team.getJSONArray("admins");
			for (int i = 0; i < admins_array.length(); i++) {
				admins.add(new User(admins_array.getJSONObject(i)));

			}

		}


    }

    public long getTeam_id() {
		return team_id;
	}
	
	public void setTeam_id(long team_id) {
		this.team_id = team_id;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<User> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}

	public ArrayList<User> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<User> admins) {
		this.admins = admins;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
}
