import java.time.LocalDateTime;
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
	
}
