import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {

		File folder = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\");

		for (File fileEntry : folder.listFiles()) {

		//File fileEntry = new File("C:\\Users\\cille_000\\Documents\\DTU\\DiplomProject\\tests\\2018-09-11 07.04.35.json");
		
			ScheduleCreater scheduleCreater = new ScheduleCreater(fileEntry.getAbsolutePath());
			
			scheduleCreater.preProcesing();
			//scheduleCreater.printMatrix();
			scheduleCreater.createInitialSchedule();
			//scheduleCreater.printSchedule();
			scheduleCreater.randomChangeOne();

		}
	}

}
