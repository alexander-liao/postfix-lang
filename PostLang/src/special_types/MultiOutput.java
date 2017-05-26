package special_types;

import java.util.Stack;

public class MultiOutput {
	private final Object[] array;

	public MultiOutput(Object[] array) {
		this.array = array;
	}

	public int length() {
		return this.array.length;
	}

	public Object get(int index) {
		return this.array[index];
	}

	public Object[] getArray() {
		return this.array.clone();
	}

	public void push(Stack<Object> stack) {
		for (Object object : this.array) {
			if (object instanceof MultiOutput) {
				((MultiOutput) object).push(stack);
			} else {
				stack.push(object);
			}
		}
	}
}