package org.osehra.das.wrapper.nwhin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class C32DocumentLogicTest {
	protected IC32DocumentLogic docLogic;
	protected BufferedReader inFileReader;
	
	@Before
	public void setup() {
		docLogic = new C32DocumentLogic();
	}
	
	@After
	public void tearDown() throws IOException {
		if (inFileReader!=null) {
			inFileReader.close();
		}
	}
	
	@Test
	public void getPatientId_happyPath() throws IOException {
		Assert.assertEquals("1012638924V546709", docLogic.getPatientId(getFileAsString("/VA_C32_NWHINONE.xml")));
	}
	
	@Test
	public void getPatientId_emptyString() {
		Assert.assertEquals("", docLogic.getPatientId(""));
	}
	
	@Test
	public void getPatientId_nullString() {
		Assert.assertEquals("", docLogic.getPatientId(null));
	}
	
	@Test
	public void filterDocument_noFiltering() throws IOException {
		String aDoc = getFileAsString("/VA_C32_NWHINONE.xml");
		Assert.assertEquals(aDoc, docLogic.filterDocument(aDoc));
	}
	
	protected String getFileAsString(String fileName) throws IOException {
		inFileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line=inFileReader.readLine())!=null) {
			str.append(line);
		}
		return str.toString();
	}

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
