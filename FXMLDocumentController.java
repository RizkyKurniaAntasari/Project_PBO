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

    }    
    
}
