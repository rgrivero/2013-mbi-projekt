import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import Jama.Matrix;

public class Utils 
{
	/* Wczytuje dane z pliku .csv
	 * Nazwa pliku jest parametrem konstruktora
	 * Wsp�rz�dne s� oddzielane spacjami
	 * Zwraca obiekt klasy Jama.Matrix
	 */
	public static Matrix getInputCSVData(String fileName)
	{ 
		/*Nie znamy ani liczby wierszy ani liczby kolumn*/
		ArrayList< ArrayList<Double> > matrix = new ArrayList< ArrayList<Double> > ();
		
		try
		{
			BufferedReader bufferedReader = new BufferedReader (new FileReader(fileName));
			String line = "";		
			
			while((line=bufferedReader.readLine()) != null)
			{
				String[] rowStr = line.split(" ");				
				ArrayList<Double> row = new ArrayList<Double> ();
				
				for(int i=0; i<rowStr.length;i++)
					row.add(Double.parseDouble(rowStr[i]));
								
				matrix.add(row);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		int n = matrix.size();
		int m = matrix.get(0).size();
		
		double[][] pomMatrix = new double[n][m];
		
		/*przepisujemy list� do tablicy, bo tylko taki parametr
		 * przyjmuje konstruktor klasy jama.Matrix
		  TODO w przypadku problem�w z wydajno�ci� zmieni�
		 */
		for(int i=0; i < n; i++)			
			for(int j=0; j<m;j++)
				pomMatrix[i][j] = matrix.get(i).get(j);
		
		Matrix jamaMatrix = new Matrix(pomMatrix);
		return jamaMatrix;
	}
	
	public static void displayMatrix(Matrix matrix)
	{
		matrix.print(matrix.getColumnDimension(), 0);
	}
}
