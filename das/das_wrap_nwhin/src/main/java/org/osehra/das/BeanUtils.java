package org.osehra.das;

/**
 * A toolkit which can be used across classes.  Duplicative some of the integration packages, should be rolled into them in a later release.
 * 
 * @author Steve Monson
 *
 */
public class BeanUtils {
	
	/**
	 * A null safe string comparison tool.
	 * 
	 * @param item1		String 1 to be compared.
	 * @param item2		String 2 to be compared.
	 * @return			Returns true if strings are equal or are both null, false if unequal and both are not null.
	 */
	public static boolean equalsNullSafe(Object item1, Object item2) {
		if (item1==null && item2==null) {
			return true;
		}
		if (item1!=null && item2!=null) {
			return item1.equals(item2);
		}
		return false;
	}
}
