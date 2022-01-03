package atoms;

/**
 * An atom/variable that may be of different types, along with a VERY helpful
 * toString method in order to quickly print it according to the task
 * @author Rares
 *
 */
public class Atom {
	private AtomType type;
	private Integer arg1;
	private Integer arg2;
	
	public Atom() {}
	
	public String toString() {
		return (type == AtomType.EDGE ? "x" : "a") + arg1 + "-" + arg2;
	}

	// getters and setters
	public AtomType getType() {
		return type;
	}

	public Atom setType(AtomType type) {
		this.type = type;
		
		return this;
	}

	public Integer getArg1() {
		return arg1;
	}

	public Atom setArg1(Integer arg1) {
		this.arg1 = arg1;
		
		return this;
	}

	public Integer getArg2() {
		return arg2;
	}

	public Atom setArg2(Integer arg2) {
		this.arg2 = arg2;
		
		return this;
	}
	
	
}
