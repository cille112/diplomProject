public class JobAssignment {

	int[][] orginalMatrix;
	int[] auxiliaryNumbersRow;
	int[] auxiliaryNumbersColumn;
	int[][] equalityGraph;
	int[] columnPicked;
	int[] rowPicked;
	int[] sqaure;
	int[] triangle;
	boolean change;
	int epsilon = 1000;
	int lengthPath = 0;
	int [] pathNewRow;
	int [] pathNewColumn;
	boolean backTostepOne = false;


	public JobAssignment(int[][]matrix){
		this.orginalMatrix = matrix;
		this.equalityGraph = new int[matrix.length][matrix.length];
		this.columnPicked = new int[matrix.length];
		this.rowPicked = new int[matrix.length];
		this.sqaure = new int[matrix.length];
		this.triangle = new int[matrix.length];
		this.pathNewRow = new int[matrix.length];
		this.pathNewColumn = new int [matrix.length];
		this.auxiliaryNumbersColumn = new int[orginalMatrix.length];
		this.auxiliaryNumbersRow = new int[orginalMatrix.length];
	}

	public void initialzeAuxiliray(){
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
				equalityGraph[i][j] = 0;
				if (orginalMatrix[i][j] == auxiliaryNumbersColumn[j] + auxiliaryNumbersRow[i]){
					equalityGraph[i][j] = 1;	
				}
			}
		}
	}

	public void makeInitialMatching(){
		for (int i = 0; i < equalityGraph.length; i++) {
			for (int j = 0; j < equalityGraph.length; j++) {
				if (equalityGraph[i][j] == 1){
					if(rowPicked[j] == -1 && columnPicked[i] == -1){
						columnPicked[i] = j;
						rowPicked[j] = i;
						//System.out.println("Picked: " + i +" " +j);
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
		do {
			change = false;
			stepOne();
		} while (change);

	}

	public void resetMatching(){
		for (int i = 0; i < auxiliaryNumbersColumn.length; i++) {
			sqaure[i] = 0;
			triangle[i] = 0;
		}
		epsilon = 1000;
		backTostepOne = false;
	}

	public int printMatching(){
		int price = 0;
		for (int i = 0; i < auxiliaryNumbersColumn.length; i++) {
			if(orginalMatrix[i][columnPicked[i]] != 101)	
				price = price + orginalMatrix[i][columnPicked[i]];
		}
		return price;
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
			if(sqaure[i] != 0){
				auxiliaryNumbersRow[i] = auxiliaryNumbersRow[i] - epsilon;
			}
			if(triangle[i] == 1){
				auxiliaryNumbersColumn[i] = auxiliaryNumbersColumn[i] + epsilon;
			}
		}
	}

	private void stepOne(){
		for (int i = 0; i < sqaure.length; i++) {
			if (sqaure[i] == 1){
				sqaure[i] = 2;
				for (int j = 0; j < equalityGraph.length; j++) {
					if(equalityGraph[i][j] == 1 && triangle[j] == 0){
						triangle[j] = 1;
						change = true;
						pathNewColumn[i] = j;
						stepTwo(j);
						if(backTostepOne){
							backTostepOne = false;
							break;
						}
					}
				}
			}
			pathNewColumn[i] = -1;

		}
	}

	private void stepOne(int i){
			sqaure[i] = 2;
			for (int j = 0; j < sqaure.length; j++) {
				if(equalityGraph[i][j] == 1 && triangle[j] == 0){
					triangle[j] = 1;
					change = true;
					pathNewColumn[i] = j;
					stepTwo(j);
					if(backTostepOne)
						return;
				}
			}
			pathNewColumn[i] = -1;
	}

	private void stepTwo(int i){
		if(backTostepOne)
			return;
			if(rowPicked[i] != -1){
				sqaure[rowPicked[i]] = 1;
				change = true;
				pathNewRow[i] = rowPicked[i];
				stepOne(rowPicked[i]);
			}
			else{
				changePath();
			}
			pathNewRow[i] = -1;
		}
	

	private void changePath(){
		backTostepOne = true;
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




