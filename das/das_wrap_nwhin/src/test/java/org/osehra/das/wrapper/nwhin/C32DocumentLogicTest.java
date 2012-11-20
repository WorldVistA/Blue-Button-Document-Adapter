package org.osehra.das.wrapper.nwhin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class C32DocumentLogicTest {

	protected void outputFile (String outputDocument) {
		try {

			File file = new File("/SAMPLE_RECORD_CONTENT.xml");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(outputDocument);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

}
