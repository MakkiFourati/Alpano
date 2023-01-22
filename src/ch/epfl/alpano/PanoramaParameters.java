/*
 *	Author:      Haitham Hammami
 *	Date:        17 Mar 2017
 */

package ch.epfl.alpano;

import java.util.Objects;
/**
 * Panorama Paramaters: assembles the paramaters needed in a Panorama
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class PanoramaParameters {
    private final int maxDistance, width, observerElevation, height;
    private final GeoPoint observerPosition;
    private final double centerAzimuth, horizontalFieldOfView,verticalFieldOfView;
    // central x and y indexes and the angle between two indexes
    private final double centralX,centralY,unitAngle;

    /**
     * @param observerPosition
     * @param observerElevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     * @throws IllegalArgumentException
     * checks if the arguments are valid and store them
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation,
            double centerAzimuth, double horizontalFieldOfView, int maxDistance,
            int width, int height) {
        Objects.requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(width > 1);
        Preconditions.checkArgument(height > 0);
        Preconditions.checkArgument(maxDistance > 0);
        Preconditions.checkArgument(horizontalFieldOfView > 0
                && horizontalFieldOfView <= Math2.PI2);
        this.centerAzimuth = centerAzimuth;
        this.height = height;
        this.horizontalFieldOfView = horizontalFieldOfView;
        this.maxDistance = maxDistance;
        this.observerElevation = observerElevation;
        this.width = width;
        this.observerPosition = observerPosition;
        centralX=(width-1)/2d;
        centralY=(height-1)/2d;
        unitAngle=horizontalFieldOfView  / (width() - 1);
        verticalFieldOfView=unitAngle * (height() - 1) ;
    }
    /**
     * @return the central azimuth
     */
    public double centerAzimuth(){
        return centerAzimuth;
    }
    /**
     * @return the width
     */
    public int  width(){
        return width;
    }
    /**
     * @return the height
     */
    public int height(){
        return height;
    }
    /**
     * @return the observer's elevation
     */
    public int observerElevation() {
        return observerElevation;
    }
    /**
     * @return the observer's position
     */
    public GeoPoint observerPosition() {
        return observerPosition;
    }
    /**
     * @return the horizontal field of view
     */
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }
    /**
     * @return the maximal distance
     */
    public int maxDistance() {
        return maxDistance;
    }

    /**
     * @return the vertical field of view
     */
    public double verticalFieldOfView() {
        return verticalFieldOfView;
    }
    
    /**
     * @param x
     * @return the azimuth correspending to the abscissa
     */
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=width()-1);
        
        return Azimuth.canonicalize((x-centralX)*unitAngle+ centerAzimuth());
        
    }
    /**
     * @param a
     * @return the abscissa correspending to the azimuth
     */
    public double xForAzimuth(double a){
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(centerAzimuth(), a))<=horizontalFieldOfView/2d);
    return  Math2.angularDistance(centerAzimuth(), a)/unitAngle+ centralX;
    }
    /**
     * @param y
     * @return the altitude for the ordinate
     */
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>=0 && y<=height()-1);
        return (centralY-y)*unitAngle;
    }
    /**
     * @param a
     * @return the ordinate for the altitude
     */
    public double yForAltitude(double a){
        Preconditions.checkArgument(Math.abs(a)<=(verticalFieldOfView()/2));
        return    (-a)/unitAngle+centralY;
    }
    /**
     * @param x
     * @param y
     * @return checks if a sample index is valid
     */
    boolean isValidSampleIndex(int x, int y){
        return (x>=0 && x<=width()-1 && y>=0 && y<=height()-1);
    }
    /**
     * @param x
     * @param y
     * @return the linear sample index of a coordinate point
     * @throws IllegalArgumentException
     */
    int linearSampleIndex(int x, int y){
        Preconditions.checkArgument(isValidSampleIndex(x, y));
        return x +(y*width());
    }
}
