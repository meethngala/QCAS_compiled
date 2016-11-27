/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Creates an list of question objects, establishes connections and conducts
 * quiz for the user
 *
 * @author paridhichoudhary
 */
public class Quiz {

    public ArrayList<Question> questions = new ArrayList();
    String grade = "Fail"; //Grade is the percentage of correct and incorrect answers 
    double score = 0.0;
    Result result = new Result();
    int numberOfQuestions;
    String difficultyLevel;
    databaseConnection connection;
    Statement stmt;
    PreparedStatement preparedStmt;
    int quizNumber;

    /**
     *
     * @param filepath : path of the file to read questions from
     * @param url : URL for database connection
     * @param username : username for the database
     * @param password : password for the database
     * @throws IOException
     * @throws SQLException
     */
    public Quiz(String Filepath, String difficultyLevel, int numberOfQuestions, String url, String username, String password, Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions) throws IOException, SQLException {
        connection = new databaseConnection(url, username, password);
        stmt = connection.con.createStatement();
        setDifficultyLevel(difficultyLevel);
        setNumberOfQuestions(numberOfQuestions);
        setQuestions(Filepath, percentMAQuestions, percentMCQuestions, percentFIBQuestions, percentTFQuestions);
        //conductQuiz();
    }

    /**
     * Returns list of questions for this quiz
     *
     * @return : list of questions for this quiz
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * sets questions into the array list of questions using database table
     *
     * @param filepath : text file containing questions
     * @throws IOException
     * @throws SQLException
     */
    public void setQuestions(String Filepath, Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions) throws IOException, SQLException {
        try {
            InputFileReader reader = new InputFileReader(); //reader object to read from the input file
            reader.readFile(Filepath); //reading the file containing quiz questions
            List<String[]> questionDetails = reader.wordsArray; // Each string from the csv file is stored in an ArrayList
            ArrayList<String> insertQueries = new ArrayList();
            int mastercounter = 0;
            // Dropping the table if already present
//            String createQueryTest = "CREATE TABLE QCASRohan.TEST"
//                + "("
//                + "Abbreviation VARCHAR(30000)" + ")";
//            stmt.execute(createQueryTest);
            // Inserting question details from the csv file words array into the insert String to be run to store values in a database
            for (int j = 0; j < questionDetails.size(); j++) {
                String countQuery = "select count(*) as count from QCASRohan.QUESTION";
                preparedStmt = connection.con.prepareStatement(countQuery);
                ResultSet rsc = preparedStmt.executeQuery();

                while (rsc.next()) {
                    mastercounter = rsc.getInt("count");
                }
                String insertString = "INSERT INTO QCASRohan.QUESTION VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                preparedStmt = connection.con.prepareStatement(insertString);
//                for (int j = 0; j < questionDetails.get(i).length; j++) {
//                    preparedStmt.setString(j+1, questionDetails.get(i)[j]);
//                }
//                preparedStmt
                for (int i = 0; i < questionDetails.get(j).length; i++) {
                    preparedStmt.setString(i + 1, questionDetails.get(j)[i]);
                }
                for (int k = questionDetails.get(j).length; k < 11; k++) {
                    preparedStmt.setString(k + 1, "");
                }
                mastercounter += 1;
                preparedStmt.setString(12, "" + mastercounter);
//                int counting = preparedStmt.executeUpdate();

//                preparedStmt.executeUpdate();
            }

            int numberMAQuestions = (int) (percentMAQuestions * numberOfQuestions);
            int numberMCQuestions = (int) (percentMCQuestions * numberOfQuestions);
            int numberFIBQuestions = (int) (percentFIBQuestions * numberOfQuestions);
            int numberTFQuestions = numberOfQuestions - numberMAQuestions - numberMCQuestions - numberFIBQuestions;

            int questionCounter = 0;

            //Setting MA Questions
            String query = "SELECT * FROM QCASRohan.QUESTION WHERE DIFFICULTYLEVEL = ?"
                    + " AND QUESTIONTYPE = ?"
                    + " ORDER BY RAND()";
            Integer answer = 0;
            preparedStmt = connection.con.prepareStatement(query); //Using prepared statement to make connection 
            preparedStmt.setString(1, difficultyLevel);
            preparedStmt.setString(2, "MA");
            ResultSet rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberMAQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)).equals("correct")) {
                        answer = (i + 1);
                    }
                }
                Question question = new MultipleAnswer(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "MC");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberMCQuestions) {
                HashMap<String, String> answerChoices = new HashMap<String, String>(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new MultipleChoice(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "TF");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberTFQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new TrueFalse(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "FIB");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberFIBQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    System.out.println(rs.getString("ANSWER" + (i + 1)));
                    System.out.println(rs.getString("VALIDITY" + (i + 1)));
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new FillInTheBlank(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
        } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }
    }

    /**
     * Method that provides the user quiz questions and and takes their answer;
     * also counts the number of right and wrong answers
     */
    public void conductQuiz() {

        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        int count = 0;
//        int rightCount = 0;

        while (count < numberOfQuestions) {
            int pickIndex = rand.nextInt(this.questions.size()); // random question picked from the questions set
            Question question = questions.get(pickIndex);
            questions.remove(pickIndex);
            int question_type = 0;

            if (question.abbreviation.equals("MA")) {
                question_type = 1;
            } else if (question.abbreviation.equals("MC")) {
                question_type = 2;
            } else if (question.abbreviation.equals("TF")) {
                question_type = 3;
            } else if (question.abbreviation.equals("FIB")) {
                question_type = 4;
            }
            System.out.println("Question:" + question.description + "?"); //Printing the question to the user
            Object[] answerChoicesKey = question.answerChoices.keySet().toArray();
            ArrayList<String> userAnswer = new ArrayList();

            switch (question_type) {
                case 1:

                    System.out.println("Following are the choices for answers: ");
                    System.out.println("a. " + answerChoicesKey[0] + "\n" + "b." + answerChoicesKey[1] + "\n" + "c." + answerChoicesKey[2] + "\n" + "d." + answerChoicesKey[3]);
                    userAnswer.add(input.nextLine());
                    userAnswer.add(input.nextLine());
                    break;
                case 2:
                    System.out.println("Following are the choices for answers: ");
                    System.out.println("a. " + answerChoicesKey[0] + "\n" + "b." + answerChoicesKey[1] + "\n" + "c." + answerChoicesKey[2] + "\n" + "d." + answerChoicesKey[3]);
                    userAnswer.add(input.nextLine());
                    break;
                case 3:
                    System.out.println("a. True");
                    System.out.println("b. False");
                    userAnswer.add(input.nextLine());
                    break;
                case 4:
                    userAnswer.add(input.nextLine());
                    break;
            }

            result.answers.put(question, userAnswer);
            count += 1;
        }
        List<Question> questionObjects = new ArrayList<>();
        ArrayList<ArrayList<String>> value = new ArrayList();
        for (Map.Entry<Question, ArrayList<String>> entry : result.answers.entrySet()) {

            questionObjects.add(entry.getKey());
//                String key = entry.getKey();
            value.add(entry.getValue());
        }

//        System.out.println(questionObjects.size());
//        System.out.println(questionObjects.get(0).questionNumber);
//        System.out.println(questionObjects.get(1).questionNumber);
//        System.out.println(questionObjects.get(2).questionNumber);
//        System.out.println(questionObjects.get(3).questionNumber);
//
//        System.out.println(value.get(0));
//        System.out.println(value.get(1));
//        System.out.println(value.get(2));
//        System.out.println(value.get(3));
        insertResults(questionObjects, value);

    }

    public void insertResults(List<Question> questionObjects, ArrayList<ArrayList<String>> value) {
        try {
            System.out.println("\n\n\n");
            connection = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest");
            stmt = connection.con.createStatement();
            String insertQuizQuestionQuery = "";
            int rightCount = 0;

            for (int i = 0; i < result.answers.size(); i++) {
                if (questionObjects.get(i).checkValidity(value.get(i))) {
                    rightCount += 1;
                    insertQuizQuestionQuery = "INSERT INTO QCASRohan.QUIZ_QUESTION VALUES(" + "1" + "," + questionObjects.get(i).questionNumber + "," + "'correct');";
                    stmt.executeUpdate(insertQuizQuestionQuery);
                } else {
                    insertQuizQuestionQuery = "INSERT INTO QCASRohan.QUIZ_QUESTION VALUES(" + "1" + "," + questionObjects.get(i).questionNumber + "," + "'incorrect');";
                    stmt.executeUpdate(insertQuizQuestionQuery);
                }
            }

//          
            stmt.execute(insertQuizQuestionQuery);
            score = rightCount / numberOfQuestions;
            if (score > 0.4) {
                grade = "Pass";
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));

            String resultQuery = "INSERT INTO QCASRohan.RESULTS VALUES(" + "'" + dateFormat.format(date) + "'," + "1" + "," + "'pariD'" + "," + score + ",'" + grade + "');";
            stmt.execute(resultQuery);
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Mismatch Exception: Please enter integers 1, 2 or 3");
        } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }

    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
    
    public Result getResult() {
        return this.result;
    }

}
