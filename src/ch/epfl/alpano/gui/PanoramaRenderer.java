
package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.*;

/**
 * Panoram Renderer
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public interface PanoramaRenderer {
    /**
     * @param panorama
     * @param painter
     * @return a rendered Image from the values of the panorama and the colors
     *         of the painter
     */
    static Image renderPanorama(Panorama panorama, ImagePainter painter) {
        int width = panorama.parameters().width();
        int height = panorama.parameters().height();
        WritableImage image = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.getPixelWriter().setColor(x, y, painter.colorAt(x, y));
            }
        }
        return image;
    }

}
