import java.util.ArrayList;
import java.util.Collections;

import Jama.Matrix;

/**
 *
 *
 */
public class MatrixOperations {
	Matrix matrix;

	/**
	 * Konstruktor
	 * 
	 * @param fileName
	 *            - plik z danymi wejœciowymi
	 */
	public MatrixOperations(String fileName) {
		matrix = Utils.getInputCSVData(fileName);
	}

	/**
	 * Uruchamia mechanizm redukcji wymiarów
	 * 
	 * @param out
	 *            - nazwa pliku wyjœciowego
	 * @param corelation
	 *            - czy u¿ywaæ macierzy korelacji zamiast kowariancji
	 * @param threshold
	 *            - próg (procentowy) pokrycia minimalnej skumulowanej
	 *            zmiennosci danych
	 */
	public void run(String out, boolean corelation, double threshold) {

		Matrix m = computePca(threshold, corelation);
		Utils.displayMatrix(m);
		Utils.saveMatrix(m, out);
	}

	/**
	 * Oblicza nowe wartoœci wspó³czynników
	 * 
	 * @param threshold
	 *            - próg procentowy do wyboru wartoœci w³asnych
	 * @param corelation
	 *            - czy u¿ywaæ macierzy korelacji zamiast kowariancji
	 * @return
	 */
	public Matrix computePca(double threshold, boolean corelation) {
		Matrix m = corelation ? corelationMatrix(matrix.copy())
				: covarianceMatrix(matrix.copy());
		ArrayList<EigenValue> eig = computeEigenValues(m);
		eig = computeEigenValues(eig, threshold);
		m = createResults(eig);
		return cast(matrix, m);
	}

	/**
	 * Rzutowanie wartoœci danych wejœciowych na przestrzeñ nowych atrybutów
	 * (mno¿enie macierzy)
	 * 
	 * @param input
	 *            - dane wejœciowe
	 * @param vectors
	 *            - wspó³czynniki nowych atrybutów
	 * @return
	 */
	public Matrix cast(Matrix input, Matrix vectors) {
		Utils.log("Cast input matrix into new attributes");
		return input.times(vectors.transpose());
	}

	/**
	 * Tworzenie macierzy wspó³czynników nowych atrybutów na podstawie listy
	 * wektorów w³asnych
	 * 
	 * @param eig
	 *            - lista wektorów w³asnych
	 * @return - macierz wspó³czynników do wyliczania nowych wartoœci atrybutów
	 */
	public Matrix createResults(ArrayList<EigenValue> eig) {
		Utils.log("Create matrix from list of eigen vectors");
		double[][] mat = new double[eig.size()][eig.get(0).getVector().length];

		for (int i = 0; i < eig.size(); ++i)
			mat[i] = eig.get(i).getVector();
		return new Matrix(mat);
	}

	/**
	 * Wyliczanie œrednich wartoœci w wierszach
	 * 
	 * @param matrix
	 *            - macierz wejœciowa
	 * @return - wektor ze œrednimi
	 */
	public double[] getAvgRows(Matrix matrix) {
		Utils.log("Compute average values for rows");
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

	/**
	 * Wyliczanie œrednich wartoœci w kolumnach
	 * 
	 * @param matrix
	 *            - macierz wejœciowa
	 * @return wektor ze œrednimi
	 */
	public double[] getAvgCols(Matrix matrix) {
		Utils.log("Compute average values for cols");
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

	/**
	 * Odejmowanie od aktualnych wartoœci wartoœci œrednich w kolumnach
	 * 
	 * @param matrix
	 *            - macierz wejœciowa
	 * @param vector
	 *            - wektor ze œrednimi w kolumnach
	 * @return
	 */
	public Matrix subtractAvgValues(Matrix matrix, double[] vector) {
		Utils.log("Subtract average values from current values");
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				matrix.set(i, j, matrix.get(i, j) - vector[j]);

		return matrix;
	}

	/**
	 * Wyznaczanie macierzy kowariancji
	 * 
	 * @param inputMatrix
	 *            - macierz wejœciowa
	 * @return macierz kowariancji
	 */
	public Matrix covarianceMatrix(Matrix inputMatrix) {
		Utils.log("Compute covariance matrix");
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

	// macierz sum iloczynów kolumn
	/**
	 * Wyznaczanie macierzy sumy iloczynów wartoœci w kolumnach przemno¿onych
	 * przez 1/(n-1)
	 * 
	 * @param inputMatrix
	 * @return
	 */
	public Matrix sumOfSquaredMatrix(Matrix inputMatrix) {
		Utils.log("Compute matrix from sum of squared values");
		// srednie
		double[] means = getAvgCols(inputMatrix);
		// odejmujemy srednie od wartosci
		subtractAvgValues(inputMatrix, means);
		// macierz transponowana (potrzebna do mnozenia)
		Matrix transposedMatrix = inputMatrix.transpose();
		// mnozenie macierzy, w celu uzyskania macierzy sum kwadratów
		Matrix resultMatrix = transposedMatrix.times(inputMatrix);
		double n = (double) 1 / (resultMatrix.getColumnDimension() - 1);
		return resultMatrix.times(n);
	}

	/**
	 * Wyznaczanie macierzy korelacji
	 * 
	 * @param inputMatrix
	 *            - macierz wejœciowa
	 * @return
	 */
	public Matrix corelationMatrix(Matrix inputMatrix) {
		Utils.log("Compute corelation matrix");
		Matrix sum = sumOfSquaredMatrix(inputMatrix);
		int cols = sum.getColumnDimension();
		Matrix resultMatrix = Matrix.identity(cols, cols);
		for (int i = 0; i < cols; ++i)
			for (int j = 0; j < cols; ++j) {
				if (i == j)
					continue;
				resultMatrix.set(
						i,
						j,
						sum.get(i, j)
								/ Math.sqrt(sum.get(i, i) * sum.get(j, j)));
			}
		return resultMatrix;
	}

	/**
	 * Wyznaczanie wartoœci i wektorów w³asnych
	 * 
	 * @param m
	 * @return
	 */
	public ArrayList<EigenValue> computeEigenValues(Matrix m) {
		Utils.log("Compute eigen values");
		double[] vals = m.eig().getRealEigenvalues();
		ArrayList<EigenValue> eig = new ArrayList<EigenValue>();
		for (int i = 0; i < vals.length; ++i)
			eig.add(new EigenValue(vals[i]));

		Matrix vectors = m.eig().getV();
		for (int i = 0; i < vals.length; ++i)
			eig.get(i).setVector(vectors, i);

		Collections.sort(eig);
		Collections.reverse(eig);
		return eig;
	}

	/**
	 * Wartoœci i wektory w³asne ponad zadanym progiem
	 * 
	 * @param inputValues
	 * @param threshold
	 * @return
	 */
	public ArrayList<EigenValue> computeEigenValues(
			ArrayList<EigenValue> inputValues, double threshold) {
		Utils.log("Choose eigen values under the threshold");
		ArrayList<EigenValue> output = new ArrayList<EigenValue>();

		double sum = 0, cumulativeSum = 0;

		for (int i = 0; i < inputValues.size(); ++i)
			sum += inputValues.get(i).getValue();

		for (int i = 0; i < inputValues.size(); ++i) {
			cumulativeSum += inputValues.get(i).getValue();
			output.add(inputValues.get(i));
			if (cumulativeSum * 100 / sum >= threshold)
				break;
		}
		return output;
	}

	/**
	 * @return
	 */
	public Matrix getMatrix() {
		return matrix;
	}
}
