package at.bxm.running.xml;

import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Deprecated
@XmlRootElement(name = "FitnessWorkbook", namespace = FitlogNode.NAMESPACE)
public class FitnessWorkbook extends FitlogNode {

	/** <xs:element ref="AthleteLog" minOccurs="0" maxOccurs="unbounded"/> */
	@XmlElement(name = "AthleteLog", namespace = FitlogNode.NAMESPACE)
	private List<AthleteLog> athleteLogs;

	public List<AthleteLog> getAthleteLogs() {
		return athleteLogs != null ? athleteLogs : Collections.<AthleteLog>emptyList();
	}

	public String toXml() {
		StringWriter out = new StringWriter();
		JAXB.marshal(this, out);
		return out.toString();
	}

	@Override
	public String toString() {
		return toXml();
	}

}
