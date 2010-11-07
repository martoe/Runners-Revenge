package at.bxm.running.graph.map;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test
public class GoogleMapProviderTest {

	private GoogleMapProvider googleMapProvider;

	@BeforeMethod
	public void setup() {
		googleMapProvider = new GoogleMapProvider();
	}

	public void calculateX() throws Exception {
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 2), 2);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 3), 4);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 4), 8);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 5), 17);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 6), 34);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 7), 69);
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 17), 71491);
	}

	public void calculateY() throws Exception {
		assertEquals(googleMapProvider.calculateY(48.147533416748, 1), 0);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 2), 1);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 3), 2);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 4), 5);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 5), 11);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 6), 22);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 7), 44);
		assertEquals(googleMapProvider.calculateY(48.147533416748, 17), 45482);
	}

	public void calculateNorth() throws Exception {
		assertEquals(googleMapProvider.calculateY(85, 2), 0);
		assertEquals(googleMapProvider.calculateY(67, 2), 0);
		assertEquals(googleMapProvider.calculateY(66, 2), 1);
	}

}
