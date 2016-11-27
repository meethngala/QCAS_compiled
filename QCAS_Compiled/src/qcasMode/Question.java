
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class that creates a question object when passed with the question number,
 * description, answer choices and answer
 *
 * @author paridhichoudhary
 */
public class Question {

    public String abbreviation;
    public String difficulty;
    public int questionNumber;
    public String description;
    public HashMap answerChoices = new LinkedHashMap();
    public ArrayList<Integer> answer = new ArrayList();

    /**
     *
     * @param questionNumber: Number of the question
     * @param description: Question in detail
     * @param answerChoices: Answer options
     * @param answer: Answer for the question
     */
    public Question(String abbreviation, String difficulty, String description, HashMap answerChoices, Integer answer, int questionNumber) {
        setAbbreviation(abbreviation);
        setDifficulty(difficulty);
        setDescription(description);
        setAnswerChoices(answerChoices);
        setAnswer(answer);
        setQuestionNumber(questionNumber);
    }

    /**
     * Gets the question number of the question
     *
     * @return
     */
    public int getQuestionNumber() {
        return questionNumber;
    }

    /**
     * Sets the question number of the question
     *
     * @param questionNumber
     */
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * Gets the description of the question
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the question
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the Answer Choices for the question
     *
     * @return
     */
    public HashMap getAnswerChoices() {
        return answerChoices;
    }

    /**
     * Sets the Answer Choices for the question
     *
     * @param answerChoices
     */
    public void setAnswerChoices(HashMap answerChoices) {
        this.answerChoices = answerChoices;
    }

    /**
     * Gets the answer of the question
     *
     * @return
     */
    public ArrayList<Integer> getAnswer() {
        return answer;
    }

    /**
     * Sets the answer of the question
     *
     * @param answer
     */
    public void setAnswer(Integer answer) {
        this.answer.add(answer);
    }

    private void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    private void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    boolean checkValidity(ArrayList<String> ans) {

        int question_type = 0;
        if (abbreviation.equals("MA")) {
            question_type = 1;
        } else if (abbreviation.equals("MC")) {
            question_type = 2;
        } else if (abbreviation.equals("TF")) {
            question_type = 3;
        } else if (abbreviation.equals("FIB")) {
            question_type = 4;
        }
        boolean check = true;

        switch (question_type) {
            case 1:

                for (int i = 0; i < ans.size(); i++) {
                    System.out.println(ans.size());
                    System.out.println(i + "  " + answerChoices.size());
                    if (answerChoices.get(ans.get(i)).equals("incorrect")) {
                        check = false;
                    }
                }
                break;
            case 2:

                for (int i = 0; i < ans.size(); i++) {

                    System.out.println(ans.size());
                    System.out.println(i + "  " + answerChoices.size());
                    if (answerChoices.get(ans.get(i)).equals("incorrect")) {
                        check = false;
                    }
                }
            case 3:

                System.out.println(ans.size());

                System.out.println(answerChoices.size());
                ans.get(0).equals(answerChoices.keySet().toArray()[0].toString());
                check = false;

                break;
            case 4:

                System.out.println(ans.size());
                ans.get(0).equals(answerChoices.keySet().toArray()[0].toString());
                check = false;
                break;
        }

        return check;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setAnswer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

}