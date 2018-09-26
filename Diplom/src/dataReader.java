import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dataReader {

	String jsonstring;	
	JSONArray sub, classes = null;
	JSONObject jsonObj, oneClass, time = null;
	CalculatePunish calculate = new CalculatePunish();

	public dataReader(String json) {
		this.jsonstring = json;
	}

	public void readJson(){
		try {
			jsonObj = new JSONObject(jsonstring);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public Lecture[] createLectures(){
		Lecture[] lectureList = null;
		try {
			classes = jsonObj.getJSONArray("Lectures");
			lectureList = new Lecture[classes.length()];
			for (int i = 0; i < classes.length(); i++) {
				Lecture lecture = null;
				oneClass = classes.getJSONObject(i);
				if(oneClass != null){
					time = oneClass.getJSONObject("Time");
					lecture = new Lecture(oneClass.getInt("Id"), oneClass.getString("Subject"), oneClass.getString("Class"), oneClass.getInt("SubstituteGender"), oneClass.getBoolean("IgnoreSubject"), new TimeInterval(time.getString("Start"), time.getString("End"), time.getString("Duration")));
					lectureList[i] = lecture;
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return lectureList;
	}

	public Substitute[] createSubstitutes(Lecture[] lectures, ArrayList<String> tis){
		Substitute[] substitutesList = null;
		try {
			sub = jsonObj.getJSONArray("Substitutes");
			substitutesList = new Substitute[sub.length() * tis.size()];
			for (int i = 0; i < sub.length(); i++) {
				double minusWeight = 0;
				int id = sub.getJSONObject(i).getInt("Id");
				int exp = sub.getJSONObject(i).getInt("Experience");
				int course = sub.getJSONObject(i).getInt("Courses");
				int rating = sub.getJSONObject(i).getInt("Rating");
				int edu = sub.getJSONObject(i).getInt("Education");
				minusWeight = minusWeight + calculate.calculateItem(exp, jsonObj.getJSONObject("ExperiencePenalty").getJSONArray("Intervals"));
				minusWeight = minusWeight + calculate.calculateItem(course, jsonObj.getJSONObject("CoursePenalty").getJSONArray("Intervals"));
				minusWeight = minusWeight + calculate.calculateItem(rating, jsonObj.getJSONObject("RatingPenalty").getJSONArray("Intervals"));
				minusWeight = minusWeight + calculate.calculateItem(edu, jsonObj.getJSONObject("EducationPenalty").getJSONArray("Intervals"));
				TimeInterval[] til = new TimeInterval[sub.getJSONObject(i).getJSONArray("AvailableTime").length()];
				for (int j = 0; j < til.length; j++) {
					JSONObject time = sub.getJSONObject(i).getJSONArray("AvailableTime").getJSONObject(j);
					TimeInterval availableTime = new TimeInterval(time.getString("Start"), time.getString("End"));	
					til[j] = availableTime;
				}
				for (int j = 0; j < tis.size(); j++) {
					TimeInterval ti = new TimeInterval(tis.get(j), TimeInterval.endList.get(j));
					int[] weight = new int[lectures.length];
					for (int k = 0; k < lectures.length; k++) {
						if(lectures[k].ti.available(til) && ti.available(lectures[k].ti)){
							double startWeight = 100;
							int level = 0;
							int classExp = 0;
							int subExp = 0;
							int gender = 0;
							try {
								level = sub.getJSONObject(i).getJSONObject("SubjectLevel").getInt(lectures[k].subject);
							} catch (Exception e) {
							}
							try {
								classExp = sub.getJSONObject(i).getJSONObject("ClassExperience").getInt(lectures[k].classid);
							} catch (Exception e) {
							}
							try {
								subExp = sub.getJSONObject(i).getJSONObject("SubjectExperience").getInt(lectures[k].subject);
							} catch (Exception e) {
							}
							try {
								gender = sub.getJSONObject(i).getInt("Gender");
							} catch (Exception e) {
							}
							startWeight = startWeight - calculate.calculateItem(subExp, jsonObj.getJSONObject("SubjectExperiencePenalty").getJSONArray("Intervals"));
							startWeight = startWeight - calculate.calculateItem(classExp, jsonObj.getJSONObject("ClassExperiencePenalty").getJSONArray("Intervals"));
							startWeight = startWeight - calculate.genderCalculate(gender, lectures[k].subGender, jsonObj.getInt("GenderPenalty"));
							startWeight = startWeight - calculate.calculateItem(level, jsonObj.getJSONObject("SubjectLevelPenalty").getJSONArray("Intervals"));
							startWeight = startWeight - minusWeight;
							if(startWeight < 0)
								startWeight = 0;
							weight[k] = (int) startWeight;
						}
						
					}
					substitutesList[sub.length()*j+i] = new Substitute(id, ti, weight);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return substitutesList;
	}

	public int[][] createWeigthMatrix(Substitute[] subList, int lectures){
		int[] oneArray = new int[subList.length - lectures];
		for (int i = 0; i < oneArray.length; i++) {
			oneArray[i] = 101;
		}
		int [][] matrix = new int[subList.length][subList.length];
		for (int i = 0; i < subList.length; i++) {
			int[] arrayTotal = new int[subList.length];
			System.arraycopy(subList[i].weigth, 0, arrayTotal, 0, subList[i].weigth.length);
			System.arraycopy(oneArray, 0, arrayTotal, subList[i].weigth.length, oneArray.length);
			matrix[i] = arrayTotal;
		}
		return matrix;
		
	}

}
