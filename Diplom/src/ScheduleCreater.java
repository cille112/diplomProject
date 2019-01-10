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
	int totalScore;

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
		TimeInterval.startList = new ArrayList<String>();
		TimeInterval.endList = new ArrayList<String>();
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
				lecture[jobAssignment.columnPicked[i]].setSub(i);
				sub[i].setLecture(lecture[jobAssignment.columnPicked[i]]);
				//System.out.println(sub[i].id + " " + lecture[job.columnPicked[i]].id + " " + i);
			} 
			catch (Exception e) {
			}
		}
		
		System.out.println("Match score: " + matchScore);
		
		doubleLecture = reader.findDoubleLectures(lecture);

		int schedulePunish;
		schedulePunish = calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture, sub);

		//System.out.println("Shc: " + calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + " dou: " + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture));

		totalScore = matchScore - schedulePunish;

		sub = Substitute.removeSubs(sub);
		lecture = Substitute.updateLecture(sub, lecture.length);
	}

	private void doubleChange(){
		ArrayList<Lecture[]> list = findDoubleNotSame();
		for (int i = 0; i < list.size(); i++) {
			Lecture a = list.get(i)[0];
			Lecture b = list.get(i)[1];

			boolean newScore = false;
			int scoreOne = 0;
			int scoreTwo = 0;
			if(a.ti.start.compareTo(b.ti.start) == -1){
				newScore = switchTwo(a.sub, b.sub-1, true);
				scoreOne = totalScore;
				if(newScore){
					reverseSwitch(a.sub, b.sub-1);
				}
				newScore = switchTwo(a.sub+1, b.sub, true);
				scoreTwo = totalScore;
				if(scoreOne > scoreTwo){
					if(newScore){
						reverseSwitch(a.sub+1, b.sub);
						switchTwo(a.sub, b.sub-1, true);
					}
				}
			}
			else{
				newScore = switchTwo(b.sub, a.sub-1, true);
				scoreOne = totalScore;
				if(newScore){
					reverseSwitch(b.sub, a.sub-1);
				}
				newScore = switchTwo(b.sub+1, a.sub, true);
				scoreTwo = totalScore;
				if(scoreOne > scoreTwo){
					if(newScore){
						reverseSwitch(b.sub+1, a.sub);
						switchTwo(b.sub, a.sub-1, true);
					}
				}
			}
		}
	}

	public void doubleChanges(){
		
		while(true){
			int tempScore = totalScore;
			doubleChange();
			if(tempScore == totalScore)
			break;
		}
	}
	
	public void randomChangeOne(double minute){
		final long NANOSEC_PER_SEC = 1000l*1000*1000;
		long startTime = System.nanoTime();
		while ((System.nanoTime()-startTime)< minute*60*NANOSEC_PER_SEC){
			int first = (int) Math.floor(Math.random() * lecture.length);


			first = sub[lecture[first].sub].index;

			Substitute[] same = sameTime(sub[first].ti, sub[first].id, false);
			int second = (int) Math.floor(Math.random() * same.length);

			second = same[second].ownIndex;

			switchTwo(first, second, true);
		}
	}

	public void randomTwoChange(double minutes){
		final long NANOSEC_PER_SEC = 1000l*1000*1000;

		long startTime = System.nanoTime();
		//for (int i = 0; i < 10000; i++) {
		while ((System.nanoTime()-startTime)< minutes*60*NANOSEC_PER_SEC){

			boolean firstSwitched = false;
			boolean secondSwitched = false;
			int tempTotal = totalScore;
			int tempmatchScore = matchScore;

			int first = (int) Math.floor(Math.random() * lecture.length);

			first = sub[lecture[first].sub].index;
			Substitute[] same = sameTime(sub[first].ti, sub[first].id, false);
			int second = (int) Math.floor(Math.random() * same.length);

			second = same[second].ownIndex;

			firstSwitched = switchTwo(first, second, false);
			int third = (int) Math.floor(Math.random() * lecture.length);

			third = sub[lecture[third].sub].index;

			same = sameTime(sub[third].ti, sub[third].id, false);
			int fourth = (int) Math.floor(Math.random() * same.length);

			fourth = same[fourth].ownIndex;

			secondSwitched = switchTwo(third, fourth, false);

			if(tempTotal >= totalScore){
				if(secondSwitched)
					reverseSwitch(third, fourth);
				if(firstSwitched)
					reverseSwitch(first, second);
				totalScore = tempTotal;
				matchScore = tempmatchScore;
			}
		}
	}

	public void shortWorkDay(){
		Substitute[] subs = findShortDay();
		ArrayList<Substitute> tmp = new ArrayList<>();
		//System.out.println(subs.length);
		if(subs.length == 0)
			return;
		int id = subs[0].id;
		for (int i = 0; i < subs.length; i++) {
			if(id == subs[i].id){
				tmp.add(subs[i]);
			}
			else{
				//System.out.println(tmp.size());
				removeLecture(tmp.toArray(new Substitute[tmp.size()]));
				tmp.clear();
				tmp.add(subs[i]);
				id = subs[i].id;
			}
		}
		removeLecture(tmp.toArray(new Substitute[tmp.size()]));
	}

	public void freePeriods(){
		while(true){
			int tempScore = totalScore;
			freePeriod();
			if(tempScore == totalScore)
			break;
		}
	}
	
	
	private void freePeriod(){
		Substitute[] subs = findFreePeriods();
		for (int i = 0; i < subs.length; i++) {
			ArrayList<Substitute> first = findFirstLast(subs[i].ti, subs[i].id);
			
			if(first.size()  != 0){
				for (int j = 0; j < first.size(); j++) {
					if(switchTwo(subs[i].ownIndex, first.get(j).ownIndex, true))
						break;
				}
				//System.out.println(subs[i].id + " " + subs[i].ti.start + " " + first.id + " " + first.ti.start);
			}
		}
	}


	public void printSchedule(){
		for (int i = 0; i < sub.length; i++) {
			try {
				System.out.println(sub[i].id + " " + sub[i].ti.start + ": " + sub[i].lecture.id + " " + " " + sub[i].weigth[sub[i].index]);
			} catch (Exception e) {
				System.out.println(sub[i].id + " " + sub[i].ti.start + ": Free");
			}

		}
	}

	public void printMatrix(){
		for (int i = 0; i < sub.length; i++) {
			//System.out.print(sub[i].id + " " + sub[i].ti.start + " ");
			for (int j = 0; j < sub.length; j++) {
				//if(sub[i].weigth[j]> 0 && sub[i].weigth[j]<101)
					try {
						System.out.print(sub[i].weigth[j] + " ");
					} catch (Exception e) {
						System.out.print(103 + " ");
					}
			}
			System.out.println();
		}
	}

	private boolean switchTwo(int first, int second, boolean reverse){
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
				lecture[a.index].setSub(first);
			} catch (Exception e) {
			}
			try {
				lecture[b.index].setSub(second);
			} catch (Exception e) {
			}

			int tempSchedulePunish = calculate.calculatePunishSchedule(sub, lecture, reader.timePen(), reader.gapPen()) + calculate.calculateDoublePen(reader.penDouble(), doubleLecture, lecture, sub);

			int tempScore = matchScore;

			tempScore -= removeScore;
			tempScore += addScore;
			tempScore -= tempSchedulePunish;


			if(totalScore <= tempScore){
				matchScore = matchScore - removeScore + addScore;
				totalScore = tempScore;
				return true;
			}
			if(reverse)
				reverseSwitch(first, second);
			else{
				totalScore = tempScore;
				matchScore = matchScore - removeScore + addScore;
				return true;
			}
		}
		return false;
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
			lecture[a.index].setSub(first);
		} catch (Exception e) {

		}
		try {
			lecture[b.index].setSub(second);
		} catch (Exception e) {
		}
	}


	private ArrayList<Lecture[]> findDoubleNotSame(){
		ArrayList<Lecture[]> lec = new ArrayList<>();
		for (int i = 0; i < doubleLecture.size(); i++) {
			try {
				if(!(sub[doubleLecture.get(i).get(0).sub].id == (sub[doubleLecture.get(i).get(1).sub].id))){
					Lecture[] temp = new Lecture[2];
					temp[0] = doubleLecture.get(i).get(0);
					temp[1] = doubleLecture.get(i).get(1);
					lec.add(temp);
				}
			} catch (Exception e) {
				return null;
			}
		}		
		return lec;
	}

	private Substitute[] sameTime(TimeInterval ti, int id, boolean free){
		ArrayList<Substitute> list = new ArrayList<>();
		for (int i = 0; i < sub.length; i++) {
			if(sub[i].ti.start.equals(ti.start) && id != sub[i].id)
				if(!free)
					list.add(sub[i]);
				else{
					if(sub[i].index > lecture.length-1){
						list.add(sub[i]);
					}
				}
		}
		return list.toArray(new Substitute[list.size()]);
	}

	private Substitute[] findShortDay(){
		ArrayList<Substitute> list = new ArrayList<>();
		ArrayList<Substitute> tmpList = new ArrayList<>();
		int id = -1;
		int tempTime = 0;
		for (int i = 0; i < sub.length; i++) {
			if(id != sub[i].id){
				if(tempTime > 0  && tempTime < 91){
					tmpList.addAll(list);
					list.clear();
				}
				id = sub[i].id;
				tempTime = 0;
				list.clear();
			}
			if(sub[i].lecture != null){
				list.add(sub[i]);
				tempTime += sub[i].ti.getMinutes();
			}

		}

		if(tempTime > 0  && tempTime < 91){
			tmpList.addAll(list);
			list.clear();
		}
		return tmpList.toArray(new Substitute[tmpList.size()]);
	}

	private Substitute[] findFreePeriods(){
		int id = -1;
		int indexOne = -1;
		int indexTwo = -1;
		ArrayList<Substitute> list = new ArrayList<>();
		for (int i = 0; i < sub.length; i++) {
			if(id != sub[i].id){
				id = sub[i].id;
				indexOne = -1;
				indexTwo = -1;
				if(sub[i].lecture != null)
					indexOne = i;
			}
			if(sub[i].lecture != null){
				if(indexOne == -1)
					indexOne =
					i;
				indexTwo = i;
			}
			if(indexTwo - indexOne > 1){
				for (int j = indexOne+1; j < indexTwo; j++) {
					list.add(sub[j]);
				}
			}
			indexOne = indexTwo;
		}
		return list.toArray(new Substitute[list.size()]);
	}

	private void removeLecture(Substitute[] subs){
		ArrayList<Substitute> switched = new ArrayList<>();
		int tmpTotal = totalScore;
		int tmpMatch = matchScore;
		for (int i = 0; i < subs.length; i++) {
			Substitute[] temp = sameTime(subs[i].ti,subs[i].id, true);
			for (int j = 0; j < temp.length; j++) {
				if(switchTwo(subs[i].ownIndex, temp[j].ownIndex, false)){
					switched.add(temp[j]);
					break;
				}
			}
			switched.add(null);
		}
		if(tmpTotal > totalScore){
			for (int i = subs.length-1; i >= 0; i--){
				if(!(switched.get(i) == null))
					reverseSwitch(subs[i].ownIndex, switched.get(i).ownIndex);
			}
			matchScore = tmpMatch;
			totalScore = tmpTotal;
			findMoreLectures(subs);
			//System.out.println("Give up");
		}
	}
	private void findMoreLectures(Substitute[] subs){

	}

	private ArrayList<Substitute> findFirstLast(TimeInterval ti, int id){
		Substitute[] subs = sameTime(ti, id, false);
		ArrayList<Substitute> list = new ArrayList<>();
		for (int i = 0; i < subs.length; i++) {
			//System.out.println(subs[i].id + " " + subs[i].ti.start + " " + firstOrLast(subs[i]));
			if(!(subs[i].index > lecture.length-1)){
				if(firstOrLast(subs[i]))
					list.add(subs[i]);
			}
		}
		return list;
	}

	private boolean firstOrLast(Substitute substi){
		int id = substi.id;

		boolean foundClass = false;

		//First class?
		for (int i = substi.ownIndex-1; i > -1; i--) {
			if(id == sub[i].id){	
				if(!(sub[i].index > lecture.length-1)){
					foundClass = true;
					break;
				}
			}
			else
				break;
		}


		if(!foundClass)
			return true;

		foundClass = false;
		//Last class?
		for (int i = substi.ownIndex+1; i < sub.length; i++) {
			if(id == sub[i].id){	
				if(!(sub[i].index > lecture.length-1)){
					foundClass = true;
					break;
				}
			}
			else
				break;
		}

		if(!foundClass)
			return true;

		return false;
	}
}
