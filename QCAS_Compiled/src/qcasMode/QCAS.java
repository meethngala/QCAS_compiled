/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author paridhichoudhary
 */
public class QCAS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        Quiz quiz = new Quiz("Quiz_To_Test.txt","M",4, "jdbc:derby:QuizDB;create=true", "scott", "tiger", .25, .25,.25, .25); //Provide the details to conduct quiz
        //quiz.conductQuiz(); //Conduct the quiz

    }
    
}
