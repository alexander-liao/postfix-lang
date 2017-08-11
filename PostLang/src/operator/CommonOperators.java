package operator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import special_types.Flag;
import special_types.MultiOutput;
import special_types.Numeral;
import converters.Converter;

public class CommonOperators {
	public static final BufferedReader reader = getReader(System.in);

	public static final BufferedReader getReader(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream));
	}

	public static final Operator ADDITION = new BinaryOperator(new String[] {
			"+", "ADD", "SUM", "COMBINE" }, Object.class) {
		protected Object process(Object[] parameters) {
			if (parameters[0] instanceof Numeral
					&& parameters[1] instanceof Numeral) {
				return new Numeral(((Numeral) parameters[0]).value
						+ ((Numeral) parameters[1]).value);
			} else if (parameters[0] instanceof String
					|| parameters[1] instanceof String) {
				return parameters[0].toString() + parameters[1].toString();
			} else {
				throw new IllegalArgumentException(
						"Only numbers and strings can be added");
			}
		}
	};

	public static final Operator ADD_ALL = new ConsumingOperator(new String[] {
			"#", "++", "ADD_ALL" }, Object.class) {
		protected Object process(Object[] parameters) {
			boolean allnums = true;
			for (Object object : parameters) {
				if (!(object instanceof Numeral)) {
					allnums = false;
					break;
				}
			}
			if (allnums) {
				float val = 0;
				for (Object object : parameters) {
					val += ((Numeral) object).value;
				}
				return val;
			} else {
				StringBuffer string = new StringBuffer();
				for (Object object : parameters) {
					string.append(object.toString());
				}
				return string.toString();
			}
		}
	};

	public static final Operator SUBTRACT = new BinaryOperator(new String[] {
			"-", "SUBTRACT", "SUB" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).value
					- ((Numeral) parameters[1]).value);
		}
	};

	public static final Operator NEGATE = new UnaryOperator(new String[] { "N",
			"NEGATE", "NEG", "ADDINV", "ADDITIVE_INVERSE" }, Object.class) {
		protected Object process(Object[] parameters) {
			return parameters[0] instanceof Numeral ? new Numeral(
					-((Numeral) parameters[0]).value) : new StringBuffer(
					parameters[0].toString()).reverse().toString();
		}
	};

	public static final Operator PRIME = new UnaryOperator(new String[] { "p",
			"PRIME" }, Object.class) {
		protected Object process(Object[] parameters) {
			if (parameters[0] instanceof Numeral) {
				int number = (int) (((Numeral) parameters[0]).value);
				for (int i = 2; i < number; i++) {
					if (number % i == 0) {
						return new Numeral(0);
					}
				}
				return new Numeral(1);
			} else {
				throw new IllegalArgumentException("Object was not a number for primality check.");
			}
		}
	};

	public static final Operator MULTIPLY = new BinaryOperator(new String[] {
			"*", "MULTIPLY", "PRODUCT", "MULT" }, Numeral.class, Object.class) {
		protected Object process(Object[] parameters) {
			if (parameters[1] instanceof Numeral) {
				return new Numeral(((Numeral) parameters[0]).value
						* ((Numeral) parameters[1]).value);
			} else {
				StringBuffer string = new StringBuffer();
				String base = parameters[1].toString();
				for (int index = 0; index < ((Numeral) parameters[0]).value
						* base.length(); index++) {
					string.append(base.charAt(index % base.length()));
				}
				return string.toString();
			}
		}
	};

	public static final Operator MULTIPLY_ALL = new ConsumingOperator(
			new String[] { "M", "XX", "MULTIPLY_ALL" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			float val = 1;
			for (Object object : parameters) {
				val *= ((Numeral) object).value;
			}
			return new Numeral(val);
		}
	};

	public static final Operator DIVIDE = new BinaryOperator(new String[] {
			"/", "DIVIDE" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).value
					/ ((Numeral) parameters[1]).value);
		}
	};

	public static final Operator INVERSE = new UnaryOperator(new String[] {
			"I", "RECIPROCAL", "RECIP", "MULTINV", "MULTIPLICATIVE_INVERSE" },
			Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(1.0f / ((Numeral) parameters[0]).value);
		}
	};

	public static final Operator FACTORIAL = new UnaryOperator(new String[] {
			"!", "FACT", "FACTORIAL" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			int val = ((Numeral) parameters[0]).intValue();
			int result = 1;
			for (int i = 2; i <= val; i++) {
				result *= i;
			}
			return new Numeral(result);
		}
	};

	public static final Operator EXPONENTIATE = new BinaryOperator(
			new String[] { "**", "EXPONENTIATE", "EXP", "POW", "POWER", "P" },
			Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral((float) Math.pow(
					((Numeral) parameters[0]).value,
					((Numeral) parameters[1]).value));
		}
	};

	public static final Operator REVERSE = new ConsumingOperator(new String[] {
			"REVERSE", "<->", "<>" }, Object.class) {
		protected Object process(Object[] parameters) {
			Object[] output = new Object[parameters.length];
			for (int i = 0; i < output.length; i++) {
				output[i] = parameters[output.length - i - 1];
			}
			return output;
		}
	};

	public static final Operator RANGE = new TernaryOperator(new String[] {
			"RANGE", ")" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			float start = ((Numeral) parameters[0]).value;
			float bound = ((Numeral) parameters[1]).value;
			float step = ((Numeral) parameters[2]).value;
			Object[] array = new Object[(int) Math
					.floor((bound - start) / step)];
			for (int i = 0; i < array.length; i++) {
				array[i] = new Numeral(start + step * i);
			}
			return array;
		}
	};

	public static final Operator SWITCH = new TernaryOperator(new String[] {
			"SWITCH", "=" }, Object.class) {
		protected Object process(Object[] parameters) {
			return Converter.ifCheck(parameters[0]) ? parameters[1]
					: parameters[2];
		}
	};

	public static final Operator DEL = new UnaryOperator(new String[] { "DEL",
			"D" }, Object.class) {
		protected Object process(Object[] parameters) {
			return Flag.NULL_FLAG;
		}
	};

	public static final Operator POP = new UnaryOperator(new String[] { ".",
			"_", "POP" }, Object.class) {
		protected Object process(Object[] parameters) {
			System.out.print(parameters[0]);
			return Flag.NULL_FLAG;
		}
	};

	public static final Operator PEEK = new UnaryOperator(new String[] {
			"PEEK", "}" }, Object.class) {
		protected Object process(Object[] parameters) {
			System.out.println(parameters[0]);
			return parameters[0];
		}
	};

	public static final Operator BIT_FLIP = new UnaryOperator(new String[] {
			"BIT_FLIP", "|" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(Integer.parseInt(
					Integer.toString(((Numeral) parameters[0]).intValue(), 2)
							.replaceAll("1", "/").replaceAll("0", "1")
							.replaceAll("/", "0"), 2));
		}
	};

	public static final Operator GOTO = new UnaryOperator(new String[] {
			"JUMP", "GOTO", ";" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Flag("GOTO", ((Numeral) parameters[0]).intValue());
		}
	};

	public static final Operator END = new GeneratorOperator(new String[] {
			"END", "TERMINATE", "->|", "]" }) {
		protected Object process(Object[] parameters) {
			return Flag.END_FLAG;
		}
	};

	public static final Operator FLOOR = new UnaryOperator(new String[] {
			"FLOOR", "FLR", "V" }, Object.class) {
		protected Object process(Object[] parameters) {
			return parameters[0] instanceof Numeral ? new Numeral(
					(float) Math.floor(((Numeral) parameters[0]).value))
					: parameters[0].toString().toLowerCase();
		}
	};

	public static final Operator CEIL = new UnaryOperator(new String[] {
			"CEILING", "CEIL", "^" }, Object.class) {
		protected Object process(Object[] parameters) {
			return parameters[0] instanceof Numeral ? new Numeral(
					(float) Math.ceil(((Numeral) parameters[0]).value))
					: parameters[0].toString().toUpperCase();
		}
	};

	public static final Operator ROUND = new UnaryOperator(new String[] {
			"ROUND", "~" }, Object.class) {
		protected Object process(Object[] parameters) {
			return parameters[0] instanceof Numeral ? new Numeral(
					(float) Math.round(((Numeral) parameters[0]).value))
					: parameters[0].toString().trim();
		}
	};

	public static final Operator RANDOM = new GeneratorOperator(new String[] {
			"RANDOM", "RAND", "RND", "?" }) {
		protected Object process(Object[] parameters) {
			return new Numeral((float) Math.random());
		}
	};

	public static final Operator INPUT = new GeneratorOperator(new String[] {
			"IN", "INPUT", "," }) {
		protected Object process(Object[] parameters) {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};

	public static final Operator PARSE = new UnaryOperator(new String[] {
			"PARSE", "NUMERIFY", "NUM", "$" }, Object.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(Float.parseFloat(parameters[0].toString()));
		}
	};

	public static final Operator PARSE_RADIX = new BinaryOperator(new String[] {
			"RADIX", "R" }, Object.class, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(Integer.parseInt(parameters[1].toString(),
					((Numeral) parameters[0]).intValue()));
		}
	};

	public static final Operator UNPARSE = new UnaryOperator(new String[] {
			"UNPARSE", "STRINGIFY", "STR", "@" }, Object.class) {
		protected Object process(Object[] parameters) {
			return parameters[0].toString();
		}
	};

	public static final Operator DUPLICATE = new UnaryOperator(new String[] {
			"C", "COPY", "DUPLICATE" }, Object.class) {
		protected Object process(Object[] parameters) {
			return new Object[] { parameters[0], parameters[0] };
		}
	};

	public static final Operator MODULO = new BinaryOperator(new String[] {
			"MOD", "MODULO", "%" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).value
					% ((Numeral) parameters[1]).value);
		}
	};

	public static final Operator COMBINE_DIGITS = new BinaryOperator(
			new String[] { "COMBINE", "COMBINE_DIGITS", "x10+", ":" },
			Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).value * 10
					+ ((Numeral) parameters[1]).value);
		}
	};

	public static final Operator SWAP_TOP = new BinaryOperator(new String[] {
			"SWAP", "\\" }, Object.class) {
		protected Object process(Object[] parameters) {
			return new Object[] { parameters[0], parameters[1] };
		}
	};

	public static final Operator SKIP = new GeneratorOperator(new String[] {
			"SKIP", "`" }) {
		protected Object process(Object[] parameters) {
			return Flag.SKIP_FLAG;
		}

	};

	public static final Operator EMPTY_STRING = new GeneratorOperator(
			new String[] { "EMPTY", "EMPTY_STRING", "E" }) {
		protected Object process(Object[] parameters) {
			return "";
		}
	};

	public static final Operator PUSH = new GeneratorOperator(new String[] {
			"PUSH", "W", "WORKER" }) {
		protected Object process(Object[] parameters) {
			return Flag.PUSH_FLAG;
		}
	};

	public static final Operator SET_WORKER = new UnaryOperator(new String[] {
			"PULL", "SET", "<" }, Object.class) {
		protected Object process(Object[] parameters) {
			return new Flag("SET", parameters[0]);
		}
	};

	public static final Operator CONTAINS = new BinaryOperator(new String[] {
			"CONTAINS", "O" }, String.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(
					((String) parameters[0]).contains((String) parameters[0]) ? 1
							: 0);
		}
	};

	public static final Operator BLANK = new GeneratorOperator(new String[] {
			"BLANK", "B" }) {
		protected Object process(Object[] parameters) {
			return Flag.BLANK_CHECK_FLAG;
		}
	};

	// Version 2.0 - "Boolean" Operators and Operators working with Binary
	// Version 2.0 - Set Operators and working with Strings
	// Version 2.0 - Reflection

	public static final Operator BOOLEAN = new UnaryOperator(new String[] {
			"BOOLEAN", "BOOL", "[" }, Object.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(Converter.ifCheck(parameters[0]) ? 1 : 0);
		}
	};

	public static final Operator AND = new UnaryOperator(new String[] { "AND",
			"&" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).intValue()
					& ((Numeral) parameters[1]).intValue());
		}
	};

	public static final Operator OR = new UnaryOperator(new String[] { "OR",
			"|" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).intValue()
					| ((Numeral) parameters[1]).intValue());
		}
	};

	public static final Operator XOR = new UnaryOperator(new String[] { "XOR",
			"^" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral(((Numeral) parameters[0]).intValue()
					^ ((Numeral) parameters[1]).intValue());
		}
	};

	public static final Operator IMPLIES = new UnaryOperator(new String[] {
			"IMPLIES", "->" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Numeral((((Numeral) parameters[0]).intValue() == 0)
					|| (((Numeral) parameters[1]).intValue() != 0) ? 1 : 0);
		}
	};

	public static final Operator EQUALS = new UnaryOperator(new String[] {
			"EQUALS", "==" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return Converter.equalsCheck(parameters[0], parameters[1]);
		}
	};

	public static final Operator ENCODE = new UnaryOperator(new String[] {
			"ENCODE", "EN" }, String.class) {
		protected Object process(Object[] parameters) {
			String string = (String) parameters[0];
			if (string.length() == 0) {
				return new Numeral(string.charAt(0));
			} else {
				throw new IllegalArgumentException(
						"Input for encoding must be a single character");
			}
		}
	};

	// Regex Operations

	public static final Operator SPLIT = new BinaryOperator(new String[] {
			"SPLIT", "||" }, String.class, String.class) {
		protected Object process(Object[] parameters) {
			return ((String) parameters[1]).split((String) parameters[0]);
		}
	};

	public static final Operator MATCHES = new BinaryOperator(new String[] {
			"MATCHES", "" }, String.class, String.class) {
		protected Object process(Object[] parameters) {
			return Pattern.compile((String) parameters[0])
					.matcher((String) parameters[1]).matches();
		}
	};

	public static final Operator FIND = new BinaryOperator(new String[] {
			"FIND", "F" }, String.class, String.class) {
		protected Object process(Object[] parameters) {
			List<Numeral> list = new ArrayList<>();
			Matcher matcher = Pattern.compile((String) parameters[0]).matcher(
					(String) parameters[1]);
			while (matcher.find()) {
				list.add(new Numeral(matcher.start()));
			}
			return list.toArray(new Numeral[list.size()]);
		}
	};

	// Set Operators

	public static final Operator SUBARRAY = new ConsumingOperator(new String[] {
			"SUBARRAY", "SPLICE", "S" }, Object.class) {
		protected Object process(Object[] parameters) {
			int length = parameters.length - 2;
			int start = (((Numeral) parameters[0]).intValue() % length + length)
					% length;
			int end = (((Numeral) parameters[1]).intValue() % length + length)
					% length;
			Object[] output = new Object[end - start + 1];
			System.arraycopy(parameters, start + 2, output, 0, output.length);
			return output;
		}
	};

	public static final Operator BURY = new ConsumingOperator(new String[] {
			"BURY", "VV" }, Object.class) {
		protected Object process(Object[] parameters) {
			Object buf = parameters[0];
			for (int i = 0; i < parameters.length - 1; i++) {
				parameters[i] = parameters[i + 1];
			}
			parameters[parameters.length - 1] = buf;
			return parameters;
		}
	};

	public static final Operator DIG = new ConsumingOperator(new String[] {
			"DIG", "^^" }, Object.class) {
		protected Object process(Object[] parameters) {
			Object buf = parameters[parameters.length - 1];
			for (int i = parameters.length - 1; i > 0; i--) {
				parameters[i] = parameters[i - 1];
			}
			parameters[0] = buf;
			return parameters;
		}
	};

	public static final Operator LENGTH = new ConsumingOperator(new String[] {
			"LENGTH", "LEN", "L" }, Object.class) {
		protected Object process(Object[] parameters) {
			Object[] output = new Object[parameters.length + 1];
			System.arraycopy(parameters, 0, output, 1, parameters.length);
			output[0] = new Numeral(parameters.length);
			return output;
		}
	};

	// Reflection Operators

	public static final Operator MAP = new ConsumingOperator(
			new String[] { "MAP" }, Object.class) {
		protected Object process(Object[] parameters) {
			Operator operator = OperatorDatabase.get((String) parameters[0]);
			Object[] doubled = new Object[parameters.length * 2 - 2];
			System.arraycopy(parameters, 1, doubled, 0, parameters.length - 1);
			System.arraycopy(parameters, 1, doubled, parameters.length - 1,
					parameters.length - 1);
			Object[] output = new Object[parameters.length - 1];
			Object[] input = new Object[operator.parameters_length];
			for (int i = 0; i < output.length; i++) {
				System.arraycopy(doubled, i, input, 0, input.length);
				output[i] = operator.process(input);
			}
			return output;
		}
	};

	public static final Operator EXECUTE = new ConsumingOperator(new String[] {
			"EXECUTE", "EXEC", "X" }, Object.class) {
		protected Object process(Object[] parameters) {
			Operator operator = OperatorDatabase.get((String) parameters[0]);
			Object[] params = new Object[operator.parameters_length == -1 ? parameters.length - 1
					: operator.parameters_length];
			System.arraycopy(parameters, 1, params, 0, params.length);
			Object output = operator.process(params);
			if (output instanceof MultiOutput) {
				Object[] array = ((MultiOutput) output).getArray();
				output = new Object[array.length + parameters.length
						- params.length - 1];
				System.arraycopy(parameters, params.length, output, 0,
						parameters.length - params.length);
				System.arraycopy(array, 0, output, params.length, array.length);
				return array;
			} else {
				Object[] out = new Object[parameters.length - params.length];
				System.arraycopy(parameters, params.length, out, 0,
						parameters.length - params.length - 1);
				out[out.length - 1] = output;
				return out;
			}
		}
	};

	// SUDO Operators

	public static final Operator DEBUG = new GeneratorOperator(
			new String[] { "DEBUG" }) {
		protected Object process(Object[] parameters) {
			return Flag.DEBUG_FLAG;
		}
	};

	public static final Operator PROCESS = new GeneratorOperator(new String[] {
			"PROCESS", "START", "FLAG", ">" }) {
		protected Object process(Object[] parameters) {
			return Flag.PROCESS_FLAG;
		}
	};

	// Time Operators

	public static final Operator TIME = new GeneratorOperator(new String[] {
			"SYSTEM_TIME", "TIME", "T" }) {
		protected Object process(Object[] parameters) {
			return new Numeral(System.currentTimeMillis());
		}
	};

	public static final Operator WAIT = new UnaryOperator(new String[] {
			"WAIT", "...", "..", "Z" }, Numeral.class) {
		protected Object process(Object[] parameters) {
			return new Flag("WAIT", (long) ((Numeral) parameters[0]).value);
		}
	};

	// Automata Operators, also forces all of the operators to be loaded

	public static void init() {
		for (int rule = 0; rule <= 255; rule++) {
			final int r = rule;
			new ConsumingOperator(new String[] { "RULE_" + r, "RULE" + r,
					"R" + r }, Object.class) {
				protected Object process(Object[] parameters) {
					Object[] output = new Object[parameters.length];
					for (int index = 0; index < parameters.length; index++) {
						Object[] sub = new Object[] {
								index > 0 ? parameters[index - 1] : 0,
								parameters[index],
								index < parameters.length - 1 ? parameters[index + 1]
										: 0 };
						int val = (Converter.ifCheck(sub[0]) ? 4 : 0)
								+ (Converter.ifCheck(sub[1]) ? 2 : 0)
								+ (Converter.ifCheck(sub[0]) ? 1 : 0);
						int result = (r >> val) & 1;
						output[index] = new Numeral(result);
					}
					return output;
				}
			};
		}
	}
}
