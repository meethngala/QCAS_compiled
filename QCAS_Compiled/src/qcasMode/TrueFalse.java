
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
public class TrueFalse extends Question {
    
    public TrueFalse(String abbreviation, String difficulty,  String description, HashMap answerChoices, Integer answer, int questionNumber) {
        super(abbreviation, difficulty, description, answerChoices, answer,questionNumber);
    }
    
    boolean checkValidity(ArrayList<String> ans) {
        boolean check = false;
        if(ans.get(0).toLowerCase().equals(answerChoices.keySet().toArray()[0].toString().toLowerCase())){
        check = true;
        }
        return check;
    }
    
}