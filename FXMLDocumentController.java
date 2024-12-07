package project_akhir;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.control.Label;
/**
 *
 * @author Rizky Kurnia Antasari
 */
public class FXMLDocumentController implements Initializable {
    
     @FXML
    private TextField idField, nameField;


    @FXML
    private Canvas gameCanvas;


    @FXML
    private Label statusLabel;
           
    // Global Variable    
    private static final List<Corner> snake = new ArrayList<>();
   
    private static int score, speed = 5;
    private static Dir direction = Dir.left;
   
    private static final Random rand = new Random();
   
    private boolean gameStarted = false;
    private static boolean gameOver = false;

    // Deklarasi Object
    USER access = new AccessUser(); // Penerapan Polymorphisme
    FOOD food = new FOOD(); // akses food
   
    // Kebutuhan Stage / Arena
    public static class Stage{
        private static final int CORNERSIZE = 25;
        private static final int WIDTH = 600/CORNERSIZE;
        private static final int HEIGHT = 400/CORNERSIZE; // 24,16,25    


        public static int getCornersize() {
            return CORNERSIZE;
        }


        public static int getWidth() {
            return WIDTH;
        }


        public static int getHeight() {
            return HEIGHT;
        }
    }
   
    public void initialize() {
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::handleKeyPress);
    }

        // Kebutuhan User
    public abstract class USER{
        protected int userId;
        protected String userName;
        public abstract void setUserId(int userId);
        public abstract void setUserName(String userName);
        public abstract int getUserId();
        public abstract String getUserName();
        public abstract void currentPlay(boolean gameOver);
    }
    
    public class AccessUser extends USER{
        @Override
        public void currentPlay(boolean gameOver){
            statusLabel.setText("User " + access.getUserName() + " dengan ID " + access.getUserId() + (gameOver ? " SELESAI bermain " : " SEDANG bermain." ));
        }
        @Override
        public void setUserId(int userId){
            this.userId = userId;
        }
        @Override
        public void setUserName(String userName){
            this.userName = userName;
        }
        @Override
        public int getUserId(){
            return userId;
        }
        @Override
        public String getUserName(){
            return userName;
        }
    }
    public class FOOD{
        private int foodX, foodY;
       
        public FOOD(){}
       
        public int getFoodX() {
            return foodX;
        }

        public void setFoodX(int foodX) {
            this.foodX = foodX;
        }

        public int getFoodY() {
            return foodY;
        }

        public void setFoodY(int foodY) {
            this.foodY = foodY;
        }
    }
     
    public enum Dir {
        left, right, up, down;
    }

    public static class Corner {
        int x, y;


        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public int getX() {
            return x;
        }


        public void setX(int x) {
            this.x = x;
        }


        public int getY() {
            return y;
        }


        public void setY(int y) {
            this.y = y;
        }
    }
      
    @FXML
    private void handleStartGame() {
        if (gameStarted) return;

        String idText = idField.getText();
        String name = nameField.getText();


        if (idText.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        try {
            //USER user = new AccessUser(); // kalo mau ganti user juga bisa, tapi ubah semua access
           
            access.setUserId(Integer.parseInt(idText));
            access.setUserName(name);
            gameStarted = true;
            gameCanvas.requestFocus(); // Fokus ke canvas sebelum memulai game

            access.currentPlay(gameOver);
           
            startGame();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID must be an integer.");
        }
    }

   // Game Logic
    private void startGame() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gameCanvas.requestFocus(); // Fokus ke canvas sebelum memulai game


        snake.add(new Corner(Stage.WIDTH / 2, Stage.HEIGHT / 2)); // Start snake at the center
        newFood();


        new AnimationTimer() {
            long lastTick = 0;


            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }


                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }
        }.start();
    }

    private void tick(GraphicsContext gc) {
        if (gameOver) {
            saveScore();
            access.currentPlay(gameOver); // Merubah status menjadi selesai bermain
            return;
        }


        Corner head = snake.get(0);
        Corner newHead;
        switch (direction) {
            case up:
                newHead = new Corner(head.x, head.y - 1);
                break;
            case down:
                newHead = new Corner(head.x, head.y + 1);
                break;
            case left:
                newHead = new Corner(head.x - 1, head.y);
                break;
            case right:
                newHead = new Corner(head.x + 1, head.y);
                break;
            default:
                throw new IllegalStateException("Unexpected direction: " + direction);
        }


        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= Stage.getWidth()|| newHead.y >= Stage.getHeight()) {
            gameOver = true;
        }


        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).x == newHead.x && snake.get(i).y == newHead.y) {
                gameOver = true;
            }
        }


        if (!gameOver) {
            snake.add(0, newHead);
            if (food.getFoodX() == newHead.x && food.getFoodY() == newHead.y) {
                score++;
                speed++;
                newFood();
            } else {
                snake.remove(snake.size() - 1);
            }
        }

        gc.setFill(Color.BLACK); // Background
        gc.fillRect(0, 0, Stage.getWidth() * Stage.getCornersize(), Stage.getHeight() * Stage.getCornersize()); // Bersihin canvas
        gc.setFill(Color.RED); // warna food
        gc.fillOval(food.getFoodX() * Stage.getCornersize(), food.getFoodY() * Stage.getCornersize(), Stage.getCornersize(), Stage.getCornersize()); // koordinat x ,y ,lebar, tinggi
        for (Corner c : snake) {
            gc.setFill(Color.LIME);
            gc.fillRect(c.x * Stage.getCornersize(), c.y * Stage.getCornersize(), Stage.getCornersize() - 1, Stage.getCornersize() - 1);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 20));
        gc.fillText("Score: " + score, 10, 20);
    }
    
    }       
}
