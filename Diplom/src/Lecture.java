
public class Lecture {
	int id;
	String subject;
	String classid;
	int subGender;
	boolean ignoreSub;
	TimeInterval ti;
	int sub = -1;
	
	public Lecture(int id, String subject, String classId, int subGender, boolean ignoreSub, TimeInterval ti){
		this.id = id;
		this.subject = subject;
		this.classid = classId;
		this.subGender = subGender;
		this.ignoreSub = ignoreSub;
		this.ti = ti;
	}
	
	public void setSub(int sub){
		this.sub = sub;
	}
	
	

	
}
