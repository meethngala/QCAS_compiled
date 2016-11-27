
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author paridhichoudhary
 */
public class MultipleAnswer extends Question {

    public MultipleAnswer(String abbreviation, String difficulty, String description, HashMap answerChoices, Integer answer, int questionNumber) {
        super(abbreviation, difficulty, description, answerChoices, answer, questionNumber);
    }

    public boolean checkValidity(ArrayList<String> ans) {
        boolean correct = true;
        for (int j = 0; j < ans.size(); j++) {
//            if (!(answerChoices.get(ans.get(j) - 1).equals("correct"))) {
//                correct = false;
//            }
        }
        return correct;
    }
}