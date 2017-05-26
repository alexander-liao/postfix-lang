package operator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import special_types.Flag;

public class OperatorDatabase {
	private static final Map<String, Operator> operators = new HashMap<>();

	public static void register(Operator operator) {
		for (String key : operator.lookup) {
			key = key.toUpperCase();
			if (operators.containsKey(key)) {
				operators.remove(key);
			}
			operators.put(key, operator);
		}
	}

	public static boolean has(String key) {
		return true;
	}

	public static Operator get(String key) {
		return operators.containsKey(key.toUpperCase()) ? operators.get(key
				.toUpperCase()) : new Operator(0, new String[0], new Class[0]) {
			protected Object process(Object[] parameters) {
				return Flag.NULL_FLAG;
			}
		};
	}

	public static Set<String> keyset() {
		return new HashSet<>(operators.keySet());
	}
}