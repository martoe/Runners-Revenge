package at.bxm.running.core;

import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Activity extends FitlogNode {

	/** <xs:element ref="Metadata" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Duration" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Distance" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Elevation" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="HeartRate" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Cadence" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Power" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Calories" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element name="Notes" type="xs:string" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element name="Name" type="xs:string" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Laps" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Intensity" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Weather" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Category" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Location" minOccurs="0" maxOccurs="1"/> */
	@XmlElement(name = "Location", namespace = FitlogNode.NAMESPACE)
	private Location location;
	/** <xs:element ref="Route" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="EquipmentUsed" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="Track" minOccurs="0" maxOccurs="1"/> */
	@XmlElement(name = "Track", namespace = FitlogNode.NAMESPACE)
	private Track track;
	/** <xs:element ref="TrackClock" minOccurs="0" maxOccurs="1"/> */
	/** <xs:element ref="DistanceMarkers" minOccurs="0" maxOccurs="1"/> */
	/** <xs:attribute name="StartTime" type="xs:dateTime" use="required"/> */
	@XmlAttribute(name = "StartTime")
	private Date startTime;
	/** <xs:attribute name="Id" type="xs:string" use="required"/> */
	@XmlAttribute(name = "Id")
	private String id;

	public Track getTrack() {
		return track;
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getId() {
		return id;
	}

	public String getLocation() {
		return location == null ? null : location.name;
	}

	// TODO can we do this without an additional class?
	private static class Location {
		/** <xs:attribute name="Id" type="xs:string" use="optional"/> */
		/** <xs:attribute name="Name" type="xs:string" use="optional"/> */
		@XmlAttribute(name = "Name")
		private String name;
	}
}
