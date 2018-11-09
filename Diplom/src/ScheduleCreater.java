import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ScheduleCreater {
	private File file;
	private int [][] matrix;
	private CalculatePunish calculate;
	private DataReader reader;
	private JobAssignment jobAssignment;
	private Lecture[] lecture;
	private Substitute[] sub;
	private String json = "";
	private ArrayList<ArrayList<Lecture>> doubleLecture;
	private int matchScore;
	private int schedulePunish;
	private int totalScore;

	public ScheduleCreater(String filePath){
		calculate = new CalculatePunish();
		file = new File(filePath);
		try {
			byte[] bytes = Files.readAllBytes(file.toPath());
			json = new String(bytes,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader = new DataReader(json);
	}

	public void preProcesing(){
		reader.readJson();
		lecture = reader.createLectures();
		TimeInterval.startList.sort(null);
		TimeInterval.endList.sort(null);
		sub = reader.createSubstitutes(lecture, TimeInterval.startList);	
		matrix = reader.createWeigthMatrix(sub, lecture.length);
	}

	public void createInitialSchedule(){
		jobAssignment = new JobAssignment(matrix);
		jobAssignment.initialzeAuxiliray();
		jobAssignment.makeEqualityGraph();
		jobAssignment.makeInitialMatching();
		jobAssignment.initialzeMarkingProcedure();
		jobAssignment.markingProcedure();


		while(jobAssignment.lengthPath != jobAssignment.equalityGraph.length){
			jobAssignment.updateAuxilirayNumbers();	
			jobAssignment.resetMatching();
			jobAssignment.makeEqualityGraph();
			jobAssignment.initialzeMarkingProcedure();
			jobAssignment.markingProcedure();
		}
		matchScore = jobAssignment.printMatching();

		for (int i = 0; i < sub.length; i++) {
			try {
				sub[i].setIndec(jobAssignment.columnPicked[i]);
				sub[i].setWeigth(jobAssignment.orginalMatrix[i]);
				lecture[jobAssignment.columnPicked[i]].setSub(sub[i]);
				sub[i].setLecture(lecture[jobAssignment.columnPicked[i]]);
				//System.out.println(sub[i].id + " " + lecture[job.columnPicked[i]].id + " " + i);
			} 
			catch (Exception e) {
			}
		}
		doubleLecture = reader.findDoubleLectures(lecture);
		
		schedulePunish = calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture);
		
		System.out.println("Shc: " + calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + " dou: " + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture));
		
		totalScore = matchScore - schedulePunish;

		System.out.println("Matchscore: " + matchScore);
		System.out.println("SchedulePunish: " + schedulePunish);
		System.out.println("TotalScore: " + totalScore);
		
		sub = Substitute.removeSubs(sub);

	}

	public void randomTwoChange(){
		
	}
	
	public void doubleChange(){
		
	}
	
	public void shortWorkDay(){
		
	}
	
	public void freePeriod(){
		
	}
	
	public void firstLastLecture(){
		
	}
	
	public void randomChangeOne(){
		final long NANOSEC_PER_SEC = 1000l*1000*1000;

		long startTime = System.nanoTime();
		while ((System.nanoTime()-startTime)< 3*60*NANOSEC_PER_SEC){

			int first = (int) Math.floor(Math.random() * lecture.length);
			int second = (int) Math.floor(Math.random() * sub.length);

			first = lecture[first].sub.index;

			while(!sub[first].ti.start.equals(sub[second].ti.start) && sub[first].id != sub[second].id){
				//System.out.println("not same: " + sub[first].ti.start + " " + sub[second].ti.start);
				second = (int) Math.floor(Math.random() * sub.length);
			}

			int tempScore = switchTwo(first, second);


			if(totalScore < tempScore){
				totalScore = tempScore;
			}
			else{
				reverseSwitch(first, second);
			}
		}
		System.out.println("New Total score " + totalScore);
	}
	
	public void printSchedule(){
		for (int i = 0; i < sub.length; i++) {
			try {
				System.out.println(sub[i].id + ": " + sub[i].lecture.id);
			} catch (Exception e) {
				System.out.println(sub[i].id + ": Free");
			}
			
		}
	}
	
	public void printMatrix(){
		for (int i = 0; i < sub.length; i++) {
			System.out.print(sub[i].id + " " + sub[i].ti.start + " ");
			for (int j = 0; j < sub[i].weigth.length; j++) {
				if(sub[i].weigth[j]> 0 && sub[i].weigth[j]<101)
				System.out.print(sub[i].weigth[j] + " ");
			}
			System.out.println();
		}
	}

	private int switchTwo(int first, int second){
		Substitute a = sub[first];
		Substitute b = sub[second];

		int changes = 0;

		int removeScore=0;
		int addScore = 0;

		if(b.weigth[a.index] > 0){

			removeScore += a.weigth[a.index];
			addScore += b.weigth[a.index];
			changes++;
		}

		if(a.weigth[b.index] > 0){
			removeScore += b.weigth[b.index];
			addScore += a.weigth[b.index];
			changes++;
		}

		if(changes == 2){

			int tempIndex = a.index;
			Lecture tempLec = a.lecture;

			a.setIndec(b.index);
			a.setLecture(b.lecture);

			b.setIndec(tempIndex);
			b.setLecture(tempLec);


			try {
				lecture[a.index].setSub(a);
			} catch (Exception e) {

			}
			try {
				lecture[b.index].setSub(b);
			} catch (Exception e) {
			}

			int tempSchedulePunish = calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture);

			int tempScore = matchScore;

			tempScore -= removeScore;
			tempScore += addScore;
			tempScore -= tempSchedulePunish;

			return tempScore;
		}
		return 0;
	}
	
	private void reverseSwitch(int first, int second){
		Substitute a = sub[first];
		Substitute b = sub[second];
		
		int tempIndex = b.index;
		Lecture tempLec = b.lecture;
		
		b.setIndec(a.index);
		b.setLecture(a.lecture);

		a.setIndec(tempIndex);
		a.setLecture(tempLec);

		try {
			lecture[a.index].setSub(a);
		} catch (Exception e) {

		}
		try {
			lecture[b.index].setSub(b);
		} catch (Exception e) {
		}
	}
	
	private Substitute[] findShortSub(){
		
		
		
		return null;
	}

}
