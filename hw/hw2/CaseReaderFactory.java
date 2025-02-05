/**
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import HW3.CSVCaseReader;
import HW3.CaseReader;
import HW3.TSVCaseReader;

public class CaseReaderFactory {
	public static CaseReader createCaseReader(String filename) {
		if (filename.toLowerCase().endsWith(".tsv")) {
			return new TSVCaseReader(filename);
		} else if (filename.toLowerCase().endsWith(".csv")) {
			return new CSVCaseReader(filename);
		} else {
			throw new IllegalArgumentException("Unsupported file format");
		}
	}
}
