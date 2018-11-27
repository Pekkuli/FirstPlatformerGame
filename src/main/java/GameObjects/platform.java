package GameObjects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;


public class platform extends Sprite {

    private static double grassPixels = 6;

    public platform(double x, double y, double length) {
        super(x, y);
        setPlatformImage(length);
    }

    void setPlatformImage(double length) {
        img = new Image("/images/platform.png",length,75,false,false);
        width = length;
        height = img.getHeight();
    }

    public static double getGrassPixelsHeight(){
        return grassPixels;
    }

    @Override
    public Rectangle2D getBoundary(){
        return new Rectangle2D(getPositionX(), getPositionY() + grassPixels,img.getWidth(),img.getHeight());
    }

    public boolean intersects(Sprite s) {
        return s.getBoundary().intersects(this.getBoundary());
    }

    public String toString() {
        return "\nPosition: \n"+positionX +","+((double) positionX+width)+"\n"+
                positionY + ""+","+((double) positionY+height);
    }
}
