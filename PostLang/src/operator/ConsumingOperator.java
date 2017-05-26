package operator;

public abstract class ConsumingOperator extends Operator {
	public ConsumingOperator(String[] lookup, Class<?> type) {
		super(-1, lookup, new Class<?>[] { type });
	}
}