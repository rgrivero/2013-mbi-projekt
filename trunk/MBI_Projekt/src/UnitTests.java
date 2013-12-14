import Jama.Matrix;
import junit.framework.Assert;

public class UnitTests {

	MatrixOperations operations;

	@org.junit.Before
	public void testInit() {
		operations = new MatrixOperations("testData.csv");
	}

	@org.junit.Test
	public void constructorTest() {
		operations = new MatrixOperations("testData.csv");
		Assert.assertEquals(1.0, operations.matrix.get(0, 0));
		Assert.assertEquals(5.0, operations.matrix.get(0, 1));
		Assert.assertEquals(4.0, operations.matrix.get(0, 2));
		Assert.assertEquals(1.0, operations.matrix.get(0, 3));
		Assert.assertEquals(3.0, operations.matrix.get(1, 0));
		Assert.assertEquals(3.0, operations.matrix.get(1, 1));
		Assert.assertEquals(0.0, operations.matrix.get(1, 2));
		Assert.assertEquals(2.0, operations.matrix.get(1, 3));
		Assert.assertEquals(6.0, operations.matrix.get(2, 0));
		Assert.assertEquals(1.0, operations.matrix.get(2, 1));
		Assert.assertEquals(1.0, operations.matrix.get(2, 2));
		Assert.assertEquals(9.0, operations.matrix.get(2, 3));
	}

	@org.junit.Test
	public void getAvgColsTest() {
		double[] results = operations.getAvgCols(operations.matrix);

		Assert.assertEquals(4, results.length);
		Assert.assertEquals((double) 10 / 3, results[0]);
		Assert.assertEquals((double) 3, results[1]);
		Assert.assertEquals((double) 5 / 3, results[2]);
		Assert.assertEquals((double) 4, results[3]);
	}

	@org.junit.Test
	public void subtractAvgValuesTest() {
		double[] results = operations.getAvgCols(operations.matrix);
		operations.subtractAvgValues(operations.matrix, results);

		// Assert.assertEquals(1.0 - (double)10/3, operations.matrix.get(0, 0));
		// Assert.assertEquals(5.0 - 3.0, operations.matrix.get(0, 1));
		// Assert.assertEquals(4.0 - (double)5/3, operations.matrix.get(0, 2));
		// Assert.assertEquals(1.0 - 4.0, operations.matrix.get(0, 3));
		// Assert.assertEquals(3.0 - (double)10/3, operations.matrix.get(1, 0));
		// Assert.assertEquals(3.0 - 3.0, operations.matrix.get(1, 1));
		// Assert.assertEquals(0.0 - (double)5/3, operations.matrix.get(1, 2));
		// Assert.assertEquals(2.0 - 4.0, operations.matrix.get(1, 3));
		// Assert.assertEquals(6.0 - (double)10/3, operations.matrix.get(2, 0));
		// Assert.assertEquals(1.0 - 3.0, operations.matrix.get(2, 1));
		// Assert.assertEquals(1.0 - (double)5/3, operations.matrix.get(2, 2));
		// Assert.assertEquals(9.0 - 4.0, operations.matrix.get(2, 3));

		Assert.assertEquals(-2.3333333333333335, operations.matrix.get(0, 0));
		Assert.assertEquals(2.0, operations.matrix.get(0, 1));
		Assert.assertEquals(2.333333333333333, operations.matrix.get(0, 2));
		Assert.assertEquals(-3.0, operations.matrix.get(0, 3));
		Assert.assertEquals(-0.3333333333333335, operations.matrix.get(1, 0));
		Assert.assertEquals(0.0, operations.matrix.get(1, 1));
		Assert.assertEquals(-1.6666666666666667, operations.matrix.get(1, 2));
		Assert.assertEquals(-2.0, operations.matrix.get(1, 3));
		Assert.assertEquals(2.6666666666666665, operations.matrix.get(2, 0));
		Assert.assertEquals(-2.0, operations.matrix.get(2, 1));
		Assert.assertEquals(-0.6666666666666667, operations.matrix.get(2, 2));
		Assert.assertEquals(5.0, operations.matrix.get(2, 3));
	}

