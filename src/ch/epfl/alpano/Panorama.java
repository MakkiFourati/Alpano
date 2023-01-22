package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;



public final class Panorama {

    private final PanoramaParameters parameters;
    private final float[] distances, longitudes, latitudes, elevations, slopes;

    /**
     * @param parameters
     * @param distances
     * @param longitudes
     * @param latitudes
     * @param elevations
     * @param slopes
     * constructs a panorama from its corresponding parameters
     */
    private Panorama(PanoramaParameters parameters, float[] distances,
            float[] longitudes, float[] latitudes, float[] elevations,
            float[] slopes) {

        this.parameters = parameters;
        this.distances = distances;
        this.longitudes = longitudes;
        this.latitudes = latitudes;
        this.elevations = elevations;
        this.slopes = slopes;
    }

    /**
     * @return the panorama paramaters
     */
    public PanoramaParameters parameters() {
        return parameters;
    }

    /**
     * @param x
     * @param y
     * checks if a point is not out of bounds
     * @throws IndexOutOfBoundsException
     */
    private void boundsCheck(int x, int y) {
        if (!parameters().isValidSampleIndex(x, y)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @param x
     * @param y
     * @return the value of the distance correspending to the coordinate point
     */
    public float distanceAt(int x, int y) {
        boundsCheck(x, y);
        return distances[parameters().linearSampleIndex(x, y)];
    }

    /**
     * @param x
     * @param y
     * @param d
     * @return the value of the distance correspending to the coordinate point,
     *  or the default value d if it is out of bounds
     */
    public float distanceAt(int x, int y, float d) {
        if (!parameters().isValidSampleIndex(x, y)) {
            return d;
        } else {
            return distances[parameters().linearSampleIndex(x, y)];
        }
    }

    /**
     * @param x
     * @param y
     * @return the value of the longitude correspending to the coordinate point
     */
    public float longitudeAt(int x, int y) {
        boundsCheck(x, y);
        return longitudes[parameters().linearSampleIndex(x, y)];
    }

    /**
     * @param x
     * @param y
     * @return the value of the latitude correspending to the coordinate point
     */
    public float latitudeAt(int x, int y) {
        boundsCheck(x, y);
        return latitudes[parameters().linearSampleIndex(x, y)];
    }

    /**
     * @param x
     * @param y
     * @return the value of the elevation correspending to the coordinate point
     */
    public float elevationAt(int x, int y) {
        boundsCheck(x, y);
        return elevations[parameters().linearSampleIndex(x, y)];
    }

    /**
     * @param x
     * @param y
     * @return the value of the slope correspending to the coordinate point
     */
    public float slopeAt(int x, int y) {
        boundsCheck(x, y);
        return slopes[parameters().linearSampleIndex(x, y)];
    }

    public final static class Builder {
        private 
        PanoramaParameters parameters;
       
        private  float[] distances, longitudes, latitudes, elevations,
                slopes;
        private boolean check = false;

        /**
         * @param parameters
         * constructs a builder for the panorama, initially with default values
         */
        public Builder(PanoramaParameters parameters) {
            Objects.requireNonNull(parameters);
           
            int nbSamples = parameters.height() * parameters.width();

            this.distances = new float[nbSamples];
            Arrays.fill(distances, Float.POSITIVE_INFINITY);
            this.parameters = parameters;
            this.longitudes = new float[nbSamples];
            //Arrays.fill(longitudes, 0);
            this.latitudes = new float[nbSamples];
            //Arrays.fill(latitudes, 0);
            this.elevations = new float[nbSamples];
            //Arrays.fill(elevations, 0);
            this.slopes = new float[nbSamples];
           // Arrays.fill(slopes, 0);
            check = false;
        }

        /**
         * @param x
         * @param y
         * @param distance
         * @return the builder with the added distance value in a coordinate point
         */
        public Builder setDistanceAt(int x, int y, float distance) {

            preconditions(x, y);
            distances[parameters.linearSampleIndex(x, y)] = distance;
            return this;
        }

        /**
         * @param x
         * @param y
         * @param longitude
         * @return the builder with the added longitude value in a coordinate point
         */
        public Builder setLongitudeAt(int x, int y, float longitude) {
            preconditions(x, y);
            longitudes[parameters.linearSampleIndex(x, y)] = longitude;
            return this;
        }

        /**
         * @param x
         * @param y
         * @param latitude
         * @return the builder with the added latitude value in a coordinate point
         */
        public Builder setLatitudeAt(int x, int y, float latitude) {
            preconditions(x, y);
            latitudes[parameters.linearSampleIndex(x, y)] = latitude;
            return this;
        }

        /**
         * @param x
         * @param y
         * @param elevation
         * @return the builder with the added elevation value in a coordinate point
         */
        public Builder setElevationAt(int x, int y, float elevation) {
            preconditions(x, y);
            elevations[parameters.linearSampleIndex(x, y)] = elevation;
            return this;
        }

        /**
         * @param x
         * @param y
         * @param slope
         * @return the builder with the added slope value in a coordinate point
         */
        public Builder setSlopeAt(int x, int y, float slope) {
            preconditions(x, y);
            slopes[parameters.linearSampleIndex(x, y)] = slope;
            return this;
        }

        /**
         * @return a panorama with the stored arguments
         */
        public Panorama build() {
            if (check) {
                throw new IllegalStateException();
            }
            check = true;
            return new Panorama(this.parameters, this.distances,
                    this.longitudes, this.latitudes, this.elevations,
                    this.slopes);
            
        }

        /**
         * @param x
         * @param y
         * checks if sample index is valid and if the build has not been done yet
         * @throws IllegalStateException 
         * @throws IndexOutOfBoundsException
         */
        private void preconditions(int x, int y) {

            if (check) {
                throw new IllegalStateException();
            }
            if (!parameters.isValidSampleIndex(x, y))
                throw new IndexOutOfBoundsException();
        }
    }

}
