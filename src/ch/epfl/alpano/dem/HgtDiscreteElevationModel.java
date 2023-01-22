
package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Height discrete elevation model
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
	private final Interval2D extent;
	private ShortBuffer buffer;
	private final int FILE_LENGTH= 25934402;
	
	
	/**
	 * @param file
	 * @throws IllegalArgumentException
	 * checks if a file's name is valid and constructs a buffer to read the file's values
	 */
	public HgtDiscreteElevationModel(File file) {
		String s = file.getName();

		try (FileInputStream is = new FileInputStream(file)) {
			
			Preconditions.checkArgument((s.length() == 11)
			        && (s.charAt(0) == 'N' || s.charAt(0) == 'S')
					&& (s.charAt(3) == 'E' || s.charAt(3) == 'W')
					&& (s.substring(7).equals(".hgt")) 
					&& (file.length() ==FILE_LENGTH)  
					&& isNumeric(s.substring(1, 3))
					&& isNumeric(s.substring(4, 7)));

			buffer = is.getChannel().map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
			int longitude = Integer.parseInt(s.substring(4, 7)) * SAMPLES_PER_DEGREE;
			if (s.charAt(0) == 'S')
				longitude *= -1;
			int latitude = Integer.parseInt(s.substring(1, 3)) * SAMPLES_PER_DEGREE;
			if (s.charAt(3) == 'W')
				latitude *= -1;
			extent = new Interval2D(new Interval1D(longitude, longitude + SAMPLES_PER_DEGREE),
					new Interval1D(latitude, latitude + SAMPLES_PER_DEGREE));
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * @param str
	 * @return true if the string is numeric
	 */
	private static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	@Override
	public void close() throws Exception {

		buffer = null;
	}

	@Override
	public Interval2D extent() {
		return extent;

	}

	
	@Override
	public double elevationSample(int x, int y) {
		Preconditions.checkArgument(extent().contains(x, y));
		int x1 = x - extent().iX().includedFrom();
		int y1 = y - extent().iY().includedTo();

		return buffer.get(x1 - (SAMPLES_PER_DEGREE+1) * y1);
	}
}