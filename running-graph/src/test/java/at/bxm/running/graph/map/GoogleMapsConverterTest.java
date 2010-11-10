package at.bxm.running.graph.map;

import static org.testng.Assert.*;
import at.bxm.running.graph.TestBase;
import org.testng.annotations.Test;

@Test
public class GoogleMapsConverterTest extends TestBase {

	public void toX() throws Exception {
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 2), 2);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 3), 4);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 4), 8);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 5), 17);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 6), 34);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 7), 69);
		assertEquals(GoogleMapsConverter.toX(16.3582229614258, 17), 71491);
	}

	@Test
	public void toY() throws Exception {
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 1), 0);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 2), 1);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 3), 2);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 4), 5);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 5), 11);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 6), 22);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 7), 44);
		assertEquals(GoogleMapsConverter.toY(48.147533416748, 17), 45482);
	}

	@Test
	public void toLatitude() throws Exception {
		assertEquals(GoogleMapsConverter.toLatitude(0, 1), 85.05112877980659);
		assertEquals(GoogleMapsConverter.toLatitude(1, 2), 66.51326044311186);
	}

	@Test
	public void calculateNorth() throws Exception {
		assertEquals(GoogleMapsConverter.toY(85, 2), 0);
		assertEquals(GoogleMapsConverter.toY(67, 2), 0);
		assertEquals(GoogleMapsConverter.toY(66, 2), 1);
	}

}
