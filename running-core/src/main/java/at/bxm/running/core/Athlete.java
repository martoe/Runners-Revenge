package at.bxm.running.core;


import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;

public class Athlete extends FitlogNode {

	/** <xs:attribute name="Id" type="xs:string" use="optional"/> */
	@XmlAttribute(name = "Id")
	private String id;
	/** <xs:attribute name="Name" type="xs:string" use="optional"/> */
	@XmlAttribute(name = "Name")
	private String name;
	/** <xs:attribute name="DateOfBirth" type="xs:date" use="optional"/> */
	@XmlAttribute(name = "DateOfBirth")
	private Date birthdate;
	/**
	 * <xs:attribute name="HeightCentimeters" type="xs:decimal" use="optional"/>
	 */
	@XmlAttribute(name = "HeightCentimeters")
	private Double height;
	/** <xs:attribute name="WeightKilograms" type="xs:decimal" use="optional"/> */
	@XmlAttribute(name = "WeightKilograms")
	private Double weight;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public double getHeight() {
		return height;
	}

	public double getWeight() {
		return weight;
	}

}
