package GameObjects;



import javafx.geometry.Rectangle2D;

import static GameObjects.Game.*;

public class player extends Sprite{

    private double velocityX;
    private double velocityY;
    private boolean onGround;
    private String status;

    player(double x, double y) {
        super(x, y);
        setImage("/images/ES2.png");
        status = "in air";

        velocityX = 0;
        velocityY = 0;
        onGround = false;
    }

    void setStatus(String stat){
        status = stat;
    }

    void setOnGround(boolean ground){
        this.onGround = ground;
    }

    boolean getOnGround(){
        return onGround;
    }

    String getStatus(){
        return status;
    }

    double getVelocityX() {
        return velocityX;
    }

    double getVelocityY() {
        return velocityY;
    }

    void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    void setVelocityX(double x){
        velocityX = x;
    }

    void setVelocityY(double y) {
        velocityY = y;
    }

    void addVelocity(double x, double y){
        velocityX += x;
        velocityY += y;
    }

    void jump() {
        if(onGround) {
            System.out.println("JUMP!");
            addVelocity(0, -3.00);
            setOnGround(false);
        }
    }

    void update(double time) {

        addVelocity(0,getGravity());

        double deltaX = velocityX*time;
        double deltaY = velocityY*time;

        double newX = positionX +deltaX;
        double newY = positionY +deltaY;

//        dont need to check x if havent moved
        if(deltaX != 0){
//            if player is inside gamescreen
            if( (newX >=0) && (newX + width <= getGameDimX()) ){
//                if player hits platform (side)
                if(playerCollidesAfterX(newX)){
                    velocityX = 0;
//                    if player on on surface (ground/platform)
                } else {
                    positionX += deltaX;
                }
//                if player is outside of gamescreen (leftside)
            } else if (newX <=0){
                positionX =0;
//                if palyer is outside of gamescreen (rightside)
            } else {
                positionX = getGameDimX() - width;
            }
        }

//        if player is inside gamescreen
        if(newY + height <= getGameDimY()) {
//            if player drops onto platform
            if(playerCollidesAfterY(newY)){

                if(onGround && deltaY < 0) {
                    setStatus("on platform");
                    positionY += deltaY;
                }
//                if player is already on platform
            } else {

                if(deltaY < 0){
                    setStatus("jumping");
                } else {
                    setStatus("falling");
                }

                positionY += deltaY;
            }
//            if player is outside of gamescreen
        } else if (newY <=0){
            setStatus("in air");
            positionY =0;
//            if player is on ground
        } else {
            setStatus("on ground");
            positionY = getGameDimY() - height;
            velocityY = 0;
            onGround = true;
        }
    }

    private Rectangle2D getBoundaryAfterXMovement(double newx){
        return new javafx.geometry.Rectangle2D(newx,positionY,width,height);
    }

    private Rectangle2D getBoundaryAfterYMovement(double newy){
        return new javafx.geometry.Rectangle2D(positionX, newy,width,height);
    }

    boolean intersectsAfterXMovement(Sprite s, double newx) {
        return getBoundaryAfterXMovement(newx).intersects(s.getBoundary());
    }

    boolean intersectsAfterYMovement(Sprite s, double newy) {
        return getBoundaryAfterYMovement(newy).intersects(s.getBoundary());
    }

    public String toString() {
        return "Position: "+positionX +","+positionY +" , Velocity: "+velocityX +","+velocityY;
    }

//    boolean intersects(Sprite s) {
//        return s.getBoundary().intersects(getBoundary());
//    }
}
