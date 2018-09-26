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
			return Math.round(y1 +(level - x1)*((y2-y1)/(x2-x1)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return 0;
	}

}
