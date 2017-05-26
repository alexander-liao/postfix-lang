package operator;

import java.util.Stack;

import special_types.MultiOutput;

public abstract class Operator {
	public final int parameters_length;
	public final String[] lookup;
	private final Class<?>[] types;

	public Operator(int parameters, String[] lookup, Class<?>[] types) {
		this.parameters_length = parameters;
		this.types = types;
		this.lookup = lookup;
		OperatorDatabase.register(this);
	}

	protected abstract Object process(Object[] parameters);

	public Object process(Stack<?> parameters) {
		if (parameters_length == -1) {
			Object[] params = new Object[parameters.size()];
			for (int index = 0; index < params.length; index++) {
				params[index] = parameters.pop();
				if (!types[0].isInstance(params[index])) {
					throw new IllegalArgumentException(params[index].toString()
							+ " is not an instance of " + types[0]);
				}
			}
			Object output = this.process(params);
			return output == null ? output
					: output instanceof Object[] ? new MultiOutput(
							(Object[]) output) : output;
		} else if (parameters.size() >= parameters_length) {
			Object[] params = new Object[this.parameters_length == -1 ? parameters
					.size() : this.parameters_length];
			for (int index = params.length - 1; index >= 0; index--) {
				params[index] = parameters.pop();
				if (!types[index].isInstance(params[index])) {
					throw new IllegalArgumentException(params[index].toString()
							+ " is not an instance of " + types[index]);
				}
			}
			Object output = this.process(params);
			return output == null ? output
					: output instanceof Object[] ? new MultiOutput(
							(Object[]) output) : output;
		} else {
			throw new IllegalArgumentException("Not enough items on the stack");
		}
	}
}