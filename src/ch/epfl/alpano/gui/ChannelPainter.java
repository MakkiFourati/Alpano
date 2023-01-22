
package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import static java.lang.Math.max;

import static java.lang.Math.min;
import java.util.function.DoubleUnaryOperator;

/**
 * Channel painter
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface ChannelPainter {

    /**
     * @param x
     * @param y
     * @return the value at the pixel located in (x,y)
     */
    float valueAt(int x, int y);

    /**
     * @param pano
     * @return the distance to the furthest neighbor of a pixel
     */
    static ChannelPainter maxDistanceToNeighbors(Panorama pano) {
        return (x, y) -> {
            float max1 = max(pano.distanceAt(x + 1, y, 0),
                    pano.distanceAt(x, y + 1, 0));
            float max2 = max(pano.distanceAt(x - 1, y, 0),
                    pano.distanceAt(x, y - 1, 0));
            return max(max1, max2) - pano.distanceAt(x, y);
        };

    }

    /**
     * @param f
     * @return the sum of the value f and the value at the pixel located in
     *         (x,y)
     */
    public default ChannelPainter add(float f) {
        return (x, y) -> this.valueAt(x, y) + f;

    }

    /**
     * @param f
     * @return the multiplication of the value f and the value at the pixel
     *         located in (x,y)
     */
    public default ChannelPainter mul(float f) {
        return (x, y) -> this.valueAt(x, y) * f;

    }

    /**
     * @param f
     * @return the difference between the value f and the value at the pixel
     *         located in (x,y)
     */
    public default ChannelPainter sub(float f) {
        return (x, y) -> this.valueAt(x, y) - f;

    }

    /**
     * @param f
     * @return the division of the value f and the value at the pixel located in
     *         (x,y)
     */
    public default ChannelPainter div(float f) {
        return (x, y) -> this.valueAt(x, y) / f;

    }

    /**
     * @param f
     * @return the mapping of the fucntion f taking the point (x,y) as intial
     *         value
     */
    public default ChannelPainter map(DoubleUnaryOperator f) {
        return (x, y) -> (float) f.applyAsDouble((double) valueAt(x, y));
    }

    /**
     * @return the clamping of the value of (x,y) between 1 and 0
     */
    public default ChannelPainter clamped() {
        return (x, y) -> max(0, min(this.valueAt(x, y), 1));
    }

    /**
     * @return the inversion relative to 1 of the value of (x,y)
     */
    public default ChannelPainter inverted() {
        return (x, y) -> 1 - this.valueAt(x, y);
    }

    /**
     * @return the cycled value relative to 1 of the value (x,y)
     */
    public default ChannelPainter cycle() {
        return (x, y) -> this.valueAt(x, y) % 1;
    }

}
