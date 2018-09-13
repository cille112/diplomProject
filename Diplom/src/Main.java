public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [][] matrix = {{5,3,2,8},{7,9,2,6},{6,4,5,7},{5,7,7,8}};
		int[][] matrixNew = new int[4][4];
		int[][] matrixUnderway = new int[4][4];
		boolean covered;
		int small;
		
		
		KuhnMunkres kuhn = new KuhnMunkres();
		
		matrixNew = kuhn.SmallestRow(matrix);
		matrixNew = kuhn.SmallestColumn(matrixNew);
		
		for (int i = 0; i < matrixNew.length; i++) {
			for (int j = 0; j < matrixNew.length; j++) {
				System.out.print(matrixNew[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		matrixUnderway = kuhn.markZeroes(matrixNew);
		
		for (int i = 0; i < matrixNew.length; i++) {
			for (int j = 0; j < matrixNew[0].length; j++) {
				System.out.print(matrixUnderway[i][j] + " ");
			}
			System.out.println();
		}
		
		covered = kuhn.allZeroMarked(matrixNew, matrixUnderway);
		System.out.println(covered);
		
		small = kuhn.findSmallest(matrixNew);
		System.out.println(small);
		
		matrixNew = kuhn.createZero(matrixNew, matrixUnderway, small);
		
		for (int i = 0; i < matrixNew.length; i++) {
			for (int j = 0; j < matrixNew.length; j++) {
				System.out.print(matrixNew[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		matrixUnderway = kuhn.markZeroes(matrixNew);
		
		for (int i = 0; i < matrixNew.length; i++) {
			for (int j = 0; j < matrixNew[0].length; j++) {
				System.out.print(matrixUnderway[i][j] + " ");
			}
			System.out.println();
		}
		
		covered = kuhn.allZeroMarked(matrixNew, matrixUnderway);
		System.out.println(covered);
	}

}
