package at.bxm.running.xml;

public class XmlDecodingException extends Exception {

	private static final long serialVersionUID = -9027816336092256665L;

	private final int line;
	private final int column;

	public XmlDecodingException(int line, int column, String message) {
		this(line, column, message, null);
	}

	public XmlDecodingException(int line, int column, String message,
			Throwable cause) {
		super("Error at line " + line + ", column " + column + ": " + message,
				cause);
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

}
