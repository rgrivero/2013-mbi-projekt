import Jama.Matrix;

public class Tests 
{
	public static void testLoadingMatrixFromFile(String fileName)
	{	
		Matrix loadedMatrix = Utils.getInputCSVData(fileName);
		Utils.displayMatrix(loadedMatrix);
	}
}
