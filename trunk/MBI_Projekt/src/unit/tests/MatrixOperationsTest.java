package unit.tests;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;
import pca.EigenValue;
import pca.MatrixOperations;
import pca.Utils;
import Jama.Matrix;

public class MatrixOperationsTest {

	MatrixOperations operations;

	@org.junit.Before
	public void testInit() {
		operations = new MatrixOperations("resources/input/testData.csv");
	}

	@org.junit.AfterClass
	public static void cleanUpClass() {
		File folder = new File("resources/output/");
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files)
				f.delete();
		}
	}

	@org.junit.Test
	public void constructorTest() {
		operations = new MatrixOperations("resources/input/testData.csv");
		Assert.assertEquals(1.0, operations.getMatrix().get(0, 0));
		Assert.assertEquals(5.0, operations.getMatrix().get(0, 1));
		Assert.assertEquals(4.0, operations.getMatrix().get(0, 2));
		Assert.assertEquals(1.0, operations.getMatrix().get(0, 3));
		Assert.assertEquals(3.0, operations.getMatrix().get(1, 0));
		Assert.assertEquals(3.0, operations.getMatrix().get(1, 1));
		Assert.assertEquals(0.0, operations.getMatrix().get(1, 2));
		Assert.assertEquals(2.0, operations.getMatrix().get(1, 3));
		Assert.assertEquals(6.0, operations.getMatrix().get(2, 0));
		Assert.assertEquals(1.0, operations.getMatrix().get(2, 1));
		Assert.assertEquals(1.0, operations.getMatrix().get(2, 2));
		Assert.assertEquals(9.0, operations.getMatrix().get(2, 3));
	}

	@org.junit.Test
	public void constructorTest_fileNotExists() {
		// exception stack trace should be printed
		operations = new MatrixOperations(
				"resources/input/notExistingFile.csv", ";");
		Assert.assertNull(operations.getMatrix());
	}

	@org.junit.Test
	public void getAvgColsTest() {
		double[] results = operations.getAvgCols(operations.getMatrix());

		Assert.assertEquals(4, results.length);
		Assert.assertEquals((double) 10 / 3, results[0]);
		Assert.assertEquals((double) 3, results[1]);
		Assert.assertEquals((double) 5 / 3, results[2]);
		Assert.assertEquals((double) 4, results[3]);
	}

	@org.junit.Test
	public void subtractAvgValuesTest() {
		double[] results = operations.getAvgCols(operations.getMatrix());
		operations.subtractAvgValues(operations.getMatrix(), results);

		// Assert.assertEquals(1.0 - (double)10/3, operations.getMatrix().get(0,
		// 0));
		// Assert.assertEquals(5.0 - 3.0, operations.getMatrix().get(0, 1));
		// Assert.assertEquals(4.0 - (double)5/3, operations.getMatrix().get(0,
		// 2));
		// Assert.assertEquals(1.0 - 4.0, operations.getMatrix().get(0, 3));
		// Assert.assertEquals(3.0 - (double)10/3, operations.getMatrix().get(1,
		// 0));
		// Assert.assertEquals(3.0 - 3.0, operations.getMatrix().get(1, 1));
		// Assert.assertEquals(0.0 - (double)5/3, operations.getMatrix().get(1,
		// 2));
		// Assert.assertEquals(2.0 - 4.0, operations.getMatrix().get(1, 3));
		// Assert.assertEquals(6.0 - (double)10/3, operations.getMatrix().get(2,
		// 0));
		// Assert.assertEquals(1.0 - 3.0, operations.getMatrix().get(2, 1));
		// Assert.assertEquals(1.0 - (double)5/3, operations.getMatrix().get(2,
		// 2));
		// Assert.assertEquals(9.0 - 4.0, operations.getMatrix().get(2, 3));

		Assert.assertEquals(-2.3333333333333335,
				operations.getMatrix().get(0, 0));
		Assert.assertEquals(2.0, operations.getMatrix().get(0, 1));
		Assert.assertEquals(2.333333333333333, operations.getMatrix().get(0, 2));
		Assert.assertEquals(-3.0, operations.getMatrix().get(0, 3));
		Assert.assertEquals(-0.3333333333333335,
				operations.getMatrix().get(1, 0));
		Assert.assertEquals(0.0, operations.getMatrix().get(1, 1));
		Assert.assertEquals(-1.6666666666666667,
				operations.getMatrix().get(1, 2));
		Assert.assertEquals(-2.0, operations.getMatrix().get(1, 3));
		Assert.assertEquals(2.6666666666666665, operations.getMatrix()
				.get(2, 0));
		Assert.assertEquals(-2.0, operations.getMatrix().get(2, 1));
		Assert.assertEquals(-0.6666666666666667,
				operations.getMatrix().get(2, 2));
		Assert.assertEquals(5.0, operations.getMatrix().get(2, 3));
	}

	@org.junit.Test
	public void sumOfSquaredMatrixTest() {
		Matrix results = operations.sumOfSquaredMatrix(operations.getMatrix());

		Assert.assertEquals(4, results.getColumnDimension());
		Assert.assertEquals(4, results.getRowDimension());

		double n = (double) 1 / 3;
		Matrix m = operations.getMatrix();

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
		Matrix results = operations.corelationMatrix(operations.getMatrix());

		Utils.assertEqual(4, results.getColumnDimension());
		Utils.assertEqual(4, results.getRowDimension());

		Utils.assertEqual(1.0, results.get(0, 0));
		Utils.assertEqual(-0.99, results.get(0, 1));
		Utils.assertEqual(-0.64, results.get(0, 2));
		Utils.assertEqual(0.96, results.get(0, 3));
		Utils.assertEqual(-0.99, results.get(1, 0));
		Utils.assertEqual(1.0, results.get(1, 1));
		Utils.assertEqual(0.72, results.get(1, 2));
		Utils.assertEqual(-0.92, results.get(1, 3));
		Utils.assertEqual(-0.64, results.get(2, 0));
		Utils.assertEqual(0.72, results.get(2, 1));
		Utils.assertEqual(1.0, results.get(2, 2));
		Utils.assertEqual(-0.39, results.get(2, 3));
		Utils.assertEqual(0.96, results.get(3, 0));
		Utils.assertEqual(-0.92, results.get(3, 1));
		Utils.assertEqual(-0.39, results.get(3, 2));
		Utils.assertEqual(1.0, results.get(3, 3));
	}

	@org.junit.Test
	public void covarianceMatrixTest() {
		Matrix results = operations.covarianceMatrix(operations.getMatrix());

		Utils.assertEqual(4, results.getColumnDimension());
		Utils.assertEqual(4, results.getRowDimension());

		Utils.assertEqual(3.17, results.get(0, 0));
		Utils.assertEqual(-2.5, results.get(0, 1));
		Utils.assertEqual(-1.67, results.get(0, 2));
		Utils.assertEqual(5.25, results.get(0, 3));

		Utils.assertEqual(-2.50, results.get(1, 0));
		Utils.assertEqual(2.0, results.get(1, 1));
		Utils.assertEqual(1.5, results.get(1, 2));
		Utils.assertEqual(-4.00, results.get(1, 3));

		Utils.assertEqual(-1.67, results.get(2, 0));
		Utils.assertEqual(1.50, results.get(2, 1));
		Utils.assertEqual(2.17, results.get(2, 2));
		Utils.assertEqual(-1.75, results.get(2, 3));

		Utils.assertEqual(5.25, results.get(3, 0));
		Utils.assertEqual(-4.0, results.get(3, 1));
		Utils.assertEqual(-1.75, results.get(3, 2));
		Utils.assertEqual(9.5, results.get(3, 3));
	}

	@org.junit.Test
	public void corelationMatrixTest2() {

		operations = new MatrixOperations("resources/input/testData2.csv");
		Matrix results = operations.corelationMatrix(operations.getMatrix());

		Utils.assertEqual(8, results.getColumnDimension());
		Utils.assertEqual(8, results.getRowDimension());

		// diagonal
		Utils.assertEqual(1.0, results.get(0, 0));
		Utils.assertEqual(1.0, results.get(1, 1));
		Utils.assertEqual(1.0, results.get(2, 2));
		Utils.assertEqual(1.0, results.get(3, 3));
		Utils.assertEqual(1.0, results.get(4, 4));
		Utils.assertEqual(1.0, results.get(5, 5));
		Utils.assertEqual(1.0, results.get(6, 6));
		Utils.assertEqual(1.0, results.get(7, 7));

		Utils.assertEqual(-0.63, results.get(0, 1));
		Utils.assertEqual(-0.75, results.get(0, 2));
		Utils.assertEqual(-0.55, results.get(0, 3));
		Utils.assertEqual(-0.49, results.get(0, 4));
		Utils.assertEqual(-0.57, results.get(0, 5));
		Utils.assertEqual(-0.43, results.get(0, 6));
		Utils.assertEqual(-0.80, results.get(0, 7));

		Utils.assertEqual(0.11, results.get(1, 2));
		Utils.assertEqual(0.89, results.get(1, 3));
		Utils.assertEqual(0.80, results.get(1, 4));
		Utils.assertEqual(0.72, results.get(1, 5));
		Utils.assertEqual(0.28, results.get(1, 6));
		Utils.assertEqual(0.67, results.get(1, 7));

		Utils.assertEqual(-0.11, results.get(2, 3));
		Utils.assertEqual(-0.20, results.get(2, 4));
		Utils.assertEqual(-0.06, results.get(2, 5));
		Utils.assertEqual(0.14, results.get(2, 6));
		Utils.assertEqual(0.26, results.get(2, 7));

		Utils.assertEqual(0.94, results.get(3, 4));
		Utils.assertEqual(0.85, results.get(3, 5));
		Utils.assertEqual(0.37, results.get(3, 6));
		Utils.assertEqual(0.78, results.get(3, 7));

		Utils.assertEqual(0.94, results.get(4, 5));
		Utils.assertEqual(0.37, results.get(4, 6));
		Utils.assertEqual(0.83, results.get(4, 7));

		Utils.assertEqual(0.22, results.get(5, 6));
		Utils.assertEqual(0.81, results.get(5, 7));

		Utils.assertEqual(0.60, results.get(6, 7));
	}

	@org.junit.Test
	public void eigenValueSortTest() {
		// przyk³adowa macierz
		Matrix m = new Matrix(3, 3);
		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.set(0, 2, 0);
		m.set(1, 0, 0);
		m.set(1, 1, 2);
		m.set(1, 2, 0);
		m.set(2, 0, -2);
		m.set(2, 1, -2);
		m.set(2, 2, -1);

		ArrayList<EigenValue> eig = operations.computeEigenValues(m);

		Utils.assertEqual(-1.0, eig.get(2).getValue());
		Utils.assertEqual(1.0, eig.get(1).getValue());
		Utils.assertEqual(2.0, eig.get(0).getValue());

		Utils.assertEqual(0.0, eig.get(2).getVector()[0]);
		Utils.assertEqual(0.0, eig.get(2).getVector()[1]);
		Utils.assertEqual(-1.41, eig.get(2).getVector()[2]);

		Utils.assertEqual(0.71, eig.get(1).getVector()[0]);
		Utils.assertEqual(0.0, eig.get(1).getVector()[1]);
		Utils.assertEqual(-0.71, eig.get(1).getVector()[2]);

		Utils.assertEqual(-2.0, eig.get(0).getVector()[0]);
		Utils.assertEqual(-1.0, eig.get(0).getVector()[1]);
		Utils.assertEqual(2.0, eig.get(0).getVector()[2]);
	}

	@org.junit.Test
	public void eigenValueSortTest2() {
		// przyk³adowa macierz
		operations = new MatrixOperations("resources/input/testData2.csv");
		Matrix m = operations.corelationMatrix(operations.getMatrix());

		ArrayList<EigenValue> eig = operations.computeEigenValues(m);

		Utils.assertEqual(8, eig.size());

		Utils.assertEqual(5.01, eig.get(0).getValue());
		Utils.assertEqual(1.68, eig.get(1).getValue());
		Utils.assertEqual(0.85, eig.get(2).getValue());
		Utils.assertEqual(0.36, eig.get(3).getValue());
		Utils.assertEqual(0.06, eig.get(4).getValue());
		Utils.assertEqual(0.04, eig.get(5).getValue());
		Utils.assertEqual(0.01, eig.get(6).getValue());
		Utils.assertEqual(0.00, eig.get(7).getValue());
	}

	@org.junit.Test
	public void computeEigenValuesWithThresholdTest() {

		operations = new MatrixOperations("resources/input/testData2.csv");
		Matrix m = operations.corelationMatrix(operations.getMatrix());

		ArrayList<EigenValue> eig = operations.computeEigenValues(m);

		ArrayList<EigenValue> eigRes = operations.chooseNewAttributes(eig,
				80.0, true);
		Assert.assertEquals(2, eigRes.size());

		eigRes = operations.chooseNewAttributes(eig, 10.0, true);
		Assert.assertEquals(1, eigRes.size());

		eigRes = operations.chooseNewAttributes(eig, 100.0, true);
		Assert.assertEquals(7, eigRes.size());
	}

	@org.junit.Test
	public void computeEigenVectorsTest() {

		operations = new MatrixOperations("resources/input/testData2.csv");
		Matrix m = operations.corelationMatrix(operations.getMatrix());

		ArrayList<EigenValue> eig = operations.computeEigenValues(m);

		ArrayList<EigenValue> eigRes = operations.chooseNewAttributes(eig,
				80.0, true);
		Assert.assertEquals(2, eigRes.size());
		Utils.assertEqual(-0.34, eigRes.get(0).getVector()[0]);
		Utils.assertEqual(0.39, eigRes.get(0).getVector()[1]);
		Utils.assertEqual(0.07, eigRes.get(0).getVector()[2]);
		Utils.assertEqual(0.41, eigRes.get(0).getVector()[3]);
		Utils.assertEqual(0.41, eigRes.get(0).getVector()[4]);
		Utils.assertEqual(0.40, eigRes.get(0).getVector()[5]);
		Utils.assertEqual(0.23, eigRes.get(0).getVector()[6]);
		Utils.assertEqual(0.42, eigRes.get(0).getVector()[7]);
		
		Utils.assertEqual(-0.48, eigRes.get(1).getVector()[0]);
		Utils.assertEqual(-0.07, eigRes.get(1).getVector()[1]);
		Utils.assertEqual(0.74, eigRes.get(1).getVector()[2]);
		Utils.assertEqual(-0.22, eigRes.get(1).getVector()[3]);
		Utils.assertEqual(-0.28, eigRes.get(1).getVector()[4]);
		Utils.assertEqual(-0.20, eigRes.get(1).getVector()[5]);
		Utils.assertEqual(0.20, eigRes.get(1).getVector()[6]);
		Utils.assertEqual(0.12, eigRes.get(1).getVector()[7]);
		
	}

	// sprawdzenie poprawnoœci wektorów w³asnych
	@org.junit.Test
	public void checkAreVectorsReal() {
		operations = new MatrixOperations("resources/input/testData2.csv");
		Matrix m = operations.corelationMatrix(operations.getMatrix());
		ArrayList<EigenValue> eig = operations.computeEigenValues(m);
		ArrayList<EigenValue> eigRes = operations.chooseNewAttributes(eig,
				80.0, true);
		
		double [][] vectors = new double[1][8];
		vectors[0]= eig.get(0).getVector();
		Matrix identity = Matrix.identity(8, 8);
		Matrix mulByValue = identity.times(eigRes.get(0).getValue());
		Matrix partialRes = m.minus(mulByValue);
		Matrix res = partialRes.times(new Matrix(vectors).transpose());
		
		Assert.assertEquals(1, res.getColumnDimension());
		Assert.assertEquals(8, res.getRowDimension());
		
		Utils.assertEqual(0.0, res.get(0, 0));
		Utils.assertEqual(0.0, res.get(1, 0));
		Utils.assertEqual(0.0, res.get(2, 0));
		Utils.assertEqual(0.0, res.get(3, 0));
		Utils.assertEqual(0.0, res.get(4, 0));
		Utils.assertEqual(0.0, res.get(5, 0));
		Utils.assertEqual(0.0, res.get(6, 0));
		Utils.assertEqual(0.0, res.get(7, 0));
		
		// sprawdzenie czy wektor o wartoœciach przeciwnych jest prawid³owy
		res = partialRes.times(new Matrix(vectors).times(-1).transpose());
		
		Assert.assertEquals(1, res.getColumnDimension());
		Assert.assertEquals(8, res.getRowDimension());
		
		Utils.assertEqual(0.0, res.get(0, 0));
		Utils.assertEqual(0.0, res.get(1, 0));
		Utils.assertEqual(0.0, res.get(2, 0));
		Utils.assertEqual(0.0, res.get(3, 0));
		Utils.assertEqual(0.0, res.get(4, 0));
		Utils.assertEqual(0.0, res.get(5, 0));
		Utils.assertEqual(0.0, res.get(6, 0));
		Utils.assertEqual(0.0, res.get(7, 0));
	}
	
	@org.junit.Test
	public void eigenValueSumTest() {
		// przyk³adowa macierz
		Matrix m = new Matrix(3, 3);
		m.set(0, 0, 1);
		m.set(0, 1, 2);
		m.set(0, 2, 0);
		m.set(1, 0, 0);
		m.set(1, 1, 2);
		m.set(1, 2, 0);
		m.set(2, 0, -2);
		m.set(2, 1, -2);
		m.set(2, 2, -1);

		ArrayList<EigenValue> eig = operations.computeEigenValues(m);

		double sum = 0;

		for (int i = 0; i < eig.size(); ++i)
			sum += eig.get(i).getValue();

		Utils.assertEqual(2.0, sum);
	}

	@org.junit.Test
	public void runAllTest1() {
		// przyk³adowa macierz
		operations = new MatrixOperations("resources/input/testData2.csv");
		operations.run("resources/output/out01.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out01.csv");
		Assert.assertEquals(2, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(11, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runAllTest2() {
		operations = new MatrixOperations("resources/input/23attr2000rows.csv");
		operations.run("resources/output/out02.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out02.csv");
		Assert.assertEquals(12, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(2126, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runAllTest3() {
		operations = new MatrixOperations("resources/input/21attr540rows.csv");
		operations.run("resources/output/out03.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out03.csv");
		Assert.assertEquals(16, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(540, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runAllTest4() {
		operations = new MatrixOperations("resources/input/57attr4601rows.csv");
		operations.run("resources/output/out04.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out04.csv");
		Assert.assertEquals(35, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(4601, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runAllTest5() {
		operations = new MatrixOperations("resources/input/21attr5000rows.csv");
		operations.run("resources/output/out05.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out05.csv");
		Assert.assertEquals(10, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(5000, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runAllTest6() {
		operations = new MatrixOperations("resources/input/500attr2001rows.csv");
		operations.run("resources/output/out06.csv", true, 80.0, true);

		operations = new MatrixOperations("resources/output/out06.csv");
		Assert.assertEquals(295, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(2000, operations.getMatrix().getRowDimension());
	}

	// @org.junit.Test
	// public void runAllTest7() {
	// operations = new MatrixOperations(
	// "resources/input/1300attr572rows.csv", ";");
	// operations.run("resources/output/out07.csv", true, 80.0, true);
	//
	// operations = new MatrixOperations("resources/output/out07.csv");
	// Assert.assertEquals(572, operations.getMatrix().getRowDimension());
	// Assert.assertEquals(295, operations.getMatrix().getColumnDimension());
	// }

	@org.junit.Test
	public void runWithAttributes_Test1() {
		// przyk³adowa macierz
		operations = new MatrixOperations("resources/input/testData2.csv");
		operations.run("resources/output/out01.csv", true, 3, false);

		operations = new MatrixOperations("resources/output/out01.csv");
		Assert.assertEquals(3, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(11, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runWithAttributes_Test2() {
		operations = new MatrixOperations("resources/input/23attr2000rows.csv");
		operations.run("resources/output/out02.csv", true, 10, false);

		operations = new MatrixOperations("resources/output/out02.csv");
		Assert.assertEquals(10, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(2126, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runWithAttributes_Test3() {
		operations = new MatrixOperations("resources/input/21attr540rows.csv");
		operations.run("resources/output/out03.csv", true, 20, false);

		operations = new MatrixOperations("resources/output/out03.csv");
		Assert.assertEquals(20, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(540, operations.getMatrix().getRowDimension());
	}

	@org.junit.Test
	public void runWithAttributes_Test4() {
		operations = new MatrixOperations("resources/input/57attr4601rows.csv");
		operations.run("resources/output/out04.csv", true, 15, false);

		operations = new MatrixOperations("resources/output/out04.csv");
		Assert.assertEquals(15, operations.getMatrix().getColumnDimension());
		Assert.assertEquals(4601, operations.getMatrix().getRowDimension());
	}
}
