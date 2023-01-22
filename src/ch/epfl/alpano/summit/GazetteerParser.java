package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;
/**
 * Gazetteer Parser
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class GazetteerParser {

	/**
	 * Gazetteer Parser
	 */
	private GazetteerParser() {
	}

	/**
	 * @param file
	 * @return a final copy of the list of summits
	 * @throws IOException
	 */
	public static List<Summit> readSummitsFrom(File file) throws IOException {
		List<Summit> summitList = new ArrayList<>();
		try (BufferedReader f = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
			String line = f.readLine();
			
			while (line != null) {
				summitList.add(summit(line));
				line = f.readLine();
			}
		}catch(IndexOutOfBoundsException | NumberFormatException e){
			throw new IOException();
		}
		return Collections.unmodifiableList(summitList);
	}

	/**
	 * @param angle
	 * @return the angle in radians from its text format state
	 */
	private static double angleConverter(String angle) {
		String[] dms = angle.split(":");

		return Math
				.toRadians(Integer.parseInt(dms[0]) + Integer.parseInt(dms[1]) / 60.0 + Integer.parseInt(dms[2]) / 3600.0);

	}

	/**
	 * @param line
	 * @return a summit from its text format state
	 */
	private static Summit summit(String line) {
		String name = line.substring(36);
		double longitude = angleConverter(line.substring(0, 9).trim());
		double latitude = angleConverter(line.substring(10, 18).trim());
		int elevation = Integer.parseInt(line.substring(20, 24).trim());
		return new Summit(name, new GeoPoint(longitude, latitude), elevation);
	}

}
