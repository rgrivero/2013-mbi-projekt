package pca;

import java.util.ArrayList;
import java.util.Collections;

import Jama.EigenvalueDecomposition;
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
	public MatrixOperations(String fileName, String separator) {
		matrix = Utils.getInputCSVData(fileName, separator);
	}

	public MatrixOperations(String fileName) {
		matrix = Utils.getInputCSVData(fileName, ",");
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
	 *            zmiennosci danych, lub ilo�� nowych atrybut�w, w zale�no�ci od
	 *            flagi percentageThreshold
	 * @param percentageThreshold
	 *            - flaga okre�laj�ca czy parametr threshold ma by� u�ywany jako
	 *            pr�b procentowy (true), czy liczba nowych atrybut�w (false)
	 */
	public void run(String out, boolean corelation, double threshold,
			boolean percentageThreshold) {
		long startTime = System.currentTimeMillis();

		Matrix m = computePca(threshold, corelation, percentageThreshold);

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
	public Matrix computePca(double threshold, boolean corelation,
			boolean percentage) {
		Matrix m = corelation ? corelationMatrix(matrix.copy())
				: covarianceMatrix(matrix.copy());
		ArrayList<EigenValue> eig = computeEigenValues(m);
		eig = chooseNewAttributes(eig, threshold, percentage);
		m = createResults(eig);
		return castToMatrix(matrix, m);
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
	public Matrix castToMatrix(Matrix input, Matrix vectors) {
		Utils.logStart();
		Matrix m = input.times(vectors.transpose());
		Utils.logStop();
		Utils.savePartialResults(m);
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
		Matrix m = new Matrix(mat);

		Utils.logStop();
		Utils.savePartialResults(m);
		return m;
	}

	/**
	 * Wyliczanie �rednich warto�ci w kolumnach
	 * 
	 * @param matrix
	 *            - macierz wej�ciowa
	 * @return wektor ze �rednimi
	 */
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
	 *            - macierz wej�ciowa
	 * @return macierz kowariancji
	 */
	public Matrix covarianceMatrix(Matrix inputMatrix) {
		Utils.logStart();

		double[] means = getAvgCols(inputMatrix);
		subtractAvgValues(inputMatrix, means);

		Matrix resultMatrix = inputMatrix.transpose().times(inputMatrix);

		int resMatrixCols = resultMatrix.getColumnDimension();
		int resMatrixRows = resultMatrix.getRowDimension();

		for (int i = 0; i < resMatrixRows; i++)
			for (int j = 0; j < resMatrixCols; j++) {
				double value = resultMatrix.get(i, j);
				resultMatrix.set(i, j, value / resMatrixRows);
			}

		Utils.logStop();
		Utils.savePartialResults(resultMatrix);

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
		Utils.savePartialResults(resultMatrix);
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

		EigenvalueDecomposition dec = m.eig();
		double[] vals = dec.getRealEigenvalues();
		Matrix vectors = dec.getV();

		ArrayList<EigenValue> eig = new ArrayList<EigenValue>();
		for (int i = 0; i < vals.length; ++i) {
			eig.add(new EigenValue(vals[i]));
			eig.get(i).setVector(vectors, i);
		}

		Collections.sort(eig);
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
	public ArrayList<EigenValue> chooseNewAttributes(
			ArrayList<EigenValue> inputValues, double threshold,
			boolean percentage) {
		Utils.logStart();
		ArrayList<EigenValue> output = new ArrayList<EigenValue>();

		double sum = 0, cumulativeSum = 0;

		for (int i = 0; i < inputValues.size(); ++i)
			sum += inputValues.get(i).getValue();

		if (percentage)
			for (int i = 0; i < inputValues.size(); ++i) {
				cumulativeSum += inputValues.get(i).getValue();
				output.add(inputValues.get(i));
				if (cumulativeSum * 100 / sum >= threshold)
					break;
			}
		else
			output.addAll(inputValues.subList(0, (int) threshold));
		Utils.logStop();
		return output;
	}

	/**
	 * @return
	 */
	public Matrix getMatrix() {
		return matrix;
	}
}
