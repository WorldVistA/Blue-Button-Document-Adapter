package org.osehra.das.common.transformer.date;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

/**
 * @author Asha Amritraj
 */
public class XmlGregorianCalendarToDate implements CustomConverter {

	@Override
	public Object convert(final Object destination, final Object source,
			final Class<?> destClass, final Class<?> sourceClass) {
		if (source == null) {
			return null;
		}
		if (XMLGregorianCalendar.class.isInstance(source)) {
			return ((XMLGregorianCalendar) source).toGregorianCalendar()
					.getTime();
		} else if (Date.class.isInstance(source)) {
			final GregorianCalendar gc = new GregorianCalendar();
			gc.setTime((Date) source);
			try {
				return DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gc);
			} catch (final DatatypeConfigurationException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			throw new MappingException(
					"Converter GregorianToDate used incorrectly. Arguments passed in were:"
							+ destination + " and " + source);
		}
	}
}
