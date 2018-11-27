package GameObjects;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

import static Game.FirstPlatformer.*;
import static GameObjects.platform.*;

public class Game {

    private static player player;
    private static ArrayList<platform> platforms;

    private static ArrayList<String> input;

    private static Scene gameScene;
    private static GraphicsContext graphicsContext;

    private static double dimX;
    private static double dimY;

    private static double gravity = 0.25;


    public Game(double x, double y) {
        player = new player(0,y/25+74);
        platforms = new ArrayList<>();
        input = new ArrayList<>();
        dimX = x;
        dimY = y;

        VBox root = new VBox();
        HBox gameBox = new HBox();

        Canvas canvas = new Canvas(dimX,dimY);
        gameBox.getChildren().add(canvas);
        gameBox.setStyle("-fx-background-color: #08ff0a");

        graphicsContext = canvas.getGraphicsContext2D();

        gameScene = new Scene(root);

        gameBox.setOnMouseClicked(event -> {
            System.out.println("game was clicked at: " + event.getX() + "," + event.getY());
            getPlayerCharacter().setVelocity(0, 0);
            getPlayerCharacter().setPosition(event.getX()- getPlayerCharacter().getWidth()/2, event.getY()- getPlayerCharacter().getHeight()/2);
            player.setOnGround(false);
            changeLevel();
            graphicsContext.clearRect(0,0,dimX,dimY);
            player.render();
//                shoot at x y
        });

        gameScene.setOnKeyPressed(event -> {
            String keyInput = event.getCode().toString();
            System.out.println(keyInput + " was pressed!");
            if (!getInput().contains(keyInput)){
                getInput().add(keyInput);
            }
        });

        gameScene.setOnKeyReleased(event -> {
            String keyInput = event.getCode().toString();
            getInput().remove(keyInput);
        });

        MediaPlayer music = new MediaPlayer(new Media(getClass().getResource("/sounds/steam_monster_summer_game.mp3").toString()));
        music.setOnEndOfMedia(() -> music.seek(Duration.ZERO));
        music.setVolume(0.1);
//        music.play();

        VBox musicBox = new VBox();
        Label musicLabel = new Label("Music volume");

        Slider musicVolumeSlider = new Slider(0,0.5,0.1);
        musicVolumeSlider.setMaxWidth(200);
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            music.setVolume((Double) newValue);
        });

        musicBox.getChildren().addAll(musicLabel,musicVolumeSlider);
        root.getChildren().addAll(musicBox,getTimeLabel(), gameBox);
    }

    public Scene getGameScene(){
        return gameScene;
    }

    public void moveGameObjects() {
        moveCharacter();
    }

    private void moveCharacter(){
        player.setVelocityX(0);
        if(input.contains("SPACE")){
            player.jump();
        }
        if(input.contains("A")){
            player.addVelocity(-1,0);
        }
        if(input.contains("D")){
            player.addVelocity(1,0);
        }
        changeLevel();
        player.update(1000/ getUpdateRate());
    }

    private void changeLevel() {

        if(player.getPositionX() + player.getWidth() > dimX - 20) {
            System.out.println("Change level!");

            createNewLevel(player.getPositionY());

            player.setPosition(0,player.getPositionY()-player.getHeight()-80);
            player.setVelocity(0 ,0);

//            create new platforms
        }
    }

    public void createNewLevel(double startheight){
        platforms.clear();

        Random rngLength = new Random();

        double randLength = 200 + (250 - 200)*rngLength.nextDouble();
        double platformlength = randLength;

//        createPlatform(0,startheight,randLength);
//
//
//
//
//        while (true) {
//
//            randLength = 200 + (250 - 200)*rngLength.nextDouble();
//            platformlength+=randLength;
//            double height = -100 + (125 - -125)*rngLength.nextDouble();
//            double distance = -100 + (125 - -125)*rngLength.nextDouble();
//
//            platform last = platforms.get(platforms.size()-1);
//
//            createPlatform(last.positionX+distance,last.getPositionY()+height, randLength);
//
//
//            break;
//        }
//
//        randLength = 200 + (250 - 200)*rngLength.nextDouble();
//        double randHeight = 100 + (200 - 100)*rngLength.nextDouble();
//        platformlength += randLength;
//
//        createPlatform(getGameDimX()-randLength,randHeight,randLength);


        System.out.println("legth:" +randLength);


    }



    public void render(){
        player.render();

        for(platform pt: platforms){
            pt.render();
        }
    }

    static double getGravity(){
        return gravity;
    }

    private player getPlayerCharacter() {
        return player;
    }

    public void createPlatform(double x, double y, double length){
        platform pt = new platform(x,y,length);
        pt.setPlatformImage(length);
        platforms.add(pt);
    }

    public static boolean playerCollidesAfterX(double newx){
        for(platform pt: platforms){
            if(player.intersectsAfterXMovement(pt,newx)){

                if ((!(player.getPositionX() > pt.getPositionX()) || !(player.getPositionX() < pt.getPositionX() + pt.getWidth())) &&
                    player.getPositionY() < (pt.getPositionY()+pt.getHeight()) ) {
                    double deltax = pt.getPositionX() - player.positionX;

                    if(deltax > 0) {
                        player.setPositionX(pt.getPositionX()-player.getWidth()-2);
                    } else {
                        player.setPositionX(pt.getPositionX()+pt.getWidth()+2);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean playerCollidesAfterY(double newy){
        for(platform pt: platforms){
            if(player.intersectsAfterYMovement(pt,newy)){

                double deltaY = pt.getPositionY() - player.positionY;

                if(deltaY > 0) {
                    player.setStatus("on platform");
                    player.setPositionY(pt.getPositionY()-player.getHeight()+getGrassPixelsHeight());
                    player.setOnGround(true);
                    player.setVelocityY(0);
                } else {
                    player.setPositionY(pt.getPositionY()+pt.getHeight()+2);
                    player.setOnGround(false);
                    player.setVelocityY(0);
                }
                return true;
            }
        }
        return false;
    }

    private static ArrayList<String> getInput() {
        return input;
    }

    public static double getGameDimX() {
        return dimX;
    }

    public static double getGameDimY() {
        return dimY;
    }

    public String getDebugInfo(){
        return "Time: " + getTime()/getUpdateRate()+", Position: " + (short) player.getPositionX()+" ("+(short)(player.getPositionX()
                + player.getWidth())+"), "+(short) player.getPositionY()+" ("+(short)(player.getPositionY()+ player.getHeight())+")"+" onGround: "+player.getOnGround()+" Status: "+player.getStatus()
                +"\nwidth: "+ player.getWidth()+", height: "+ player.getHeight()
                +", Velocity: "+ player.getVelocityX()+","+ player.getVelocityY();
    }

    public static GraphicsContext getGC(){
        return graphicsContext;
    }
}
