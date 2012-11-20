package org.osehra.das;

public class BeanUtils {
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
