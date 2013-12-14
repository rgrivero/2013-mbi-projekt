import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MatrixOperations {
	Matrix matrix;

	public MatrixOperations(String fileName) {
		matrix = Utils.getInputCSVData(fileName);
	}

	/* TODO */
	public Matrix computePCA() {
		Matrix finalMatrix = null;
		Matrix m = covarianceMatrix(matrix);
		EigenvalueDecomposition ed = m.eig();

		return finalMatrix;
	}

	public double[] getAvgRows(Matrix matrix) {
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		double vector[] = new double[rows];

		for (int i = 0; i < rows; i++) {
			double sum = 0;

			for (int j = 0; j < cols; j++)
				sum += matrix.get(i, j);

			vector[i] = sum / cols;
		}
		return vector;
	}

	public double[] getAvgCols(Matrix matrix) {
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		double vector[] = new double[cols];

		for (int i = 0; i < cols; i++) {
			double sum = 0;

			for (int j = 0; j < rows; j++)
				sum += matrix.get(j, i);

			vector[i] = sum / rows;
		}
		return vector;
	}

	public Matrix subtractAvgValues(Matrix matrix, double[] vector) {
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				matrix.set(i, j, matrix.get(i, j) - vector[j]);

		return matrix;
	}

	public Matrix covarianceMatrix(Matrix inputMatrix) {
		// int cols = inputMatrix.getColumnDimension();

		double[] means = getAvgCols(inputMatrix);
		subtractAvgValues(inputMatrix, means);

		Matrix transposedMatrix = inputMatrix.transpose();

		Matrix resultMatrix = inputMatrix.times(transposedMatrix);

		int resMatrixCols = resultMatrix.getColumnDimension();
		int resMatrixRows = resultMatrix.getRowDimension();

		for (int i = 0; i < resMatrixCols; i++)
			for (int j = 0; j < resMatrixCols; j++) {
				double value = resultMatrix.get(i, j);
				resultMatrix.set(i, j, value / resMatrixRows);
			}

		return resultMatrix;
	}

	// inputMatrix -
	public Matrix sumOfSquaredMatrix(Matrix inputMatrix) {
		// srednie
		double[] means = getAvgCols(inputMatrix);
		// odejmujemy srednie od wartosci
		subtractAvgValues(inputMatrix, means);
		// macierz transponowana (potrzebna do mnozenia)
		Matrix transposedMatrix = inputMatrix.transpose();
		// mnozenie macierzy, w celu uzyskania macierzy sum kwadratów
		Matrix resultMatrix = transposedMatrix.times(inputMatrix);
		double n = (double)1 / (resultMatrix.getColumnDimension() - 1);
		return resultMatrix.times(n);
	}

	public Matrix corelationMatrix(Matrix inputMatrix) {
		Matrix sum = sumOfSquaredMatrix(inputMatrix);
		int cols = sum.getColumnDimension();
		Matrix resultMatrix = Matrix.identity(cols, cols);
		for(int i = 0;i< cols;++i)
			for(int j = 0;j< cols;++j)
			{
				if(i==j)
					continue;
				resultMatrix.set(i, j, sum.get(i, j)/Math.sqrt(sum.get(i, i)*sum.get(j,j)));
			}
		return resultMatrix;
	}

	//
	public Matrix computeEigenValuesForCovMatrix(EigenvalueDecomposition ed) {
		return ed.getD();
	}

	public Matrix computeEigenVectorForCovMatrix(EigenvalueDecomposition ed) {
		return ed.getV();
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
}
