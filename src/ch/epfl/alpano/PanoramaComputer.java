
package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.PanoramaParameters;;
/**
 * Panorama computer: shapes out a panorama out of a DEM
 *  @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final class PanoramaComputer {
    private final ContinuousElevationModel dem;
    private static final double K = 0.13, COEF= ((1d - K) / (2d * Distance.EARTH_RADIUS));
    private static final int SPACING=64, EPSILON=4;
    /**
     * @param dem
     * constructs a panorama computer
     * @throws NullPointerException
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        Objects.requireNonNull(dem);
        this.dem = dem;
    }

    /**
     * @param parameters
     * @return a panorama correspending to the parameters
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        Builder b = new Builder(parameters);

        for (int i = 0; i < parameters.width(); i++) {
            ElevationProfile profile = new ElevationProfile(dem,
                    parameters.observerPosition(), parameters.azimuthForX(i),
                    parameters.maxDistance());
            double root = 0;
            
            for (int j = parameters.height() - 1; j >= 0; --j) {

                double slope = parameters.altitudeForY(j);
                DoubleUnaryOperator f = rayToGroundDistance(profile,
                        parameters.observerElevation(), Math.tan(slope));
                double bound = Math2.firstIntervalContainingRoot(f, root,
                        parameters.maxDistance(), SPACING);
                if (bound != Double.POSITIVE_INFINITY) {
                    root = Math2.improveRoot(f, bound, bound + SPACING, EPSILON);
                    GeoPoint point=profile.positionAt(root);
                    b.setDistanceAt(i, j, (float) (root/Math.cos(slope)));
                    b.setElevationAt(i, j, (float) dem.elevationAt(point));
                    b.setLatitudeAt(i, j, (float) point.latitude());
                    b.setLongitudeAt(i, j, (float) point.longitude());
                    b.setSlopeAt(i, j, (float) dem.slopeAt(point));
                }
                else break;
            }

        }

        return b.build();
    }

    /**
     * @param profile
     * @param ray0
     * @param raySlope
     * @return the distance between the observer and the ground according to his ray of view
     */
    public static DoubleUnaryOperator rayToGroundDistance(
            ElevationProfile profile, double ray0, double raySlope) {
        return (x) -> (ray0 + x * (raySlope)) - profile.elevationAt(x)
                + Math2.sq(x) * COEF ;
    }
}
