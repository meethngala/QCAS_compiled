/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
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
public class StudentActionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button quizButton;
    
    @FXML
    private Button teacherLoginButton;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void goToTestOptions(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentTestDashboard.fxml"));
        Stage stage = (Stage) quizButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
}
