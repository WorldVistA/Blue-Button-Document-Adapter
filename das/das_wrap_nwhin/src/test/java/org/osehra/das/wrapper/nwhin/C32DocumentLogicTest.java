package org.osehra.das.wrapper.nwhin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class C32DocumentLogicTest {
	protected IC32DocumentLogic docLogic;
	protected DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd");
	protected DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	
	@Before
	public void setup() {
		docLogic = new C32DocumentLogic();
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
	public void filterDocument_noFiltering() throws Exception {
		String aDoc = getFileAsString("/VA_C32_NWHINONE.xml");
		Builder builder = new Builder();
		Document doc = builder.build(new StringReader(aDoc));
		String expectedDoc = doc.toXML();
		String resultDoc = docLogic.filterDocument(aDoc);
		boolean matched = expectedDoc.equals(resultDoc);
		if (!matched) {
			FileWriter fileWriter = new FileWriter("errorOutput.txt");
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("===============");
			printWriter.println(expectedDoc);
			printWriter.println("===============");
			printWriter.println(resultDoc);
			printWriter.close();
		}
		Assert.assertTrue(matched);
	}
	
	@Test
	public void filterDocument_filterProblems() throws Exception{
		DocElementTestParams youngParams = new DocElementTestParams(6, 20);
		DocElementTestParams olderParams = new DocElementTestParams(7, 21);
		
		String aDoc = getCompositeTestDoc(getProblemTextEntry(youngParams)+getProblemTextEntry(olderParams), 
				getProblemEntry(youngParams)+getProblemEntry(olderParams), 
				null, 
				null);
		String filteredDoc = docLogic.filterDocument(aDoc);
		Assert.assertTrue(filteredDoc.indexOf("#pndProblem" + youngParams.getIndex())<0);
		Assert.assertTrue(filteredDoc.indexOf("#pndProblem" + olderParams.getIndex())>=0);
		Assert.assertTrue(filteredDoc.indexOf("pndDateOfOnset" + youngParams.getIndex())<0);
	}
	
	@Test
	public void filterDocument_filterResults() throws Exception {
		DocElementTestParams youngParams = new DocElementTestParams(6, 50);
		DocElementTestParams olderParams = new DocElementTestParams(7, 51);
		String aDoc = getCompositeTestDoc(null, 
				null, 
				getLabTextEntry(youngParams)+getLabTextEntry(olderParams), 
				getLabEntry(youngParams)+getLabEntry(olderParams));
		String filteredDoc = docLogic.filterDocument(aDoc);
		Assert.assertTrue(filteredDoc.indexOf("#lndComment"+olderParams.getIndex())>=0);
		Assert.assertTrue(filteredDoc.indexOf("#lndComment"+youngParams.getIndex())<0);
		Assert.assertTrue(filteredDoc.indexOf("lndDateTime"+olderParams.getIndex())<0);
		Assert.assertTrue(filteredDoc.indexOf("lndDateTime"+youngParams.getIndex())<0);
	}
	
	protected String getLabEntry(DocElementTestParams testParams) throws IOException {
		return String.format(getFileAsString("/resultTemplate.txt"), dateTimeFormat.print(new DateTime().minusDays(testParams.getDaysBack())), testParams.getIndex());
	}

	protected String getLabTextEntry(DocElementTestParams testParams) throws IOException {
		return String.format(getFileAsString("/resultTextTemplate.txt"), dateTimeFormat.print(new DateTime().minusDays(testParams.getDaysBack())), testParams.getIndex());
	}

	protected String getProblemTextEntry(DocElementTestParams testParams) throws IOException {
		return String.format(getFileAsString("/problemTextTemplate.txt"), dateFormat.print(new DateTime().minusDays(testParams.getDaysBack())), testParams.getIndex());
	}

	protected String getProblemEntry(DocElementTestParams testParams) throws IOException {
		return String.format(getFileAsString("/problemTemplate.txt"), dateFormat.print(new DateTime().minusDays(testParams.getDaysBack())), testParams.getIndex());
	}

	protected String getCompositeTestDoc(String midProbText, String probText, String midLabText, String labText) throws IOException {
		StringBuilder str = new StringBuilder(getFileAsString("/VA_C32_NWHINONE_TO_MID_PROB_TEXT.txt"));
		str.append(midProbText);
		str.append(getFileAsString("/VA_C32_NWHINONE_MID_PROBS_TEXT_TO_MID_PROBS.txt"));
		str.append(probText);
		str.append(getFileAsString("/VA_C32_NWHINONE_MID_PROBS_TO_MID_LABS_TEXT.txt"));
		str.append(midLabText);
		str.append(getFileAsString("/VA_C32_NWHINONE_MID_LABS_TEXT_TO_MID_LABS.txt"));
		str.append(labText);
		str.append(getFileAsString("/VA_C32_NWHINONE_MID_LABS_TO_END.txt"));
		return str.toString();
	}
	
	protected String getFileAsString(String fileName) throws IOException {
		BufferedReader inFileReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line=inFileReader.readLine())!=null) {
			str.append(line);
		}
		inFileReader.close();
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
