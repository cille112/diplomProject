import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TimeInterval {

	static ArrayList<String> startList = new ArrayList<String>();
	static ArrayList<String> endList = new ArrayList<String>();
	LocalDateTime start;
	LocalDateTime end;
	String duration;

	public TimeInterval(String start, String end, String duration) {
		this.start = LocalDateTime.parse(start);
		this.end = LocalDateTime.parse(end);
		this.duration = duration;
		if(!startList.contains(start)){
			startList.add(start);
			endList.add(end);
		}

	}

	public TimeInterval(String start, String end) {
		this.start = LocalDateTime.parse(start);
		this.end = LocalDateTime.parse(end);
	}

	public boolean available(TimeInterval ti){
		if(start.compareTo(ti.start) != 1 && end.compareTo(ti.end) != -1){
			return true;
		}
		return false;
	}

	public boolean available(TimeInterval[] ti){
		for (int i = 0; i < ti.length; i++) {
			if(start.compareTo(ti[i].start) != -1 && end.compareTo(ti[i].end) != 1){
				return true;
			}
		}

		return false;
	}

	public int findDiffernec(TimeInterval ti) {
		LocalDateTime begin;
		LocalDateTime finish;

		if(start.compareTo(ti.start) == -1){
			begin = end;
			finish = ti.start;
		}
		else{
			begin = start;
			finish = ti.end;
		}

		
		long hours = begin.until( finish, ChronoUnit.HOURS);
		begin = begin.plusHours( hours );
		
		long minutes = begin.until( finish, ChronoUnit.MINUTES);
		begin = begin.plusMinutes( minutes );

		
		return (int) (hours*60+minutes);
	}
	
	public int getMinutes(){
		LocalDateTime begin = start;;
		LocalDateTime finish = end;
		
		long hours = begin.until( finish, ChronoUnit.HOURS);
		begin = begin.plusHours( hours );
		
		long minutes = begin.until( finish, ChronoUnit.MINUTES);
		begin = begin.plusMinutes( minutes );

		
		return (int) (hours*60+minutes);
	}

}
