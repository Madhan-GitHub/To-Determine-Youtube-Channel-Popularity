package channelpopularity.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Madhan Thangavel
 *
 */

public class Results implements FileDisplayInterface, StdoutDisplayInterface {
	/**
	 * To display the Result for sysout operations.
	 *
	 * @param Result as String
	 */
	@Override
	public void display(String Result) {
		System.out.println(Result);
	}

	/**
	 * To display the Result in file
	 *
	 * @param Result as Object
	 */
	@Override
	public void display(List<String> ouputList) {
		for (String result : ouputList) {
			System.out.println(result);
		}
	}

	/**
	 * Retrieves and returns the next line in the input file.
	 *
	 * @param outputList
	 * @param outputPath
	 * @exception IOException On error encountered when writing in output file.
	 */
	@Override
	public void writeFileTo(List<String> ouputList, String outputPath) {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try {
			fileWriter = new FileWriter(outputPath);
			printWriter = new PrintWriter(fileWriter);
			for (String result : ouputList) {
				printWriter.print(result);
				printWriter.print("\n");
			}
		} catch (IOException e) {
			display(e.getMessage());
		} finally {
			if (null != printWriter) {
				printWriter.close();
			}
		}
	}
}
