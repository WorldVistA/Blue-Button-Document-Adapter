package org.osehra.das.wrapper.nwhin;

import java.io.Serializable;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class C32DocumentLogic implements C32DocumentEntityFactory, Serializable{
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(C32DocumentLogic.class);
	protected DateTimeFormatter jodaDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
	protected DateTimeFormatter jodaDateTimeFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss");

	public DateTimeFormatter getJodaDateTimeFormat() {
		return jodaDateTimeFormat;
	}

	public void setJodaDateTimeFormat(DateTimeFormatter jodaDateTimeFormat) {
		this.jodaDateTimeFormat = jodaDateTimeFormat;
	}

	public DateTimeFormatter getJodaDateFormat() {
		return jodaDateFormat;
	}

	public void setJodaDateFormat(DateTimeFormatter jodaDateFormat) {
		this.jodaDateFormat = jodaDateFormat;
	}
	
	public C32DocumentEntity createDocument(String patientId, String doc) throws Exception {
		Document xmlDoc = null;
		if (doc!=null) {
			doc = doc.trim();
			if (!"".equals(doc)) {
				Builder builder = new Builder();
				xmlDoc = builder.build(new StringReader(doc));
			}
		}

		return new C32DocumentEntity(patientId, getPatientId(xmlDoc), new java.sql.Date(new java.util.Date().getTime()), getFilteredDocument(xmlDoc));
	}

	protected String getFilteredDocument (Document xmlDoc) {
		if (xmlDoc!=null) {
			filterProblemList(xmlDoc);
			filterLabs(xmlDoc);
			return xmlDoc.toXML();
		}
		return "";
	}

	protected void filterProblemList (Document xmlDoc) {
		//Variable represents # of days delay on results.
		int problemListDelayDays = 7;
		boolean debugging = LOG.isDebugEnabled();

		/*Print Root Val */
		Element root = xmlDoc.getRootElement();
		Elements children1 = root.getChildElements();

		/* Walk the file to find labs */
		/*  Will use effectivetime/Low to filter by date of onset. */

		for(int a = 0; a < children1.size(); a ++){
			maybeDebugLocalName(debugging, children1, a);
			//System.out.println(children1.get(a).getLocalName());
			if (children1.get(a).getLocalName() == "component") {
				Elements children2 = children1.get(a).getChildElements();
				for(int b = 0; b < children2.size(); b ++){
					//System.out.println(children2.get(b).getLocalName());
					maybeDebugLocalName(debugging, children2, b);
					if (children2.get(b).getLocalName() == "structuredBody") {		
						Elements children3 = children2.get(b).getChildElements();
						for(int c = 0; c < children3.size(); c ++){
							//System.out.println(children3.get(c).getLocalName());
							maybeDebugLocalName(debugging, children3, c);
							if (children3.get(c).getLocalName() == "component") {
								Elements children4 = children3.get(c).getChildElements();
								for(int d = 0; d < children4.size(); d++) {
									//System.out.println(children4.get(d).getLocalName());
									maybeDebugLocalName(debugging, children4, d);
									if (children4.get(d).getLocalName() =="section") {
										// Identify Problem List Section by code.
										Elements children5 = children4.get(d).getChildElements();
										for(int e = 0; e < children5.size(); e++) {
											if ("11450-4".equals(children5.get(e).getAttributeValue("code"))) {
												Elements children6 = children4.get(d).getChildElements();
												for(int f = 0; f < children5.size(); f++) {
													if (children6.get(f).getLocalName() =="entry") {
														//System.out.println(children6.get(f).getLocalName());
														maybeDebugLocalName(debugging, children6, f);
														Elements children7 = children6.get(f).getChildElements();
														for(int g = 0; g < children7.size(); g++) {
															if (children7.get(g).getLocalName() == "act") {
																//System.out.println(children7.get(g).getLocalName());
																maybeDebugLocalName(debugging, children7, g);
																Elements children8 = children7.get(g).getChildElements();
																for(int h = 0; h < children8.size(); h++) {
																	if (children8.get(h).getLocalName() == "effectiveTime") {
																		//System.out.println(children8.get(h).getLocalName());
																		maybeDebugLocalName(debugging, children8, h);
																		Elements children9 = children8.get(h).getChildElements();
																		for (int i = 0; i < children9.size(); i++) {
																			if (children9.get(i).getLocalName() == "low") {
																				String ProblemDate = children9.get(i).getAttributeValue("value");
																				//If Problem Date null, no filter applied.
																				if (ProblemDate != null && !ProblemDate.isEmpty()) {	
																					//Labs seem to have a different date, may need to adjust for prod.
																					DateTime dt = getJodaDateFormat().parseDateTime(ProblemDate);
																					LocalDate probDate = dt.toLocalDate();
																					LocalDate currentDate = LocalDate.now();
																					LocalDate minDate = currentDate.minusDays(problemListDelayDays);
																					//System.out.println(probDate.toString());
																					//System.out.println(currentDate.toString());
																					//System.out.println(minDate.toString());


																					//Also blanks entire <text> element, since cannot walk as XML.
																					if (minDate.isBefore(probDate)) {
																						//Logic to destroy <entry> element.
																						children6.get(f).removeChildren();
																						//Blanks entire <text> element for labs,
																						//since cannot be individually filtered, and contain non-XML data.
																						Elements children10 = children4.get(d).getChildElements();
																						for(int j = 0; j < children10.size(); j++) {
																							//System.out.println(children10.get(i).getLocalName());
																							maybeDebugLocalName(debugging, children10, j);
																							if (children10.get(j).getLocalName() =="text") {
																								children10.get(j).removeChildren();
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																} 
															}			
														}
													}
												}			
											}	
										}
									}
								}
							}
						}
					}						
				}
			}
		}
	}
	
	protected void maybeDebugLocalName(boolean debugging, Elements elements, int index) {
		if (debugging) {
			LOG.debug(elements.get(index).getLocalName());
		}
	}

	protected void filterLabs (Document xmlDoc) {
		boolean debugging = LOG.isDebugEnabled();
		//Variable represents # of days delay on results.
		int LabDelayDays = 7;

		/*Print Root Val */
		Element root = xmlDoc.getRootElement();
		Elements children1 = root.getChildElements();

		/* Walk the file to find labs */

		for(int a = 0; a < children1.size(); a ++){				
			//System.out.println(children1.get(a).getLocalName());
			maybeDebugLocalName(debugging, children1, a);
			if (children1.get(a).getLocalName() == "component") {
				Elements children2 = children1.get(a).getChildElements();
				for(int b = 0; b < children2.size(); b ++){
					//System.out.println(children2.get(b).getLocalName());
					maybeDebugLocalName(debugging, children2, b);
					if (children2.get(b).getLocalName() == "structuredBody") {		
						Elements children3 = children2.get(b).getChildElements();
						for(int c = 0; c < children3.size(); c ++){
							//System.out.println(children3.get(c).getLocalName());
							maybeDebugLocalName(debugging, children3, c);
							if (children3.get(c).getLocalName() == "component") {
								Elements children4 = children3.get(c).getChildElements();
								for(int d = 0; d < children4.size(); d++) {
									//System.out.println(children4.get(d).getLocalName());
									maybeDebugLocalName(debugging, children4, d);
									if (children4.get(d).getLocalName() =="section") {
										// Identify Labs Section by code.
										Elements children5 = children4.get(d).getChildElements();
										for(int e = 0; e < children5.size(); e++) {
											if ("30954-2".equals(children5.get(e).getAttributeValue("code"))) {
												Elements children6 = children4.get(d).getChildElements();
												for(int f = 0; f < children5.size(); f++) {
													if (children6.get(f).getLocalName() =="entry") {
														//System.out.println(children6.get(f).getLocalName());
														maybeDebugLocalName(debugging, children6, f);
														Elements children7 = children6.get(f).getChildElements();
														for(int g = 0; g < children7.size(); g++) {
															if (children7.get(g).getLocalName() == "organizer") {
																//System.out.println(children7.get(g).getLocalName());
																maybeDebugLocalName(debugging, children7, g);
																Elements children8 = children7.get(g).getChildElements();
																for(int h = 0; h < children8.size(); h++) {
																	if (children8.get(h).getLocalName() == "effectiveTime") {
																		//System.out.println(children8.get(h).getLocalName());
																		maybeDebugLocalName(debugging, children8, h);
																		//System.out.println(children8.get(h).getAttributeValue("value"));
																		String LabDate = children8.get(h).getAttributeValue("value");

																		//This is the format in the demo file's lab values.  May vary by element.
																		DateTime dt = getJodaDateTimeFormat().parseDateTime(LabDate);
																		LocalDate labDate = dt.toLocalDate();
																		LocalDate currentDate = LocalDate.now();
																		LocalDate minDate = currentDate.minusDays(LabDelayDays);
																		//System.out.println(labDate.toString());
																		//System.out.println(currentDate.toString());
																		//System.out.println(minDate.toString());



																		//Also blanks entire <text> element, since cannot walk as XML.
																		if (minDate.isBefore(labDate)) {
																			//Logic to destroy <entry> element.
																			children6.get(f).removeChildren();
																			//Blanks entire <text> element for labs,
																			//since cannot be individually filtered, and contain non-XML data.
																			Elements children9 = children4.get(d).getChildElements();
																			for(int i = 0; i < children9.size(); i++) {
																				//System.out.println(children9.get(i).getLocalName());
																				maybeDebugLocalName(debugging, children9, i);
																				if (children9.get(i).getLocalName() =="text") {
																					children9.get(i).removeChildren();
																				}
																			}
																		}

																	}
																} 
															}			
														}
													}
												}			
											}	
										}
									}
								}
							}
						}
					}						
				}
			}
		}
	}

	protected String getPatientId (Document xmlDoc) {
		boolean debugging = LOG.isDebugEnabled();
		if (xmlDoc==null) {
			if (debugging) {
				LOG.debug("xmlDoc was null: empty string returned");
			}
			return "";
		}
		String patientIdentifier = "";

		// Build Document Root.
		Element root = xmlDoc.getRootElement();
		Elements children1 = root.getChildElements();

		/* Walk the file to find patient id. */
		for(int a = 0; a < children1.size(); a ++){
			maybeDebugLocalName(debugging, children1, a);
			if (children1.get(a).getLocalName() == "recordTarget") {
				Elements children2 = children1.get(a).getChildElements();
				for(int b = 0; b < children2.size(); b ++){
					maybeDebugLocalName(debugging, children2, b);
					if (children2.get(b).getLocalName() == "patientRole") {		
						Elements children3 = children2.get(b).getChildElements();
						for(int c = 0; c < children3.size(); c ++){
							maybeDebugLocalName(debugging, children3, c);
							if (children3.get(c).getLocalName() == "id") {
								maybeDebugLocalName(debugging, children3, c);
								patientIdentifier = children3.get(c).getAttributeValue("extension");
							}
						}	
					}						
				}
			}
		}

		int carrotIndex = patientIdentifier.indexOf('^');
		if (carrotIndex>-1) {
			return patientIdentifier.substring(0, carrotIndex);
		}
		return patientIdentifier;
	}

}
