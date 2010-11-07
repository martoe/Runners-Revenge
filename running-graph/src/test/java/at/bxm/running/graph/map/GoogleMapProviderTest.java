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
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 2), 2); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 3), 4); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 4), 8); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 5), 17); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 6), 34); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 7), 69); // home
		assertEquals(googleMapProvider.calculateX(16.3582229614258, 17), 71491); // home
	}

	public void calculateY() throws Exception {
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 2), 1); // home
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 3), 2); // home
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 4), 5); // home
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 5), 11); // home
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 6), 22); // home
		assertEquals(googleMapProvider.calculateY(16.3582229614258, 7), 44); // home
		assertEquals(googleMapProvider.calculateY(48.147533416748, 17), 45482); // home
	}

	public void calculateNorth() throws Exception {
		assertEquals(googleMapProvider.calculateY(85, 2), 0);
		assertEquals(googleMapProvider.calculateY(67, 2), 0);
		assertEquals(googleMapProvider.calculateY(66, 2), 1);
	}

}