	@org.junit.Test
	public void sumOfSquaredMatrixTest() {
		Matrix results = operations.sumOfSquaredMatrix(operations.matrix);

		Assert.assertEquals(4, results.getColumnDimension());
		Assert.assertEquals(4, results.getRowDimension());

		double n = (double) 1 / 3;
		Matrix m = operations.matrix;

		Assert.assertEquals(
				(Math.pow(m.get(0, 0), 2) + Math.pow(m.get(1, 0), 2) + Math
						.pow(m.get(2, 0), 2)) * n, results.get(0, 0));
		Assert.assertEquals(
				(m.get(0, 0) * m.get(0, 1) + m.get(1, 0) * m.get(1, 1) + m.get(
						2, 0) * m.get(2, 1))
						* n, results.get(0, 1));
		Assert.assertEquals(
				(m.get(0, 0) * m.get(0, 2) + m.get(1, 0) * m.get(1, 2) + m.get(
						2, 0) * m.get(2, 2))
						* n, results.get(0, 2));
		Assert.assertEquals(
				(m.get(0, 0) * m.get(0, 3) + m.get(1, 0) * m.get(1, 3) + m.get(
						2, 0) * m.get(2, 3))
						* n, results.get(0, 3));
		Assert.assertEquals(
				(m.get(0, 1) * m.get(0, 0) + m.get(1, 1) * m.get(1, 0) + m.get(
						2, 1) * m.get(2, 0))
						* n, results.get(1, 0));
		Assert.assertEquals(
				(Math.pow(m.get(0, 1), 2) + Math.pow(m.get(1, 1), 2) + Math
						.pow(m.get(2, 1), 2)) * n, results.get(1, 1));
		Assert.assertEquals(
				(m.get(0, 1) * m.get(0, 2) + m.get(1, 1) * m.get(1, 2) + m.get(
						2, 1) * m.get(2, 2))
						* n, results.get(1, 2));
		Assert.assertEquals(
				(m.get(0, 1) * m.get(0, 3) + m.get(1, 1) * m.get(1, 3) + m.get(
						2, 1) * m.get(2, 3))
						* n, results.get(1, 3));
		Assert.assertEquals(
				(m.get(0, 2) * m.get(0, 0) + m.get(1, 2) * m.get(1, 0) + m.get(
						2, 2) * m.get(2, 0))
						* n, results.get(2, 0));
		Assert.assertEquals(
				(m.get(0, 2) * m.get(0, 1) + m.get(1, 2) * m.get(1, 1) + m.get(
						2, 2) * m.get(2, 1))
						* n, results.get(2, 1));
		Assert.assertEquals(
				(Math.pow(m.get(0, 2), 2) + Math.pow(m.get(1, 2), 2) + Math
						.pow(m.get(2, 2), 2)) * n, results.get(2, 2));
		Assert.assertEquals(
				(m.get(0, 2) * m.get(0, 3) + m.get(1, 2) * m.get(1, 3) + m.get(
						2, 2) * m.get(2, 3))
						* n, results.get(2, 3));
		Assert.assertEquals(
				(m.get(0, 3) * m.get(0, 0) + m.get(1, 3) * m.get(1, 0) + m.get(
						2, 3) * m.get(2, 0))
						* n, results.get(3, 0));
		Assert.assertEquals(
				(m.get(0, 3) * m.get(0, 1) + m.get(1, 3) * m.get(1, 1) + m.get(
						2, 3) * m.get(2, 1))
						* n, results.get(3, 1));
		Assert.assertEquals(
				(m.get(0, 3) * m.get(0, 2) + m.get(1, 3) * m.get(1, 2) + m.get(
						2, 3) * m.get(2, 2))
						* n, results.get(3, 2));
		Assert.assertEquals(
				(Math.pow(m.get(0, 3), 2) + Math.pow(m.get(1, 3), 2) + Math
						.pow(m.get(2, 3), 2)) * n, results.get(3, 3));
	}

