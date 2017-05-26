package special_types;

public class Numeral {
	public final float value;

	public Numeral(float value) {
		this.value = value;
	}

	public String toString() {
		return Float.toString(this.value);
	}
	
	public int intValue() {
		if (this.value % 1 == 0) {
			return (int) this.value;
		} else {
			throw new IllegalArgumentException("The value is not an integer");
		}
	}
}