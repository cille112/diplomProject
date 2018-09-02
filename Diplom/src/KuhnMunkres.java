
public class KuhnMunkres {

	public int[] SmallestRow (int[][] matrix){
		int [] smallestNumber = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			int smallNumber = 0;
			for (int j = 0; j < matrix.length;i++){
				if (smallNumber < matrix[i][j])
					smallNumber = matrix[i][j];
			}
			smallestNumber[i] = smallNumber;
			smallNumber = 0;
		}
		return smallestNumber;
	}
	
	public int[] SmallestColumn (int[][] matrix){
		int [] smallestNumber = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			int smallNumber = 0;
			for (int j = 0; j < matrix.length;i++){
				if (smallNumber < matrix[j][i])
					smallNumber = matrix[j][i];
			}
			smallestNumber[i] = smallNumber;
			smallNumber = 0;
		}
		return smallestNumber;
	}
	
}
