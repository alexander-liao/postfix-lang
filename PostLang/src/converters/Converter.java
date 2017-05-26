package converters;

import java.util.HashSet;
import java.util.Set;

import special_types.Flag;
import special_types.Numeral;

public class Converter {
	public static boolean ifCheck(Object object) {
		if (object == null) {
			return false;
		} else if (object instanceof String) {
			return ((String) object).length() != 0;
		} else if (object instanceof Numeral) {
			return ((Numeral) object).value != 0;
		} else if (object instanceof Flag) {
			return false;
		} else if (object instanceof Boolean) {
			return (Boolean) object;
		} else {
			return true;
		}
	}

	public static boolean equalsCheck(Object obj1, Object obj2) {
		if (obj1 == null) {
			return obj2 == null;
		} else if (obj1.equals(obj2)) {
			return true;
		} else if (obj1 instanceof Numeral && obj2 instanceof Numeral) {
			return ((Numeral) obj1).value == ((Numeral) obj2).value;
		} else {
			return false;
		}
	}

	public static Set<String> filter(Set<String> set, String prefix) {
		Set<String> output = new HashSet<>(set);
		for (String element : set) {
			if (!element.startsWith(prefix)) {
				output.remove(element);
			}
		}
		return output;
	}
}