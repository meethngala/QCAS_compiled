/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author paridhichoudhary
 */
public class Result {
    int score;
    HashMap checkAnswers = new HashMap();
    LinkedHashMap<Question, ArrayList<String>> answers = new LinkedHashMap<Question, ArrayList<String>>();

    public LinkedHashMap<Question, ArrayList<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(LinkedHashMap<Question, ArrayList<String>> answers) {
        this.answers = answers;
    }
    
    
}

