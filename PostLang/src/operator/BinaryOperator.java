package operator;

public abstract class BinaryOperator extends Operator {
	public BinaryOperator(String[] lookup, Class<?>[] types) {
		super(2, lookup, types);
	}

	public BinaryOperator(String[] lookup, Class<?> type) {
		this(lookup, new Class<?>[] { type, type });
	}

	public BinaryOperator(String[] lookup, Class<?> type1, Class<?> type2) {
		this(lookup, new Class<?>[] { type1, type2 });
	}
}