	@org.junit.Test
	public void corelationMatrixTest() {
		Matrix results = operations.corelationMatrix(operations.matrix);

		AssertEqual(4, results.getColumnDimension());
		AssertEqual(4, results.getRowDimension());

		AssertEqual(1.0, results.get(0, 0));
		AssertEqual(-0.99, results.get(0, 1));
		AssertEqual(-0.64, results.get(0, 2));
		AssertEqual(0.96, results.get(0, 3));
		AssertEqual(-0.99, results.get(1, 0));
		AssertEqual(1.0, results.get(1, 1));
		AssertEqual(0.72, results.get(1, 2));
		AssertEqual(-0.92, results.get(1, 3));
		AssertEqual(-0.64, results.get(2, 0));
		AssertEqual(0.72, results.get(2, 1));
		AssertEqual(1.0, results.get(2, 2));
		AssertEqual(-0.39, results.get(2, 3));
		AssertEqual(0.96, results.get(3, 0));
		AssertEqual(-0.92, results.get(3, 1));
		AssertEqual(-0.39, results.get(3, 2));
		AssertEqual(1.0, results.get(3, 3));
	}
	
	@org.junit.Test
	public void corelationMatrixTest2() {

		operations = new MatrixOperations("testData2.csv");
		Matrix results = operations.corelationMatrix(operations.matrix);

		AssertEqual(8, results.getColumnDimension());
		AssertEqual(8, results.getRowDimension());

		// diagonal
		AssertEqual(1.0, results.get(0, 0));
		AssertEqual(1.0, results.get(1, 1));
		AssertEqual(1.0, results.get(2, 2));
		AssertEqual(1.0, results.get(3, 3));
		AssertEqual(1.0, results.get(4, 4));
		AssertEqual(1.0, results.get(5, 5));
		AssertEqual(1.0, results.get(6, 6));
		AssertEqual(1.0, results.get(7, 7));
		
		AssertEqual(-0.63, results.get(0, 1));
		AssertEqual(-0.75, results.get(0, 2));
		AssertEqual(-0.55, results.get(0, 3));
		AssertEqual(-0.49, results.get(0, 4));
		AssertEqual(-0.57, results.get(0, 5));
		AssertEqual(-0.43, results.get(0, 6));
		AssertEqual(-0.80, results.get(0, 7));
		
		AssertEqual(0.11, results.get(1, 2));
		AssertEqual(0.89, results.get(1, 3));
		AssertEqual(0.80, results.get(1, 4));
		AssertEqual(0.72, results.get(1, 5));
		AssertEqual(0.28, results.get(1, 6));
		AssertEqual(0.67, results.get(1, 7));
		
		AssertEqual(-0.11, results.get(2, 3));
		AssertEqual(-0.20, results.get(2, 4));
		AssertEqual(-0.06, results.get(2, 5));
		AssertEqual(0.14, results.get(2, 6));
		AssertEqual(0.26, results.get(2, 7));

		AssertEqual(0.94, results.get(3, 4));
		AssertEqual(0.85, results.get(3, 5));
		AssertEqual(0.37, results.get(3, 6));
		AssertEqual(0.78, results.get(3, 7));
		
		AssertEqual(0.94, results.get(4, 5));
		AssertEqual(0.37, results.get(4, 6));
		AssertEqual(0.83, results.get(4, 7));

		AssertEqual(0.22, results.get(5, 6));
		AssertEqual(0.81, results.get(5, 7));
		
		AssertEqual(0.60, results.get(6, 7));
	}

	public void AssertEqual(double expected, double actual) {
		Assert.assertEquals(expected, round(actual, 2));
	}

	public static double round(double valueToRound, int numberOfDecimalPlaces) {
		double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
		double interestedInZeroDPs = valueToRound * multipicationFactor;
		return Math.round(interestedInZeroDPs) / multipicationFactor;
	}
}
