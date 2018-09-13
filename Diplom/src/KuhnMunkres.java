
public class KuhnMunkres {

	public int[][] SmallestRow (int[][] matrix){
		int [][] smallestNumber = new int[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			int smallNumber = 9000;
			for (int j = 0; j < matrix.length; j++){
				if (smallNumber > matrix[i][j])
					smallNumber = matrix[i][j];
			}
			for(int j = 0; j < matrix.length; j++){
				smallestNumber[i][j] = matrix[i][j]-smallNumber;
			}
			smallNumber = 0;
		}
		return smallestNumber;
	}
	
	public int[][] SmallestColumn (int[][] matrix){
		int [][] smallestNumber = new int[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			int smallNumber = 9000;
			for (int j = 0; j < matrix.length;j++){
				if (smallNumber > matrix[j][i])
					smallNumber = matrix[j][i];
			}
			for (int j = 0; j < matrix.length; j++) {
				smallestNumber[j][i] = matrix[j][i]-smallNumber;
			}
			smallNumber = 0;
		}
		return smallestNumber;
	}

	public int[][] markZeroes(int[][] matrix){
		int[][] marked = new int[matrix.length][matrix.length];
		int zeroPlace = 0;
		int numberOfZeroes = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 0 && marked[i][j] == 0){
					zeroPlace = j;
					numberOfZeroes++;
				}
			}
			if (numberOfZeroes == 1){
				for (int j = 0; j < matrix.length; j++) {
					marked[j][zeroPlace] = marked[j][zeroPlace]+1;
				}
			}			
			numberOfZeroes = 0;
			
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[j][i] == 0 && marked[j][i] == 0){
					zeroPlace = j;
					numberOfZeroes++;
				}
			}
			if (numberOfZeroes == 1){
				for (int j = 0; j < matrix.length; j++) {
					marked[zeroPlace][j] = marked[zeroPlace][j]+1;
				}
			}
			numberOfZeroes = 0;
		}
		
		return marked;
	}
	
	public boolean allZeroMarked(int[][] matrix, int[][] marked){
		for (int i = 0; i < marked.length; i++) {
			for (int j = 0; j < marked.length; j++) {
				if(matrix[i][j] == 0 && marked[i][j] == 0)
					return false;
			}
		}
		return true;
	}
	
	public int findSmallest(int[][]matrix){
		int smallest = 5;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] < smallest && matrix[i][j] > 0)
					smallest = matrix[i][j];
			}
		}
		return smallest;
	}
	
	public int[][] createZero(int[][] matrix, int[][] covered, int small){
		for (int  i= 0; i < covered.length; i++) {
			for (int j = 0; j < covered.length; j++) {
				if(covered[i][j] == 0)
					matrix[i][j] = matrix[i][j] - small;
				else if(matrix[i][j] == 2)
					matrix[i][j] = matrix[i][j] + small;
			}
		}
		
		return matrix;
	}
	
	public int[][] coverMore(int[][]matrix, int[][] marked){
		
		return marked;
	}
}
