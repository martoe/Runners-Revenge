package at.bxm.running.xml;

@Deprecated
public class XmlDecodingException extends Exception {

	private static final long serialVersionUID = -9027816336092256665L;

	private final int line;
	private final int column;

	public XmlDecodingException(String message) {
		this(message, null);
	}

	public XmlDecodingException(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	public XmlDecodingException(String message, Throwable cause) {
		super(message, cause);
		line = 0;
		column = 0;
	}

	public XmlDecodingException(int line, int column, String message) {
		this(line, column, message, null);
	}

	public XmlDecodingException(int line, int column, String message, Throwable cause) {
		super("Error at line " + line + ", column " + column + ": " + message, cause);
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
