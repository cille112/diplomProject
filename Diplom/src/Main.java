
public class Main {

	public static void main(String[] args) {
		int [][] matrix = {{7,5,6,9},{8,7,7,5},{3,1,0,9},{7,5,5,9}};
		//int [][] matrix = {{1,0,0,1},{1,0,0,0},{0,0,0,1},{1,0,0,1}};
		jobAssignment job = new jobAssignment(matrix);
		//		int[][] matrixNew = new int[4][4];
		//		int[][] matrixUnderway = new int[4][4];
		//		boolean covered;
		//		int small;
		//		
		//		
		//		KuhnMunkres kuhn = new KuhnMunkres();
		//		
		//		matrixNew = kuhn.SmallestRow(matrix);
		//		matrixNew = kuhn.SmallestColumn(matrixNew);
		//		
		//		for (int i = 0; i < matrixNew.length; i++) {
		//			for (int j = 0; j < matrixNew.length; j++) {
		//				System.out.print(matrixNew[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		//		
		//		System.out.println();
		//		
		//		matrixUnderway = kuhn.markZeroes(matrixNew);
		//		
		//		for (int i = 0; i < matrixNew.length; i++) {
		//			for (int j = 0; j < matrixNew[0].length; j++) {
		//				System.out.print(matrixUnderway[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		//		
		//		covered = kuhn.allZeroMarked(matrixNew, matrixUnderway);
		//		System.out.println(covered);
		//		
		//		small = kuhn.findSmallest(matrixNew);
		//		System.out.println(small);
		//		
		//		matrixNew = kuhn.createZero(matrixNew, matrixUnderway, small);
		//		
		//		for (int i = 0; i < matrixNew.length; i++) {
		//			for (int j = 0; j < matrixNew.length; j++) {
		//				System.out.print(matrixNew[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		//		
		//		System.out.println();
		//		
		//		matrixUnderway = kuhn.markZeroes(matrixNew);
		//		
		//		for (int i = 0; i < matrixNew.length; i++) {
		//			for (int j = 0; j < matrixNew[0].length; j++) {
		//				System.out.print(matrixUnderway[i][j] + " ");
		//			}
		//			System.out.println();
		//		}
		//		
		//		covered = kuhn.allZeroMarked(matrixNew, matrixUnderway);
		//		System.out.println(covered);

		job.initialzeAuxiliray();
		job.makeEqualityGraph();
		job.makeInitialMatching();
		job.initialzeMarkingProcedure();
		job.markingProcedure();
		while(job.lengthPath != job.equalityGraph.length){
			job.updateAuxilirayNumbers();	
			job.resetMatching();
			job.makeEqualityGraph();
			job.makeInitialMatching();
			job.initialzeMarkingProcedure();
			job.markingProcedure();
		}
		job.printMatching();



		for (int i = 0; i < matrix.length; i++) {
			//System.out.println(job.sqaure[i]);
			//System.out.println(job.triangle[i]);
			//System.out.println(job.columnPicked[i]);
			//System.out.println(job.auxiliaryNumbersRow[i]);
			//System.out.println(job.auxiliaryNumbersColumn[i]);
			//System.out.println(job.rowPicked[i]);
			//System.out.println(job.pathNewColumn[i]);
			//System.out.println(job.pathNewRow[i]);
		}

		for (int f = 0; f < matrix.length; f++) {
			for (int i = 0; i < matrix.length; i++) {
				//System.out.print(job.equalityGraph[f][i]);
			}
			//System.out.println();
		}

		//System.out.println(job.epsilon);
		//System.out.println(job.lengthPath); 

	}

}
