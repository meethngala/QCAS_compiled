/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import qcasMode.*;
/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class TestController implements Initializable {
    @FXML
    private Button reset;
    @FXML
    Quiz quiz;
    @FXML
    private RadioButton testRB1;
    @FXML
    private RadioButton testRB2;
    
    @FXML
    private TextArea testTextArea;
    @FXML
    private ToggleGroup testTG;
    private String difficultyLevel;
    private int numberOfQuestions;

    public TestController(int numberOfQuestions, String difficultyLevel) throws IOException, SQLException {
        this.quiz = new Quiz("Quiz_To_Test.txt","M",4, "jdbc:derby:QuizDB;create=true", "scott", "tiger", .25, .25,.25, .25);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        testTextArea.setText(quiz.questions.get(0).description);
        testRB1.setText(quiz.getQuestions().get(0).difficulty);
        testRB1.setToggleGroup(testTG);
        testRB2.setToggleGroup(testTG);
        
        
    }    
    
    @FXML
    public void setDifficultyLevel(String difficultyLevelString){
        this.difficultyLevel = difficultyLevelString;
        
    }
    
    @FXML
    public void setNumberOfQuestions(int numberOfQuestions){
        this.numberOfQuestions = numberOfQuestions;
        
    }
    
    @FXML
    public void handleResetAction(MouseEvent event) throws IOException, SQLException {
        //Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Quiz quiz = new Quiz("Quiz_To_Test.txt","M",4, "jdbc:derby:QuizDB;create=true", "scott", "tiger", .25, .25,.25, .25);
        System.out.println(quiz.questions.get(1).description);
        testTextArea.setText(quiz.questions.get(1).description);
        //Stage stage = (Stage) reset.getScene().getWindow();
        //Scene scene = new Scene(root);
        //stage.setScene(scene);
        //stage.show();
    }
    
}
