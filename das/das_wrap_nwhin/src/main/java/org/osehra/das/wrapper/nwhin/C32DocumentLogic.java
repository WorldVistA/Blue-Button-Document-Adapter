package org.osehra.das.wrapper.nwhin;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.XPathContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class C32DocumentLogic implements Serializable{
	
	private static final Log LOG = LogFactory.getLog(C32DocumentLogic.class);

	public String filterDocument (String document) {
	
		  try {

				Builder builder = new Builder();
				Document doc = builder.build(new StringReader(document));

				/* Just Here to Demonstrate xom reviewing the file, will need to integrate XPATH or XSLT */

				//Nodes nodes = doc.query("/ClinicalDocument/realmCode");
				//Elements els = doc.
				//System.out.println(nodes.get(1).getValue());
				
				/*Print Root Val */
				Element root = doc.getRootElement();
				Elements children1 = root.getChildElements();

				System.out.println("Root:" + root);
				System.out.println("Sub Count:" + children1.size());
				
				
				/* Walk the file to find labs */
				
				for(int a = 0; a < children1.size(); a ++){				
					//System.out.println(children1.get(a).getLocalName());
					if (children1.get(a).getLocalName() == "component") {
						Elements children2 = children1.get(a).getChildElements();
						for(int b = 0; b < children2.size(); b ++){
							//System.out.println(children2.get(b).getLocalName());
							if (children2.get(b).getLocalName() == "structuredBody") {		
								Elements children3 = children2.get(b).getChildElements();
								for(int c = 0; c < children3.size(); c ++){
								//System.out.println(children3.get(c).getLocalName());
									if (children3.get(c).getLocalName() == "component") {
									Elements children4 = children3.get(c).getChildElements();
										for(int d = 0; d < children4.size(); d++) {
										//System.out.println(children4.get(d).getLocalName());
											if (children4.get(d).getLocalName() =="section") {
												// Identify Labs Section by code.
												Elements children5 = children4.get(d).getChildElements();
												for(int e = 0; e < children5.size(); e++) {
													 String Attr1 = children5.get(e).getAttributeValue("code");
													 System.out.println(Attr1);
													 if (Attr1 == "47519-4") {
														System.out.println(children5.get(e).getLocalName());
														System.out.println(children5.get(e).getAttributeValue("code"));
													
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

				
				
				
				
/*				for(int x = 0; x < children1.size(); x = x+1){
					System.out.println(children1.get(x).getValue());
				}*/
				//System.out.println(nodes.get(0).getValue());
				
				
/*				for(int x = 0; x < nodes.size(); x ++){
					//System.out.println("node "+x+": "+nodes.get(x).toXML());
					System.out.println(nodes.get(x).getValue());
				}	*/		
				
				
			  } catch (IOException io) {
				System.out.println(io.getMessage());
				LOG.error(io);
			  } catch (ParsingException parserr) {
				LOG.error(parserr);
			  }
		
		
		
		String filteredDocument = document;		
		return filteredDocument;
		
		}
		

	
}
