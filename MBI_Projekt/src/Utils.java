import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import Jama.Matrix;

public class Utils {
	/**
	 * Wczytuje dane z pliku .csv Nazwa pliku jest parametrem konstruktora
	 * Wsp�rz�dne s� oddzielane spacjami Zwraca obiekt klasy Jama.Matrix
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Matrix getInputCSVData(String fileName) {
		/* Nie znamy ani liczby wierszy ani liczby kolumn */
		ArrayList<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();

		try {
			Reader reader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				String[] rowStr = line.split(",");
				ArrayList<Double> row = new ArrayList<Double>();

				for (int i = 0; i < rowStr.length; i++)
					row.add(Double.parseDouble(rowStr[i]));

				matrix.add(row);
			}

			bufferedReader.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		int n = matrix.size();
		int m = matrix.get(0).size();

		double[][] pomMatrix = new double[n][m];

		/*
		 * przepisujemy list� do tablicy, bo tylko taki parametr przyjmuje
		 * konstruktor klasy jama.Matrix TODO w przypadku problem�w z
		 * wydajno�ci� zmieni�
		 */
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				pomMatrix[i][j] = matrix.get(i).get(j);

		Matrix jamaMatrix = new Matrix(pomMatrix);
		return jamaMatrix;
	}

	/**
	 * @param matrix
	 * @param fileName
	 */
	public static void saveMatrix(Matrix matrix, String fileName) {
		try {
			File file = new File(fileName);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < matrix.getRowDimension(); i++) {
				for (int j = 0; j < matrix.getColumnDimension(); j++)
					bw.write((j != 0 ? "," : "") + matrix.get(i, j));
				bw.write("\n");
			}
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wy�wietlanie macierzy na konsoli
	 * 
	 * @param matrix
	 */
	public static void displayMatrix(Matrix matrix) {
		matrix.print(matrix.getColumnDimension(), matrix.getRowDimension());
	}

	/**
	 * Odwr�cenie kolejno�ci
	 * 
	 * @param arr
	 * @return
	 */
	public static double[] reverse(double[] arr) {
		List<Double> list = new ArrayList<Double>();
		for (double d : arr) {
			list.add((Double) d);
		}
		Collections.reverse(list);
		double[] array = new double[arr.length];
		int i = 0;
		for (double d : list) {
			array[i] = d;
			++i;
		}
		return array;
	}

	/**
	 * Assert z zaokr�gleniem liczb do 2 miejsc po przecinku
	 * 
	 * @param expected
	 * @param actual
	 */
	public static void assertEqual(double expected, double actual) {
		Assert.assertEquals(expected, round(actual, 2));
	}

	/**
	 * Zaokr�glanie liczb
	 * 
	 * @param valueToRound
	 * @param numberOfDecimalPlaces
	 * @return
	 */
	public static double round(double valueToRound, int numberOfDecimalPlaces) {
		double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
		double interestedInZeroDPs = valueToRound * multipicationFactor;
		return Math.round(interestedInZeroDPs) / multipicationFactor;
	}

	/**
	 * Logowanie informacji
	 * 
	 * @param message
	 */
	public static void log(String message) {
		printTime();
		System.out.println(":\t " + message);
	}

	/**
	 * Logowanie czasu
	 */
	private static void printTime() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.print(sdf.format(cal.getTime()) + ":\t");
	}
}