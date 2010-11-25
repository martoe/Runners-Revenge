package at.bxm.running.core;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FitlogNode {

	protected static final String NAMESPACE = "http://www.zonefivesoftware.com/xmlschemas/FitnessLogbook/v2";

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
