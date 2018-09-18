public class jobAssignment {

	int[][] orginalMatrix;
	int[] auxiliaryNumbersRow;
	int[] auxiliaryNumbersColumn;
	int[][] equalityGraph;
	int[] columnPicked;
	int[] rowPicked;
	int[] sqaure;
	int[] triangle;
	boolean change = true;
	int epsilon = 100;
	int lengthPath = 0;
	int [] pathNewRow;
	int [] pathNewColumn;


	public jobAssignment(int[][]matrix){
		this.orginalMatrix = matrix;
		this.equalityGraph = new int[matrix.length][matrix.length];
		this.columnPicked = new int[matrix.length];
		this.rowPicked = new int[matrix.length];
		this.sqaure = new int[matrix.length];
		this.triangle = new int[matrix.length];
		this.pathNewRow = new int[matrix.length];
		this.pathNewColumn = new int [matrix.length];
	}

	public void initialzeAuxiliray(){
		auxiliaryNumbersColumn = new int[orginalMatrix.length];
		auxiliaryNumbersRow = new int[orginalMatrix.length];
		for (int i = 0; i < orginalMatrix.length; i++) {
			columnPicked[i] = -1;
			rowPicked[i] = -1;
			pathNewRow[i] = -1;
			pathNewColumn[i] = -1;
			int biggestNumber = 0;
			for (int j = 0; j < orginalMatrix.length; j++){
				if (biggestNumber < orginalMatrix[i][j])
					biggestNumber = orginalMatrix[i][j];
			}
			auxiliaryNumbersRow[i] = biggestNumber;
		}		
	}

	public void makeEqualityGraph(){
		for (int i = 0; i < orginalMatrix.length; i++) {
			for (int j = 0; j < orginalMatrix.length; j++) {
				if (orginalMatrix[i][j] == auxiliaryNumbersColumn[j] + auxiliaryNumbersRow[i])
					equalityGraph[i][j] = 1;
			}
		}
	}

	public void makeInitialMatching(){
		for (int i = 0; i < equalityGraph.length; i++) {
			for (int j = 0; j < equalityGraph.length; j++) {
				if (equalityGraph[i][j] == 1){
					if(rowPicked[j] == -1){
						columnPicked[i] = j;
						rowPicked[j] = i;
						lengthPath++;
						break;
					}
				}

			}
		}
	}
	


	public void initialzeMarkingProcedure(){
		for (int i = 0; i < sqaure.length; i++) {
			pathNewColumn[i] = -1;
			pathNewRow[i] = -1;
			if( columnPicked[i] == -1){
				sqaure[i] = 1;
			}
		}
	}

	public void markingProcedure(){
		change = true;
		while(change){
			change = false;
			stepOne();
		}
	}

	public void resetMatching(){
		for (int i = 0; i < auxiliaryNumbersColumn.length; i++) {
			rowPicked[i] = -1;
			columnPicked[i] = -1;
			sqaure[i] = 0;
			triangle[i] = 0;
		}
		lengthPath = 0;
	}
	
	public void printMatching(){
		int price = 0;
		for (int i = 0; i < auxiliaryNumbersColumn.length; i++) {
			price = price + orginalMatrix[i][columnPicked[i]];
		}
		System.out.println(price);
	}
	
	public void updateAuxilirayNumbers(){
		for (int i = 0; i < sqaure.length; i++) {
			if(sqaure[i] == 2){
				for (int j = 0; j < triangle.length; j++) {
					if(triangle[j] == 0){
						int eps = (auxiliaryNumbersColumn[j] + auxiliaryNumbersRow[i])-orginalMatrix[i][j];
						if(epsilon > eps){
							epsilon = eps;
						}
					}
				}
			}
		}
		for (int i = 0; i < sqaure.length; i++) {
			if(sqaure[i] == 2){
				auxiliaryNumbersRow[i] = auxiliaryNumbersRow[i] - epsilon;
			}
			if(triangle[i] == 2){
				auxiliaryNumbersColumn[i] = auxiliaryNumbersColumn[i] + epsilon;
			}
		}
	}

	private void stepOne(){
		for (int i = 0; i < sqaure.length; i++) {
			if (sqaure[i] == 1){
				sqaure[i] = 2;
				for (int j = 0; j < equalityGraph[i].length; j++) {
					if(equalityGraph[i][j] == 1 && triangle[j] == 0){
						triangle[j] = 1;
						change = true;
						pathNewColumn[i] = j;
						stepTwo();
					}
				}
			}
		}
	}

	private void stepOne(int i){
		sqaure[i] = 2;
		for (int j = 0; j < sqaure.length; j++) {
			if(equalityGraph[i][j] == 1 && triangle[j] == 0){
				triangle[j] = 1;
				change = true;
				pathNewColumn[i] = j;
				stepTwo();

			}
		}
	}

	private void stepTwo(){
		for (int i = 0; i < triangle.length; i++) {
			if(triangle[i] == 1){
				if(rowPicked[i] != -1){
					sqaure[rowPicked[i]] = 1;
					triangle[i] = 2;
					change = true;
					pathNewRow[i] = rowPicked[i];
					stepOne(i);
				}
				else{
					changePath();
					return;
				}
			}
		}
	}

	private void changePath(){
		for (int i = 0; i < pathNewColumn.length; i++) {
			if(pathNewColumn[i] != -1){
				columnPicked[i] = pathNewColumn[i];
				rowPicked[pathNewColumn[i]] = i;
			}
			pathNewColumn[i] = -1;
			pathNewRow[i] = -1;
		}
		lengthPath++;
	}

}




