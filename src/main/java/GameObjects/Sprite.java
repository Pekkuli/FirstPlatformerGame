package GameObjects;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import static GameObjects.Game.*;

public class Sprite {

    Image img;

    double positionX;
    double positionY;

    double width;
    double height;

    Sprite(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setImage(Image i) {
        img = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    void setImage(String filename) {
        img = new Image(filename);
        width = img.getWidth();
        height = img.getHeight();
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    void setPositionX(double newx) {
        positionX = newx;
    }

    void setPositionY(double newy) {
        positionY = newy;
    }

    void render() {
        getGC().drawImage(img,positionX,positionY);
    }

    Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,width,height);
    }
}
