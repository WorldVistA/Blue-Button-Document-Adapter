package org.osehra.integration.core.enricher;

/**
 * Use a specialized transformer, a Content Enricher, to access an external data
 * source in order to augment a message with missing information. The Content
 * Enricher uses information inside the incoming message (e.g. key fields) to
 * retrieve data from an external source. After the Content Enricher retrieves
 * the required data from the resource, it appends the data to the message. The
 * original information from the incoming message may be carried over into the
 * resulting message or may no longer be needed, depending on the specific needs
 * of the receiving application.
 * 
 * @author Julian Jewel
 * @param <E>
 *            Usually java.lang.Object
 */
public interface Enricher<E> {
	/**
	 * Enrich the object.
	 * 
	 * @param object
	 *            the input object
	 * @return the enriched object
	 * @throws EnricherException
	 *             if an error occured during enrichment
	 */
	E enrich(E object) throws EnricherException;

}
