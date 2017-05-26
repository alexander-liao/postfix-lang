package operator;

public abstract class TernaryOperator extends Operator {
	public TernaryOperator(String[] lookup, Class<?>[] types) {
		super(3, lookup, types);
	}

	public TernaryOperator(String[] lookup, Class<?> type) {
		this(lookup, new Class<?>[] { type, type, type });
	}

	public TernaryOperator(String[] lookup, Class<?> type1, Class<?> type2,
			Class<?> type3) {
		this(lookup, new Class<?>[] { type1, type2, type3 });
	}
}