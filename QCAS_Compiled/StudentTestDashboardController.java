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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentTestDashboardController implements Initializable {
@FXML
private Button startTestButton;
@FXML
String difficultyLevel;
@FXML
int numberOfQuestions;
@FXML
    TestController t;
public StudentTestDashboardController() throws IOException, SQLException{
    
}
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void goToTest(MouseEvent event) throws IOException {
        FXMLLoader n = new FXMLLoader().load(getClass().getResource("Test.fxml"));
        TestController testController = (n.getController());
        testController.setDifficultyLevel(this.difficultyLevel);
        testController.setNumberOfQuestions(this.numberOfQuestions);
        //Parent root = FXMLLoader.load(getClass().getResource("Test.fxml"));
        Stage stage = (Stage) startTestButton.getScene().getWindow();
        Scene scene = new Scene(n.getRoot());
        stage.setScene(scene);
        stage.getOwner();
        stage.show();
    }
    
}
