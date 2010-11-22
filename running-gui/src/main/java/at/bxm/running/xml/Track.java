package at.bxm.running.xml;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

@Deprecated
public class Track extends FitlogNode {

	/** <xs:element ref="Metadata" minOccurs="0" maxOccurs="unbounded"/> */
	/** <xs:element ref="pt" minOccurs="0" maxOccurs="unbounded"/> */
	@XmlElement(name = "pt", namespace = FitlogNode.NAMESPACE)
	private List<TrackPoint> points;

	/** <xs:attribute name="StartTime" type="xs:dateTime" use="required"/> */

	public List<TrackPoint> getPoints() {
		return points != null ? points : Collections.<TrackPoint>emptyList();
	}

}
