
public class jobAssignment {
	
	int[][] orginalMatrix;
	int[] auxiliaryNumbersRow;
	int[] auxiliaryNumbersColumn;
	
	
	public jobAssignment(int[][]matrix){
		this.orginalMatrix = matrix;
	}
	
	public void initialzeAuxiliray(){
		auxiliaryNumbersColumn = new int[orginalMatrix.length];
		auxiliaryNumbersRow = new int[orginalMatrix.length];
		for (int i = 0; i < orginalMatrix.length; i++) {
			int biggestNumber = 0;
			for (int j = 0; j < orginalMatrix.length; j++){
				if (biggestNumber < orginalMatrix[i][j])
					biggestNumber = orginalMatrix[i][j];
			}
			auxiliaryNumbersRow[i] = biggestNumber;
		}		
	}
	
}
