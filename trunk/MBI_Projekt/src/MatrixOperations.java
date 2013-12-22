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
	 *            - plik z danymi wej�ciowymi
	 */
	public MatrixOperations(String fileName) {
		matrix = Utils.getInputCSVData(fileName);
	}

	/**
	 * Uruchamia mechanizm redukcji wymiar�w
	 * 
	 * @param out
	 *            - nazwa pliku wyj�ciowego
	 * @param corelation
	 *            - czy u�ywa� macierzy korelacji zamiast kowariancji
	 * @param threshold
	 *            - pr�g (procentowy) pokrycia minimalnej skumulowanej
	 *            zmiennosci danych
	 */
	public void run(String out, boolean corelation, double threshold) {
		long startTime = System.currentTimeMillis();

		Matrix m = computePca(threshold, corelation);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		System.out.println("\nComputed in :\t" + totalTime + "ms");
		System.out.println("Input attributes: " + matrix.getColumnDimension()
				+ ", New attributes: " + m.getColumnDimension());
		// Utils.displayMatrix(m);
		Utils.saveMatrix(m, out);
	}

	/**
	 * Oblicza nowe warto�ci wsp�czynnik�w
	 * 
	 * @param threshold
	 *            - pr�g procentowy do wyboru warto�ci w�asnych
	 * @param corelation
	 *            - czy u�ywa� macierzy korelacji zamiast kowariancji
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
	 * Rzutowanie warto�ci danych wej�ciowych na przestrze� nowych atrybut�w
	 * (mno�enie macierzy)
	 * 
	 * @param input
	 *            - dane wej�ciowe
	 * @param vectors
	 *            - wsp�czynniki nowych atrybut�w
	 * @return
	 */
	public Matrix cast(Matrix input, Matrix vectors) {
		Utils.logStart();
		Matrix m = input.times(vectors.transpose());
		Utils.logStop();
		return m;
	}

	/**
	 * Tworzenie macierzy wsp�czynnik�w nowych atrybut�w na podstawie listy
	 * wektor�w w�asnych
	 * 
	 * @param eig
	 *            - lista wektor�w w�asnych
	 * @return - macierz wsp�czynnik�w do wyliczania nowych warto�ci atrybut�w
	 */
	public Matrix createResults(ArrayList<EigenValue> eig) {
		Utils.logStart();
		double[][] mat = new double[eig.size()][eig.get(0).getVector().length];

		for (int i = 0; i < eig.size(); ++i)
			mat[i] = eig.get(i).getVector();
		Utils.logStop();
		return new Matrix(mat);
	}

	/**
	 * Wyliczanie �rednich warto�ci w wierszach
	 * 
	 * @param matrix
	 *            - macierz wej�ciowa
	 * @return - wektor ze �rednimi
	 */
	public double[] getAvgRows(Matrix matrix) {
		Utils.logStart();
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		double vector[] = new double[rows];

		for (int i = 0; i < rows; i++) {
			double sum = 0;

			for (int j = 0; j < cols; j++)
				sum += matrix.get(i, j);

			vector[i] = sum / cols;
		}

		Utils.logStop();
		return vector;
	}

	/**
	 * Wyliczanie �rednich warto�ci w kolumnach
	 * 
	 * @param matrix
	 *            - macierz wej�ciowa
	 * @return wektor ze �rednimi
	 */
	public double[] getAvgCols(Matrix matrix) {
		Utils.logStart();
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		double vector[] = new double[cols];

		for (int i = 0; i < cols; i++) {
			double sum = 0;

			for (int j = 0; j < rows; j++)
				sum += matrix.get(j, i);

			vector[i] = sum / rows;
		}
		Utils.logStop();
		return vector;
	}

	/**
	 * Odejmowanie od aktualnych warto�ci warto�ci �rednich w kolumnach
	 * 
	 * @param matrix
	 *            - macierz wej�ciowa
	 * @param vector
	 *            - wektor ze �rednimi w kolumnach
	 * @return
	 */
	public Matrix subtractAvgValues(Matrix matrix, double[] vector) {
		Utils.logStart();
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				matrix.set(i, j, matrix.get(i, j) - vector[j]);

		Utils.logStop();
		return matrix;
	}

	/**
	 * Wyznaczanie macierzy kowariancji
	 * 
	 * @param inputMatrix
	 *            - macierz wej�ciowa
	 * @return macierz kowariancji
	 */
	public Matrix covarianceMatrix(Matrix inputMatrix) {
		Utils.logStart();
		// int cols = inputMatrix.getColumnDimension();

		double[] means = getAvgCols(inputMatrix);
		subtractAvgValues(inputMatrix, means);

		Matrix transposedMatrix = inputMatrix.transpose();

		Matrix resultMatrix = inputMatrix.times(transposedMatrix);

		int resMatrixCols = resultMatrix.getColumnDimension();
		int resMatrixRows = resultMatrix.getRowDimension();

		for (int i = 0; i < resMatrixRows; i++)
			for (int j = 0; j < resMatrixCols; j++) {
				double value = resultMatrix.get(i, j);
				resultMatrix.set(i, j, value / resMatrixRows);
			}
		Utils.logStop();

		return resultMatrix;
	}

	// macierz sum iloczyn�w kolumn
	/**
	 * Wyznaczanie macierzy sumy iloczyn�w warto�ci w kolumnach przemno�onych
	 * przez 1/(n-1)
	 * 
	 * @param inputMatrix
	 * @return
	 */
	public Matrix sumOfSquaredMatrix(Matrix inputMatrix) {
		Utils.logStart();
		// srednie
		double[] means = getAvgCols(inputMatrix);
		// odejmujemy srednie od wartosci
		subtractAvgValues(inputMatrix, means);
		// macierz transponowana (potrzebna do mnozenia)
		Matrix transposedMatrix = inputMatrix.transpose();
		// mnozenie macierzy, w celu uzyskania macierzy sum kwadrat�w
		Matrix resultMatrix = transposedMatrix.times(inputMatrix);
		double n = (double) 1 / (resultMatrix.getColumnDimension() - 1);
		Matrix m = resultMatrix.times(n);
		Utils.logStop();

		return m;
	}

	/**
	 * Wyznaczanie macierzy korelacji
	 * 
	 * @param inputMatrix
	 *            - macierz wej�ciowa
	 * @return
	 */
	public Matrix corelationMatrix(Matrix inputMatrix) {
		Utils.logStart();
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
		Utils.logStop();
		return resultMatrix;
	}

	/**
	 * Wyznaczanie warto�ci i wektor�w w�asnych
	 * 
	 * @param m
	 * @return
	 */
	public ArrayList<EigenValue> computeEigenValues(Matrix m) {
		Utils.logStart();
		double[] vals = m.eig().getRealEigenvalues();
		Utils.logStop();

		Utils.logStart();
		ArrayList<EigenValue> eig = new ArrayList<EigenValue>();
		for (int i = 0; i < vals.length; ++i)
			eig.add(new EigenValue(vals[i]));
		Utils.logStop();

		Utils.logStart();
		Matrix vectors = m.eig().getV();
		Utils.logStop();

		Utils.logStart();
		for (int i = 0; i < vals.length; ++i)
			eig.get(i).setVector(vectors, i);
		Utils.logStop();

		Utils.logStart();
		Collections.sort(eig);
		Utils.logStop();

		Utils.logStart();
		Collections.reverse(eig);
		Utils.logStop();
		return eig;
	}

	/**
	 * Warto�ci i wektory w�asne ponad zadanym progiem
	 * 
	 * @param inputValues
	 * @param threshold
	 * @return
	 */
	public ArrayList<EigenValue> computeEigenValues(
			ArrayList<EigenValue> inputValues, double threshold) {
		Utils.logStart();
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
		Utils.logStop();
		return output;
	}

	/**
	 * Przeksztalca liste wektorow wlasnych w macierz, gdzie kazdy wiersz to
	 * osobny wektor wlasny
	 * 
	 * @param inputValues
	 *            - lista wektorow wlasnych
	 * @return - macierz zlozona z wketorow wlasnych
	 */
	public Matrix createRowFeatureMatrix(ArrayList<EigenValue> inputValues) {
		int numberOfRows = inputValues.size();
		int numberOfCols = inputValues.get(0).getVector().length;

		Matrix featureMatrix = new Matrix(numberOfRows, numberOfCols);

		for (int i = 0; i < inputValues.size(); i++) {
			double[] vector = inputValues.get(i).getVector();
			for (int j = 0; j < vector.length; j++)
				featureMatrix.set(i, j, vector[j]);
		}

		return featureMatrix;// .transpose();
	}

	/**
	 * @return
	 */
	public Matrix getMatrix() {
		return matrix;
	}
}
