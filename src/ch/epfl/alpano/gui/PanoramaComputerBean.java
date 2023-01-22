
package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;


import java.util.List;
import java.util.Objects;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
/**
 * Panorama Computer Bean
 * 
 * @author Haitham Hammami (257479)
 * @author Makki Fourati (247746)
 */
public final  class PanoramaComputerBean {
    private ContinuousElevationModel cem;
    private Labelizer labelizer;
    private ObjectProperty<Panorama> panorama = new SimpleObjectProperty<>();
    private ObjectProperty<PanoramaUserParameters> userParameters = new SimpleObjectProperty<>();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private ObservableList<Node> labels=FXCollections.observableArrayList();
    private ObjectProperty<ObservableList<Node>> nodes = new SimpleObjectProperty<>(
            (FXCollections.unmodifiableObservableList(labels)));
    

    /**
     * @param cem
     * @param summits
     * constructs a panorama computer bean from an elevation model and a labelizer out of a list of summits
     * throw NullPointerException
     */
    public PanoramaComputerBean(ContinuousElevationModel cem,
            List<Summit> summits) {
        this.cem = Objects.requireNonNull(cem);
        labelizer = new Labelizer(cem,  Objects.requireNonNull(summits));
        userParameters.addListener((prop, oldV, newV) -> synchronizeParameters(newV));

    }

    /**
     * @return the parameters Proprety
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty() {
        return userParameters;

    }

    /**
     * @return the parameters value
     */
    public PanoramaUserParameters getParameters() {
        return userParameters.get();

    }

    /**
     * @param newParameters
     * sets the parameters from the given argument
     */
    public void setParameters(PanoramaUserParameters newParameters) {
        userParameters.set(newParameters);
        
    }

    /**
     * @return the panorama Proprety
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
        return panorama;

    }

    /**
     * @return the Panorama
     */
    public Panorama getPanorama() {
        return panorama.get();
    }

    /**
     * @return the image Proprety
     */
    public ReadOnlyObjectProperty<Image> imageProperty() {
        return image;

    }

    /**
     * @return the Image
     */
    public Image getImage() {
        return image.get();

    }

    /**
     * @return the labels Proprety
     */
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty() {
        return nodes;

    }

    /**
     * @return the labels
     */
    public ObservableList<Node> getLabels() {
        return nodes.get();
    }

    /**
     * @param p
     * @return an Image Painter used in the graphic interface
     */
    private ImagePainter getImagePainter(Panorama p) {
        ChannelPainter distance = p::distanceAt;
        ChannelPainter slope = p::slopeAt;
        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
        ChannelPainter h = distance.div(100000f).cycle().mul(360f);
        ChannelPainter s = distance.div(200000f).clamped().inverted();
        ChannelPainter b = slope.mul(2f).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);

        return ImagePainter.hsb(h, s, b, opacity);
    }

    /**
     * @param p
     * synchronizes the computer bean so that it matches the parameters p
     */
    private void synchronizeParameters(PanoramaUserParameters p) {
        setParameters(p);
        panorama.set(new PanoramaComputer(cem)
                .computePanorama(getParameters().panoramaParameters()));
        image.set(PanoramaRenderer.renderPanorama(getPanorama(),
                getImagePainter(getPanorama())));
        labels.setAll(labelizer.labels(getParameters().panoramaDisplayParameters()));
        
    }
}
