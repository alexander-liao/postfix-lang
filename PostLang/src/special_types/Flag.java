package special_types;

public class Flag {
	public static final Flag NULL_FLAG = new Flag("NULL", null);
	public static final Flag END_FLAG = new Flag("END", null);
	public static final Flag SKIP_FLAG = new Flag("SKIP", null);
	public static final Flag PROCESS_FLAG = new Flag("PROCESS", null);
	public static final Flag PUSH_FLAG = new Flag("PUSH", null);
	public static final Flag BLANK_CHECK_FLAG = new Flag("BLANK", null);
	public static final Flag DEBUG_FLAG = new Flag("DEBUG", null);

	public final String type;
	public final Object value;

	public Flag(String name, Object value) {
		this.type = name;
		this.value = value;
	}

	public String toString() {
		return "Flag\"" + type + "\"";
	}
}