package GameObjects;



import javafx.geometry.Rectangle2D;

import static GameObjects.Game.*;

public class player extends Sprite{

    private double velocityX;
    private double velocityY;
    private boolean onGround;
    private String status;

    public player(double x, double y) {
        super(x, y);
        setImage("/images/ES2.png");
        status = "in air";

        velocityX = 0;
        velocityY = 0;
        onGround = false;
    }

    public void setStatus(String stat){
        status = stat;
    }

    public void setOnGround(boolean ground){
        this.onGround = ground;
    }

    public boolean getOnGround(){
        return onGround;
    }

    public String getStatus(){
        return status;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocity(double velocityX, double velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setVelocityX(double x){
        velocityX = x;
    }

    public void setVelocityY(double y) {
        velocityY = y;
    }

    public void addVelocity(double x, double y){
        velocityX += x;
        velocityY += y;
    }

    public void jump() {
        if(onGround) {
            System.out.println("JUMP!");
            addVelocity(0, -3.00);
            setOnGround(false);
        }
    }

    public void update(double time) {

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
                    status = "on platform";
                    positionY += deltaY;
                }
//                if player is already on platform
            } else {

                if(deltaY < 0){
                    status = "jumping";
                } else {
                    status = "falling";
                }

                positionY += deltaY;
            }
//            if player is outside of gamescreen
        } else if (newY <=0){
            status = "in air";
            positionY =0;
//            if player is on ground
        } else {
            status = "on ground";
            positionY = getGameDimY() - height;
            velocityY = 0;
            onGround = true;
        }
    }

    Rectangle2D getBoundaryAfterXMovement(double newx){
        return new javafx.geometry.Rectangle2D( ((double) newx),positionY,width,height);
    }

    Rectangle2D getBoundaryAfterYMovement(double newy){
        return new javafx.geometry.Rectangle2D(positionX,( (double) newy),width,height);
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

    boolean intersects(Sprite s) {
        return s.getBoundary().intersects(getBoundary());
    }
}
