package at.bxm.running.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class TrackPoint extends FitlogNode {

	/** <xs:attribute name="tm" type="xs:decimal" use="required"/> */
	@XmlAttribute(name = "tm")
	private double tm;
	/** <xs:attribute name="lat" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "lat")
	private Double latitude;
	/** <xs:attribute name="lon" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "lon")
	private Double longitude;
	/** <xs:attribute name="ele" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "ele")
	private Double ele;
	/** <xs:attribute name="dist" type="xs:decimal" use="optional"/> */
	/** <xs:attribute name="hr" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "hr")
	private Double hr;

	/** <xs:attribute name="cadence" type="xs:decimal" use="optional"/> */
	/** <xs:attribute name="power" type="xs:decimal" use="optional"/> */

	public double getTm() {
		return tm;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getEle() {
		return ele;
	}

	public Double getHr() {
		return hr;
	}

}
