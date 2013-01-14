package org.osehra.das;

/**
 * A toolkit which can be used across classes.  Duplicative some of the 
 * integration packages, should be rolled into them in a later release.
 *
 * @author Dept of VA
 *
 */
public class BeanUtils {
	
	/**
	 * A null safe comparison tool.
	 * 
	 * @param item1		String 1 to be compared.
	 * @param item2		String 2 to be compared.
	 * @return			Returns true if strings are equal or are both null, 
	 * false if unequal and both are not null.
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
	
	/**
	 * A null safe <code>startsWith</code> method for <code>String</code>s.
	 * @param targetString
	 * @param fragment
	 * @return Returns <code>false</code> if targetString or fragment is <code>null</code>.  Delegates to the <code>String</code> method otherwise. 
	 */
	public static boolean startsWithNullSafe(String targetString, String fragment) {
		if (targetString!=null && fragment!=null) {
			return targetString.startsWith(fragment);
		}
		return false;
	}
	
}
