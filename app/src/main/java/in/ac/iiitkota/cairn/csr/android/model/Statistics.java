package in.ac.iiitkota.cairn.csr.android.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Statistics {
	private Long object_id;
	private boolean team;
	private ArrayList<Long>week,month,year;
	private ArrayList<Long>week_dates,month_dates,year_dates;

	public Statistics(JSONObject jsonObject) throws JSONException {
		if(jsonObject.has("object_id"))this.object_id=jsonObject.getLong("object_id");
		if(jsonObject.has("team"))this.setTeam(jsonObject.getBoolean("team"));
		if(jsonObject.has("week")){
		JSONArray jsonArray=jsonObject.getJSONArray("week");

			week= new ArrayList<Long>();
			for (int i = 0; i < jsonArray.length(); i++) {
				;
				week.add(jsonArray.getLong(i));
			}

		}
        if(jsonObject.has("month")){
            JSONArray jsonArray=jsonObject.getJSONArray("month");

            month= new ArrayList<Long>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ;
                month.add(jsonArray.getLong(i));
            }

        }
        if(jsonObject.has("year")){
            JSONArray jsonArray=jsonObject.getJSONArray("year");

            year= new ArrayList<Long>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ;
                year.add(jsonArray.getLong(i));
            }

        }
        if(jsonObject.has("week_dates")){
            JSONArray jsonArray=jsonObject.getJSONArray("week_dates");

            week_dates= new ArrayList<Long>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ;
                week_dates.add(jsonArray.getLong(i));
            }

        }
        if(jsonObject.has("month_dates")){
            JSONArray jsonArray=jsonObject.getJSONArray("year_dates");

            month_dates= new ArrayList<Long>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ;
                month_dates.add(jsonArray.getLong(i));
            }

        }
        if(jsonObject.has("year_dates")){
            JSONArray jsonArray=jsonObject.getJSONArray("year_dates");

            year_dates= new ArrayList<Long>();
            for (int i = 0; i < jsonArray.length(); i++) {
                ;
                year_dates.add(jsonArray.getLong(i));
            }

        }

	}

	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long object_id) {
		this.object_id = object_id;
	}
	
	public ArrayList<Long> getWeek() {
		return week;
	}

	public void setWeek(ArrayList<Long> week) {
		this.week = week;
	}
	public ArrayList<Long> getMonth() {
		return month;
	}

	public void setMonth(ArrayList<Long> month) {
		this.month = month;
	}
	public ArrayList<Long> getYear() {
		return year;
	}

	public void setYear(ArrayList<Long> year) {
		this.year = year;
	}
	public ArrayList<Long> getWeek_dates() {
		return week_dates;
	}

	public void setWeek_dates(ArrayList<Long> week_dates) {
		this.week_dates = week_dates;
	}
	public ArrayList<Long> getMonth_dates() {
		return month_dates;
	}

	public void setMonth_dates(ArrayList<Long> month_dates) {
		this.month_dates = month_dates;
	}
	public ArrayList<Long> getYear_dates() {
		return year_dates;
	}

	public void setYear_dates(ArrayList<Long> year_dates) {
		this.year_dates = year_dates;
	}
	public boolean isTeam() {
		return team;
	}

	public void setTeam(boolean team) {
		this.team = team;
	}

	

}
