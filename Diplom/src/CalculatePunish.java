import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalculatePunish {


	public double calculateItem(int level, JSONArray punish){
		JSONObject second;
		JSONObject first;
		try {
			first = punish.getJSONObject(0);
			if(first.getDouble("Item1") > level)
				return first.getDouble("Item2");
			for (int i = 0; i < punish.length(); i++) {
				first = punish.getJSONObject(i);
				if(first.getDouble("Item1") == level){
					return first.getDouble("Item2");
				}
				else if(first.getDouble("Item1") > level){
					second = first;
					first = punish.getJSONObject(i-1);
					return calculate(level, first, second);
				}
			}
			return first.getDouble("Item2");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		return 0;
	}

	public double genderCalculate(int gender, int lectureGender, int punnish){
		if(lectureGender == 0){
			return 0;
		}
		else if(lectureGender == gender){
			return 0;
		}
		return 100;
	}

	private double calculate(int level, JSONObject first, JSONObject second){
		try {
			double x1 = first.getDouble("Item1");
			double x2 = second.getDouble("Item1");
			double y1 = first.getDouble("Item2");
			double y2 = second.getDouble("Item2");
			return y1 +(level - x1)*((y2-y1)/(x2-x1));
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return 0;
	}

	public int calculatePunishSchedule(Substitute [] sub, Lecture[] lec, JSONArray timePenalty, JSONArray gapPenalty){
		int id = 0;
		LocalDateTime time = null;
		double punish = 0;
		int workingTime = 0;
		for (int i = 0; i < sub.length; i++) {
			if(sub[i].index < lec.length){
				if(id != sub[i].id){
					//System.out.println("id: " + id);
					//System.out.println("punish: " + punish);
					//System.out.println("Working time: " + workingTime);
					id = sub[i].id;
					time=sub[i].ti.end;
					//System.out.println("Work pun: " + Math.round(calculateItem(workingTime, timePenalty)));
					punish += Math.round(calculateItem(workingTime, timePenalty));
					workingTime = (sub[i].ti.end.getHour() - sub[i].ti.start.getHour())*60 + (sub[i].ti.end.getMinute() - sub[i].ti.start.getMinute());
				}
				else{
					if(!sub[i].ti.start.equals(time)){
						int hour = (sub[i].ti.start.getHour() - time.getHour())*60 + (sub[i].ti.start.getMinute() - time.getMinute()); 
						punish += Math.round(calculateItem(hour, gapPenalty));
						//System.out.println("hour" + hour);
					}
					workingTime += (sub[i].ti.end.getHour() - sub[i].ti.start.getHour())*60 + (sub[i].ti.end.getMinute() - sub[i].ti.start.getMinute());  
					time = sub[i].ti.end;
				}
			}
		}
		punish += calculateItem(workingTime, timePenalty);
		return (int) Math.round(punish);
	}

	public int calculateDoublePen(double penalty, ArrayList<ArrayList<Lecture>> dubLec,  Lecture[] lec, Substitute[] sub){
		int punish = 0;
		for (int i = 0; i < dubLec.size(); i++) {
			if(!(sub[dubLec.get(i).get(0).sub].id == sub[dubLec.get(i).get(1).sub].id)){
				punish += penalty;
			}
		}
		return punish;
	}
	
	public int recalPunish(){
		return 0;
	}


}
