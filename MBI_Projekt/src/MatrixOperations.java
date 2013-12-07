
public class MatrixOperations 
{
	public static double[] getAvgRows(double[][] matrix, int n, int m)
	{
		//Vector<Double> vector = new Vector<Double>();
		double vector[] = new double[n];
		for(int i=0;i<n;i++)
		{	
			double sum = 0;
			
			for(int j=0; j<m;j++)
				sum+=matrix[i][j];
			
			vector[i] = sum/m;
		}
		return vector;
	}
	
	public static double[][] subtractAvgValues(double [][] matrix, double[] vector, int n, int m )
	{
		for(int i=0;i<n;i++)
			for(int j=0; j<m;j++)
				matrix[i][j]=matrix[i][j] - vector[n];
		
		return matrix;
	}
}
