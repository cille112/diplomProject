import java.util.ArrayList;

public class Substitute {

	int id;
	TimeInterval ti;
	int[] weigth;
	Lecture lecture = null;
	int index;

	public Substitute(int id, TimeInterval ti, int[] weigth){
		this.id = id;
		this.ti = ti;
		this.weigth = weigth;
	}

	public void setLecture(Lecture lec){
		this.lecture = lec;
	}

	public void setIndec(int i){
		this.index = i;
	}

	public void setWeigth(int[] we){
		this.weigth = we;
	}

	public static  Substitute[] removeSubs(Substitute[] sub){

		ArrayList<Substitute> list = new ArrayList<Substitute>();

		for (int i = 0; i < sub.length; i++) {
			for (int j = 0; j < sub[i].weigth.length; j++) {
				if(!(sub[i].weigth[j] == 101 | sub[i].weigth[j] == 0)){
					list.add(sub[i]);
					break;
				}
			}
		}
		return list.toArray(new Substitute[list.size()]);
	}
	
	public static Lecture[] updateLecture(Substitute[] sub, int lec){
		Lecture[] lectures = new Lecture[lec];
		for (int i = 0; i < sub.length; i++) {
			if(sub[i].lecture !=null){
				Lecture temp = sub[i].lecture;
				temp.sub = i;
				lectures[sub[i].index] = temp;
			}
		}
		return lectures;
	}
}
