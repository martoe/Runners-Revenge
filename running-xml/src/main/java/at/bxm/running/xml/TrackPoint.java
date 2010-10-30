package at.bxm.running.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class TrackPoint extends FitlogNode {

	/** <xs:attribute name="tm" type="xs:decimal" use="required"/> */
	@XmlAttribute(name = "tm")
	private double tm;
	/** <xs:attribute name="lat" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "lat")
	private Double lat;
	/** <xs:attribute name="lon" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "lon")
	private Double lon;
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

	public Double getLat() {
		return lat;
	}

	public Double getLon() {
		return lon;
	}

	public Double getEle() {
		return ele;
	}

	public Double getHr() {
		return hr;
	}

}
