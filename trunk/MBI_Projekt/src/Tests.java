import Jama.Matrix;

public class Tests {
	public static void testLoadingMatrixFromFile(String fileName) {
		Matrix loadedMatrix = Utils.getInputCSVData(fileName);
		Utils.displayMatrix(loadedMatrix);
	}

	public static void testAll() {
		MatrixOperations mOp = new MatrixOperations("dane.csv");
		Utils.displayMatrix(mOp.getMatrix());
		double[] colsAvg = mOp.getAvgCols(mOp.getMatrix());

		for (int i = 0; i < colsAvg.length; i++)
			System.out.print("\t" + colsAvg[i]);

		Utils.displayMatrix(mOp.subtractAvgValues(mOp.getMatrix(), colsAvg));

		Utils.displayMatrix(mOp.covarianceMatrix(mOp.getMatrix()));

		// Utils.displayMatrix(mOp.computePCA());
	}
}
