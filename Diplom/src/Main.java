import java.io.File;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class Main {

	public static void main(String[] args) {

		File folder = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\");

		for (File fileEntry : folder.listFiles()) {

		//File fileEntry = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\2018-09-14 07.01.05.json");
			
			System.out.println(fileEntry.getName());
			
			ScheduleCreater scheduleCreater = new ScheduleCreater(fileEntry.getAbsolutePath());
			
			scheduleCreater.preProcesing();
			//scheduleCreater.printMatrix();
			scheduleCreater.createInitialSchedule();
			System.out.println("First score: " + scheduleCreater.totalScore);
			//scheduleCreater.printSchedule();
			scheduleCreater.shortWorkDay();
			System.out.println("Short day: " + scheduleCreater.totalScore);
			scheduleCreater.doubleChanges();
			System.out.println("Double: " + scheduleCreater.totalScore);
			scheduleCreater.freePeriods();
			System.out.println("Free period: " + scheduleCreater.totalScore);
			//scheduleCreater.doubleChanges();
			//System.out.println("Double: " + scheduleCreater.totalScore);
			//scheduleCreater.randomChangeOne(0.3);
			//System.out.println("Random one: " + scheduleCreater.totalScore);
			//scheduleCreater.randomTwoChange(0.3);
			//System.out.println("Random two: " + scheduleCreater.totalScore);
			//scheduleCreater.printSchedule();
			System.out.println();
		}
	}
 
}
