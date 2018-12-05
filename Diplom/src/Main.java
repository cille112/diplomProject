import java.io.File;

public class Main {

	public static void main(String[] args) {

		File folder = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\");

		for (File fileEntry : folder.listFiles()) {

		//File fileEntry = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\2018-09-14 07.01.05.json");
			
			ScheduleCreater scheduleCreater = new ScheduleCreater(fileEntry.getAbsolutePath());
			
			scheduleCreater.preProcesing();
			//scheduleCreater.printMatrix();
			scheduleCreater.createInitialSchedule();
			System.out.println("First score: " + scheduleCreater.totalScore);
			//scheduleCreater.printSchedule();
			scheduleCreater.doubleChange();
			System.out.println("Double: " + scheduleCreater.totalScore);
			//scheduleCreater.shortWorkDay();
			//scheduleCreater.freePeriod();
			//scheduleCreater.randomChangeOne();
			//System.out.println("Random: " + scheduleCreater.totalScore);
			scheduleCreater.randomTwoChange();
			System.out.println("Random two: " + scheduleCreater.totalScore);
			//scheduleCreater.printSchedule();
			System.out.println();
		}
	}
 
}
