package unit.tests;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;
import pca.EigenValue;
import pca.Utils;
import Jama.Matrix;

public class UtilsTest {

	@org.junit.Test
	public void saveMatrixTest_createNewFile() {
		File file = new File("resources/output/notExistingFile.csv");
		if (file.exists())
			file.delete();

		Utils.saveMatrix(new Matrix(3, 2),
				"resources/output/notExistingFile.csv");
		file = new File("resources/output/notExistingFile.csv");
		Assert.assertTrue(file.exists());
	}

	@org.junit.Test
	public void saveMatrixTest_fileIsDirectory() {
		boolean result = Utils
				.saveMatrix(new Matrix(3, 2), "resources/output/");
		Assert.assertFalse(result);

	}

	@org.junit.Test
	public void reverseTest() {
		double[] array = new double[] { 5, 3, 2, 1, 6 };

		array = Utils.reverse(array);

		Utils.assertEqual(6, array[0]);
		Utils.assertEqual(1, array[1]);
		Utils.assertEqual(2, array[2]);
		Utils.assertEqual(3, array[3]);
		Utils.assertEqual(5, array[4]);
	}

	@org.junit.Test
	public void eigenvaluesTest() {

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

		m.print(3, 2);

		// wartoœci w³asne (maj¹ byæ 3)
		double[] vals = m.eig().getRealEigenvalues();
		Assert.assertEquals(3, vals.length);

		// Arrays.sort(vals);
		// vals = Utils.reverse(vals);

		ArrayList<EigenValue> eig = new ArrayList<EigenValue>();

		for (int i = 0; i < vals.length; ++i)
			eig.add(new EigenValue(vals[i]));

		// sprawdzenie
		Utils.assertEqual(1.0, vals[0]);
		Utils.assertEqual(-1.0, vals[1]);
		Utils.assertEqual(2.0, vals[2]);

		// czy faktycznie wyznacznik dla nich jest = 0
		Utils.assertEqual(0, m.minus(Matrix.identity(3, 3).times(vals[0]))
				.det());
		Utils.assertEqual(0, m.minus(Matrix.identity(3, 3).times(vals[1]))
				.det());
		Utils.assertEqual(0, m.minus(Matrix.identity(3, 3).times(vals[2]))
				.det());

		// wektory w³asne (w kolumnach)
		Matrix vectors = m.eig().getV();
		Assert.assertEquals(3, vectors.getColumnDimension());
		Assert.assertEquals(3, vectors.getRowDimension());

		vectors.print(3, 2);

		Utils.assertEqual(0.71, vectors.get(0, 0));
		Utils.assertEqual(0, vectors.get(0, 1));
		Utils.assertEqual(-2.0, vectors.get(0, 2));

		Utils.assertEqual(0, vectors.get(1, 0));
		Utils.assertEqual(0, vectors.get(1, 1));
		Utils.assertEqual(-1.0, vectors.get(1, 2));

		Utils.assertEqual(-0.71, vectors.get(2, 0));
		Utils.assertEqual(-1.41, vectors.get(2, 1));
		Utils.assertEqual(2.0, vectors.get(2, 2));

	}

	@org.junit.Test
	public void getMethodNameTest() {
		Assert.assertEquals("getMethodNameTest", Utils.getMethodName(2));
	}

	@org.junit.Test
	public void utilsLogTest() {
		Utils.EnableDebugMode();
		Assert.assertFalse(Utils.getMap().containsKey("utilsLogTest"));
		Utils.logStart();
		Utils.logStop();
		Assert.assertTrue(Utils.getMap().containsKey("utilsLogTest"));
		Utils.DisableDebugMode();
	}
}
