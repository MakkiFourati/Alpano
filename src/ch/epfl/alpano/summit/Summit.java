package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;
/**
 * Summit
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class Summit {

	private final String name;
	private final GeoPoint position;
	private final int elevation;
	
	
	/**
	 * @param name
	 * @param position
	 * @param elevation
	 * @throws NullPointerException
	 * checks if the name or the position are null and constructs a summit
	 */
	public Summit(String name, GeoPoint position, int elevation) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(position);
		this.name = name;
		this.position = position;
		this.elevation = elevation;
	}
	
	/**
	 * @return the summit's name
	 */
	public String name(){
		return this.name;
	}
	
	/**
	 * @return the summit's position
	 */
	public GeoPoint position(){
		return this.position;
	}
	
	/**
	 * @return the summit's elevation
	 */
	public int elevation(){
		return this.elevation;
	}
	
	@Override
	public String toString() {
		return this.name() + " " + this.position().toString() + " " + this.elevation();
	}
}
