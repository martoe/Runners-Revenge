package at.bxm.running.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

@Deprecated
public class AthleteLog extends FitlogNode {

	/** <xs:element ref="Athlete" minOccurs="0" maxOccurs="1"/> */
	@XmlElement(name = "Athlete", namespace = FitlogNode.NAMESPACE)
	private Athlete athlete;
	/** <xs:element ref="Activity" minOccurs="0" maxOccurs="unbounded"/> */
	@XmlElement(name = "Activity", namespace = FitlogNode.NAMESPACE)
	private List<Activity> activities = new ArrayList<Activity>();
	/** <xs:element ref="History" minOccurs="0" maxOccurs="unbounded"/> */
	@XmlElement(name = "History", namespace = FitlogNode.NAMESPACE)
	private List<History> histories = new ArrayList<History>();

	public Athlete getAthlete() {
		return athlete;
	}

	public List<Activity> getActivities() {
		return activities != null ? activities : Collections.<Activity>emptyList();
	}

	public List<History> getHistories() {
		return histories != null ? histories : Collections.<History>emptyList();
	}

}
