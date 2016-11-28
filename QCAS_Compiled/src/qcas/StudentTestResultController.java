/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import qcasMode.Quiz;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentTestResultController implements Initializable {
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button button;
    @FXML
    private BarChart<Integer, Integer> performanceBarChart;
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series correct = new XYChart.Series<>();
    private XYChart.Series incorrect = new XYChart.Series<>();
    private Quiz quiz;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void getChart(){
        performanceBarChart.setVisible(true);
        
        xAxis.setLabel("Accuracy");
        yAxis.setLabel("Count");
        try{
        databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
        Connection con = dBconn.getConnection();
        Statement stmt = con.createStatement();
        
        String validityCheck = "SELECT VALIDITY,count(*) As Count from QCASRohan.QUIZ_QUESTION WHERE QUIZID = " + quiz.getQuizNumber() +" group by VALIDITY;";
        ResultSet rs = stmt.executeQuery(validityCheck);
        ArrayList<Integer> validityChecks = new ArrayList();
        while(rs.next()){
            validityChecks.add(rs.getInt("Count"));
        }
        
        correct.setName("Correct");
        correct.getData().add(new XYChart.Data<>("incorrect",validityChecks.get(0)));
        
        
        incorrect.setName("Incorrect");
        incorrect.getData().add(new XYChart.Data<>("incorrect",validityChecks.get(1)));
        performanceBarChart.getData().addAll(correct, incorrect);
        
        
        // TODO
        } catch(SQLException e){
            System.out.println("SQL Exception: "+ e);
        }
    }
    @FXML
    private void handleButtonAction1(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void setQuiz(Quiz quiz){
        this.quiz = quiz;
    }
}
