package main;

import pca.MatrixOperations;
import pca.Utils;

public class Main {

	static boolean corelation = false, debug = false;
	static int input = -2, output = -2, threshold = -2, attrPos = -2,
			attrs = 0;
	static String inName = null, outName = null;
	static double thresh = 80;

	// uruchamianie z konsoli (cmd):
	// java -jar PCA.jar -i testData2.csv -o outData.csv -cor -t 81
	/**
	 * @param arg0
	 */
	public static void main(String[] arg0) {
		for (int i = 0; i < arg0.length; ++i)
			checkAttributes(arg0, i);

		boolean useThresh = (attrPos == -2);
		printAttributes(useThresh);

		if (canStart())
			new MatrixOperations(inName).run(outName, corelation,
					useThresh ? thresh : attrs, useThresh);
		else
			System.out.print("\nERROR in parameteres - program closed");
	}

	/**
	 * @param useThresh
	 */
	private static void printAttributes(boolean useThresh) {
		System.out.println("Parameters: ");
		System.out.println("Input file             : " + inName);
		System.out.println("Output file            : " + outName);
		System.out.println("Using threshold        : " + useThresh);
		System.out.println("Threshold              : " + thresh + "%");
		System.out.println("Using attributes count : " + !useThresh);
		System.out.println("Attributes             : " + attrs);
		System.out.println("Covariance/Corelation  : "
				+ (corelation ? "Cor" : "Cov"));
		System.out.println("Debug mode             : " + debug + "\n");

		if (debug)
			Utils.EnableDebugMode();
		else
			Utils.DisableDebugMode();
	}

	/**
	 * @param options
	 * @param i
	 */
	private static void checkAttributes(String[] options, int i) {
		if (options[i].equals("-i"))
			input = i;
		else if (options[i].equals("-o"))
			output = i;
		else if (options[i].equals("-t"))
			threshold = i;
		else if (options[i].equals("-a"))
			attrPos = i;
		else if (options[i].equals("-help"))
			printHelp(false, "");
		else if (options[i].equals("-cor"))
			corelation = true;
		else if (options[i].equals("-d"))
			debug = true;
		else if (input == (i - 1))
			inName = options[i];
		else if (output == (i - 1))
			outName = options[i];
		else if (threshold == (i - 1))
			thresh = Double.parseDouble(options[i]);
		else if (attrPos == (i - 1))
			attrs = Integer.parseInt(options[i]);
		else
			printHelp(true, options[i]);
	}

	/**
	 * @return
	 */
	private static boolean canStart() {
		return inName != null
				&& outName != null
				&& ((thresh >= 0 && thresh <= 100 && attrPos == -2) || (threshold == -2 && attrs != 0));
	}

	/**
	 * @param withWaring
	 */
	private static void printHelp(boolean withWaring, String option) {
		if (withWaring)
			System.out.println("Warning: Unrecognized option: " + option);
		System.out.println("Application options:");
		System.out.println("-help     				- help");
		System.out.println("-i %file% 				- input file name");
		System.out.println("-o %file% 				- output file name:");
		System.out.println("-a %attributes_count% 	- new attributes lenght:");
		System.out
				.println("-t %threshold% 			- percentage coverage of exising dataset by new attributes:");
		System.out
				.println("-cor      				- using corelation matrix instead covariance:");
		System.out.println("-d 						- debug mode:");
		System.out.println();
	}
}
