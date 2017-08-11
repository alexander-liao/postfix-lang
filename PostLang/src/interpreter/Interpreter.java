package interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Stack;

import operator.CommonOperators;
import operator.Operator;
import operator.OperatorDatabase;
import special_types.Flag;
import special_types.MultiOutput;
import special_types.Numeral;
import converters.Converter;

public class Interpreter {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			CommonOperators.init();
			while (true) {
				System.out.print("Code >>> ");
				String input = reader.readLine();
				try {
					System.out.println("\n" + process(input));
				} catch (Exception e) {
					if (args.length < -1) // MODIFIER
						throw e;
					if (e instanceof NullPointerException) {
						System.out
							.println("ERROR No such operator OR program was terminated");
					} else if (e instanceof EmptyStackException) {
						System.out
							.println("ERROR Stack is too small for the operation");
					} else if (e instanceof RuntimeException) {
						System.out.println("ERROR: " + e.getMessage());
					}
				}
			}
		} else {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			CommonOperators.init();
			while (true) {
				String input = reader.readLine():
				try {
					System.out.println("\n" + process(input));
				} catch (Exception e) {
					if (args.length < -1) // MODIFIER
						throw e;
					if (e instanceof NullPointerException) {
						System.out
							.println("ERROR No such operator OR program was terminated");
					} else if (e instanceof EmptyStackException) {
						System.out
							.println("ERROR Stack is too small for the operator");
					} else if (e instanceof RuntimeException) {
						System.out.println("ERROR: " + e.getMessage());
					}
				}
			}
		}
	}

	public static Stack<Object> process(String stream) {
		Object worker = null;
		Stack<Object> stack = new Stack<>();
		boolean debug = false;
		for (int index = 0; index < stream.length(); index++) {
			if (debug) {
				System.out.println("At index " + index);
				System.out.println(stream);
				for (int i = 0; i < index; i++) {
					System.out.print(" ");
				}
				System.out.println("^");
				System.out.println(stack);
			}
			char cc = charAt(stream, index);
			if (cc == '\'' || cc == '\"') {
				StringBuffer string = new StringBuffer();
				char nc;
				for (index++; (nc = charAt(stream, index)) != cc; index++) {
					if (nc == '\\') {
						nc = charAt(stream, ++index);
						string.append(escape(nc));
					} else {
						string.append(nc);
					}
				}
				stack.push(string.toString());
			} else if (cc >= '0' && cc <= '9') {
				StringBuffer string = new StringBuffer(Character.toString(cc));
				boolean decimal = false;
				char nc;
				for (index++; ((nc = charAt(stream, index++)) == '.'
						&& !decimal || nc >= '0' && nc <= '9');) {
					string.append(nc);
				}
				index -= 2;
				stack.push(new Numeral(Float.valueOf(string.toString())));
			} else if (Character.isWhitespace(cc)) {
				// Do nothing for unnecessary whitespace or spacing whitespace
			} else {
				StringBuffer string = new StringBuffer();
				char nc;
				for (; (nc = charAt(stream, index)) != ' '; index++) {
					string.append(nc);
					if (Converter.filter(OperatorDatabase.keyset(),
							string.toString().toUpperCase()).isEmpty()) {
						string.deleteCharAt(string.length() - 1);
						break;
					}
				}
				String key = string.toString();
				if (!OperatorDatabase.has(key)) {
					throw new RuntimeException("No such operator " + key);
				}
				index--;
				Operator operator = OperatorDatabase.get(key);
				Object object;
				if (operator.parameters_length != -1) {
					Stack<Object> substack = new Stack<>();
					for (int i = 0; i < operator.parameters_length; i++) {
						substack.push(stack.pop());
					}
					object = operator.process(substack);
				} else {
					object = operator.process(stack);
				}

				Object[] objects;

				if (object instanceof MultiOutput) {
					objects = ((MultiOutput) object).getArray();
				} else {
					objects = new Object[] { object };
				}

				for (int j = objects.length - 1; j >= 0; j--) {
					Object element = objects[j];
					if (element instanceof Flag) {
						Flag flag = (Flag) element;
						if (flag == Flag.PROCESS_FLAG) {
							for (int i = 0; i < stack.size(); i++) {
								Object obj = stack.get(i);
								if (obj instanceof Flag) {
									stack.remove(obj);
									flag = (Flag) obj;
									if (flag == Flag.END_FLAG) {
										return stack;
									} else if (flag.type
											.equalsIgnoreCase("GOTO")) {
										index = (int) flag.value - 1;
									} else if (flag.type
											.equalsIgnoreCase("SET")) {
										worker = flag.value;
									} else if (flag.type
											.equalsIgnoreCase("WAIT")) {
										for (long time = 0; time < (long) flag.value; time++) {
											try {
												Thread.sleep(1);
											} catch (InterruptedException e) {
												time--;
											}
										}
									} else if (flag == Flag.PUSH_FLAG) {
										stack.push(worker);
									} else if (flag == Flag.BLANK_CHECK_FLAG) {
										stack.push(stack.size() == 0 ? new Numeral(
												1) : new Numeral(0));
									}
								}
							}
						} else if (flag == Flag.DEBUG_FLAG) {
							debug = true;
						} else if (flag != Flag.NULL_FLAG) {
							stack.push(flag);
						}
					} else {
						stack.push(element);
					}
				}
			}
		}
		return stack;
	}

	public static char charAt(String stream, int index) {
		if (index < stream.length()) {
			return stream.charAt(index);
		} else {
			return (char) 0;
		}
	}

	private static char escape(char c) {
		switch (Character.toLowerCase(c)) {
		case 'b':
			return '\b';
		case 'f':
			return '\f';
		case 'n':
			return '\n';
		case 'r':
			return '\r';
		case 't':
			return '\t';
		default:
			return c;
		}
	}
}
