package Game;

import GameObjects.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static GameObjects.Game.*;



public class FirstPlatformer extends Application {

    private static Game game;
    private static Label debugLabel;
    private long lastUpdate;
    private static int time;
    private static int updateRate = 60;

    @Override
    public void start(Stage primaryStage) {
//        pStage = primaryStage;

        lastUpdate = 0L;
        time = 0;

        final int dimX = 1200;
        final int dimY = 750;

        debugLabel = new Label("Time: 0\n( ͡° ͜ʖ ͡°)");

        game = new Game(dimX, dimY);

        game.createPlatform(200, getGameDimY()-250, 400);

        new AnimationTimer() {
            public void handle(long now) {

                long deltaTime = (now - lastUpdate)/1000000;

                if(deltaTime >= (1000/updateRate) ){

                    getGC().clearRect(0,0, getGameDimX(),getGameDimY());

                    game.moveGameObjects();
                    game.render();

                    debugLabel.setText(game.getDebugInfo());

                    lastUpdate = now;
                    time++;
                }
            }
        }.start();

        primaryStage.setScene(game.getGameScene());

        primaryStage.getIcons().add(new Image("/images/pedobear.jpg"));
        primaryStage.setTitle("Spudrospädre Xdddd");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static Label getTimeLabel(){
        return debugLabel;
    }

    public static int getTime() {
        return time;
    }

    public static int getUpdateRate() {
        return updateRate;
    }
}
