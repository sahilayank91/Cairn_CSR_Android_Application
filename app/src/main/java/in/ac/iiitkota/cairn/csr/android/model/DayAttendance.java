package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONException;
import org.json.JSONObject;





public class DayAttendance{
	private int head_count_slot1;
	private int head_count_slot2;
	private String slot1_image;
	private String slot2_image;
	private Long time;


	public DayAttendance(JSONObject author) throws JSONException{
            if(author.has("head_count_slot1"))this.head_count_slot1 = author.getInt("head_count_slot1");
            if(author.has("head_count_slot2"))this.head_count_slot2 = author.getInt("head_count_slot2");
            if(author.has("slot1_image"))this.slot1_image = author.getString("slot1_image");
            if(author.has("slot2_image"))this.slot2_image = author.getString("slot2_image");
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		
		this.time = time;
	}
	public int getHead_count_slot1() {
		return head_count_slot1;
	}

	public void setHead_count_slot1(int head_count_slot1) {
		this.head_count_slot1 = head_count_slot1;
	}
	
	public String getSlot1_image(){
		return slot1_image;
	}
	

	public void setSlot1_image(String slot1_image){
		this.slot1_image = slot1_image;
	}
	
	public String getSlot2_image(){
		return slot2_image;
	}
	

	public void setSlot2_image(String slot2_image){
		this.slot2_image = slot2_image;
	} 
	
	public int getHead_count_slot2() {
		return head_count_slot2;
	}

	public void setHead_count_slot2(int head_count_slot2) {
		this.head_count_slot2 = head_count_slot2;
	}
	
	
	
	
}
