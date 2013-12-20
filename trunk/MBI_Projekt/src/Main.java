public class Main {

	// uruchamianie z konsoli (cmd):
	// java -jar PCA.jar -i testData2.csv -o outData.csv -cor -t 81
	public static void main(String[] arg0) {
		boolean corelation = false;
		int input = -2, output = -2, threshold = -2;
		String inName = null, outName = null;
		double thresh = 80;

		String[] options = arg0;
		for (int i = 0; i < options.length; ++i) {
			// System.out.println(i + ": '" + options[i] + "'");

			if (options[i].equals("-i"))
				input = i;
			else if (options[i].equals("-o"))
				output = i;
			else if (options[i].equals("-t"))
				threshold = i;
			else if (options[i].equals("-help"))
				printHelp(false);
			else if (options[i].equals("-cor"))
				corelation = true;
			else if (input == (i - 1))
				inName = options[i];
			else if (output == (i - 1))
				outName = options[i];
			else if (threshold == (i - 1))
				thresh = Double.parseDouble(options[i]);
			else
				printHelp(true);
		}

		System.out.println("Parameters: ");
		System.out.println("Input file  : " + inName);
		System.out.println("Output file : " + outName);
		System.out.println("Threshold   : " + thresh + "%");
		System.out.println("Cov/Cor     : " + (corelation ? "Cor" : "Cov"));

		// Tests.testLoadingMatrixFromFile("dane.csv");
		// Tests.testAll();

		new MatrixOperations(inName).run(outName, corelation, thresh);
	}

	private static void printHelp(boolean withWaring) {
		if (withWaring)
			System.out.println("Warning: Unrecognized option");
		System.out.println("Application options:");
		System.out.println("-help     - help");
		System.out.println("-i %file% - input file name");
		System.out.println("-o %file% - output file name:");
		System.out
				.println("-cor      - using corelation matrix instead covariance:");
	}
}
