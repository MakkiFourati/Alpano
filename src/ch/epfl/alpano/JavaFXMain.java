package ch.epfl.alpano;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.Labelizer;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import ch.epfl.alpano.summit.GazetteerParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class JavaFXMain extends Application {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS = PredefinedPanoramas.NIESEN
            .panoramaDisplayParameters();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try (DiscreteElevationModel dDEM =

                new HgtDiscreteElevationModel(HGT_FILE)) {
            ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);

            Labelizer labelizer = new Labelizer(cDEM,
                    GazetteerParser.readSummitsFrom(new File("alps.txt")));
            labelizer.labels(PARAMS).toString();
            System.out.println(labelizer.labels(PARAMS));

            Platform.exit();

        }

    }
}
