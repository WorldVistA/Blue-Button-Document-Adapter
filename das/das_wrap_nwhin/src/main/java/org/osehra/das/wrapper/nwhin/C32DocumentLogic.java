package org.osehra.das.wrapper.nwhin;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class C32DocumentLogic implements Serializable{
	
	private static final Log LOG = LogFactory.getLog(C32DocumentLogic.class);

	public String filterDocument (String document) {
	
		  try {

				Builder builder = new Builder();
				Document doc = builder.build(new StringReader(document));

				/* Just Here to Demonstrate xom reviewing the file, will need to integrate XPATH or XSLT */
				Element root = doc.getRootElement();
				
				/*Print Root Val */
				System.out.println("Root:" + root);

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
