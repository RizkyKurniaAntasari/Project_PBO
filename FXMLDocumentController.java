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
   


    }       
}
