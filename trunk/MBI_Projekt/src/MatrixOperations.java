import Jama.Matrix;


public class MatrixOperations 
{
	public double[] getAvgRows(Matrix matrix)
	{
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();
		
		double vector[] = new double[rows];
		
		for(int i=0;i<rows;i++)
		{	
			double sum = 0;
			
			for(int j=0; j<cols;j++)
				sum+=matrix.get(i,j);
			
			vector[i] = sum/cols;
		}
		return vector;
	}
	
	public double[] getAvgCols(Matrix matrix)
	{
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();
		
		double vector[] = new double[cols];
		
		for(int i=0;i<cols;i++)
		{	
			double sum = 0;
			
			for(int j=0; j<rows;j++)
				sum+=matrix.get(i,j);
			
			vector[i] = sum/rows;
		}
		return vector;
	}
	
	public Matrix subtractAvgValues(Matrix matrix, double[] vector)
	{
		int rows = matrix.getRowDimension();
		int cols = matrix.getColumnDimension();
		
		for(int i=0;i<rows;i++)
			for(int j=0; j<cols;j++)
				matrix.set(i,j,matrix.get(i,j) - vector[i]);
		
		return matrix;
	}
	
	
	public  Matrix covarianceMatrix(Matrix inputMatrix)
	{
		//int cols = inputMatrix.getColumnDimension();
		
		double[] means = getAvgCols(inputMatrix);
		subtractAvgValues(inputMatrix, means);
		
		Matrix transposedMatrix = inputMatrix.transpose();
		
		Matrix resultMatrix = inputMatrix.times(transposedMatrix);
		
		int resMatrixCols = resultMatrix.getColumnDimension();
		int resMatrixRows = resultMatrix.getRowDimension();
		
		for(int i=0; i<resMatrixCols; i++)
			for(int j=0; j<resMatrixCols; j++)
			{
				double value = resultMatrix.get(i,j);
				resultMatrix.set(i, j, value/resMatrixRows);
			}
				
		return resultMatrix;
	}
}
