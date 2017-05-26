package operator;

public abstract class UnaryOperator extends Operator {
	public UnaryOperator(String[] lookup, Class<?> type) {
		super(1, lookup, new Class<?>[] { type });
	}
}