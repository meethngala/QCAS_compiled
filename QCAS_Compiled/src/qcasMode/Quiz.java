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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *Creates an list of question objects, establishes connections and conducts quiz for the user
 * @author paridhichoudhary
 */
public class Quiz {
    
    public ArrayList<Question> questions = new ArrayList();
    public String grade; //Grade is the percentage of correct and incorrect answers 
    public String passFail; //Pass or Fail result of the quiz
    public Result result = new Result();
    public int numberOfQuestions;
    public String difficultyLevel;
    public databaseConnection connection;
    public Statement stmt;
    public PreparedStatement preparedStmt;
    public int quizNumber;
    /**
     *
     * @param filepath : path of the file to read questions from
     * @param url : URL for database connection
     * @param username : username for the database
     * @param password : password for the database
     * @throws IOException
     * @throws SQLException
     */
    public Quiz(String Filepath, String difficultyLevel, int numberOfQuestions, String url, String username, String password,Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions) throws IOException, SQLException {
        int i =0;
        connection = new databaseConnection(url, username, password);
        stmt = connection.con.createStatement();
        setDifficultyLevel(difficultyLevel);
        setNumberOfQuestions(numberOfQuestions);
        setQuestions(Filepath, percentMAQuestions, percentMCQuestions, percentFIBQuestions, percentTFQuestions);

    }

    /**
     *Returns list of questions for this quiz
     * @return : list of questions for this quiz
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     *sets questions into the array list of questions using database table
     * @param filepath : text file containing questions
     * @throws IOException
     * @throws SQLException
     */
    public void setQuestions(String Filepath, Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions) throws IOException, SQLException {
        try {
            InputFileReader reader = new InputFileReader(); //reader object to read from the input file
            reader.readFile(Filepath); //reading the file containing quiz questions
            ArrayList<String[]> questionDetails = reader.wordsArray; // Each string from the csv file is stored in an ArrayList
            ArrayList<String> insertQueries = new ArrayList();

            // Dropping the table if already present
            String query = "DROP TABLE APP.QUIZ";
            stmt.execute(query);

            // Creating the table in the APP schema
            query = "CREATE TABLE APP.QUIZ"
                    + "("
                    + "ABBREVIATION VARCHAR(30000)" + ", "
                    + "DIFFICULTY VARCHAR(30000)" + ", "
                    + "DESCRIPTION VARCHAR(30000)" + ", "
                    + "ANSWER1 VARCHAR(30000)" + ", "
                    + "VALIDITY1 VARCHAR(30000)" + ", "
                    + "ANSWER2 VARCHAR(30000)" + ", "
                    + "VALIDITY2 VARCHAR(30000)" + ", "
                    + "ANSWER3 VARCHAR(30000)" + ", "
                    + "VALIDITY3 VARCHAR(30000)" + ", "
                    + "ANSWER4 VARCHAR(30000)" + ", "
                    + "VALIDITY4 VARCHAR(30000)"
                    + ")";
            stmt.execute(query);
            
            // Inserting question details from the csv file words array into the insert String to be run to store values in a database
            for (int i = 0; i < questionDetails.size(); i++) {
                String insertString = "INSERT INTO APP.QUIZ VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                preparedStmt = connection.con.prepareStatement(insertString);
                for (int j = 0; j < questionDetails.get(i).length; j++) {
                    preparedStmt.setString(j+1, questionDetails.get(i)[j]);
                }
                preparedStmt.executeUpdate();
            }

            // Using the Insert queries to put the question details into the database
//            for (int i = 0; i < insertQueries.size(); i++) {
//                query = insertQueries.get(i);
//                System.out.println(query);
//                stmt.executeUpdate(query); //Executes the insert queries
//            }

            
            
            int numberMAQuestions = (int) (percentMAQuestions * numberOfQuestions);
            int numberMCQuestions = (int) (percentMCQuestions * numberOfQuestions);
            int numberFIBQuestions = (int) (percentFIBQuestions * numberOfQuestions);
            int numberTFQuestions =  numberOfQuestions - numberMAQuestions - numberMCQuestions - numberFIBQuestions;
            
            int questionCounter =0;
            
            //Setting MA Questions
            query = "SELECT * FROM APP.QUIZ WHERE DIFFICULTY = ?"
                     + " AND ABBREVIATION = ?"
                     + " ORDER BY RANDOM()"; 
            String answer = "";
            preparedStmt = connection.con.prepareStatement(query); //Using prepared statement to make connection 
            preparedStmt.setString(1,difficultyLevel);
            preparedStmt.setString(2,"MA");
            ResultSet rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter<numberMAQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)),rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)).equals("correct")){ 
                       answer = "" + (i+1);
                    }
                }
                Question question = new MultipleAnswer(rs.getString("ABBREVIATION"),rs.getString("DIFFICULTY"),rs.getString("DESCRIPTION"), answerChoices,answer); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter +=1;
            }
            questionCounter =0;
            preparedStmt.setString(2,"MC");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter <numberMCQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)),rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1))=="correct"){ 
                       answer = "" + (i+1);
                    }
                }
                Question question = new MultipleChoice(rs.getString("ABBREVIATION"),rs.getString("DIFFICULTY"), rs.getString("DESCRIPTION"), answerChoices,answer); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter +=1;
            }
            questionCounter=0;
            preparedStmt.setString(2,"TF");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next()&& questionCounter<numberTFQuestions ) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)),rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1))=="correct"){ 
                       answer = "" + (i+1);
                    }
                }
                Question question = new TrueFalse(rs.getString("ABBREVIATION"),rs.getString("DIFFICULTY"), rs.getString("DESCRIPTION"), answerChoices,answer); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter +=1;
            }
            questionCounter =0;
            preparedStmt.setString(2,"FIB");
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter<numberFIBQuestions ) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)),rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1))=="correct"){ 
                       answer = "" + (i+1);
                    }
                }
                Question question = new FillInTheBlank(rs.getString("ABBREVIATION"),rs.getString("DIFFICULTY"), rs.getString("DESCRIPTION"), answerChoices,answer); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter +=1;
            }
        } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }
    }

    /**
     * Method that provides the user quiz questions and and takes their answer; also counts the number of right and wrong answers
     */
    public void conductQuiz() {

        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        int count=0;
        while (count<numberOfQuestions) {
            int pickIndex = rand.nextInt(this.questions.size()); // random question picked from the questions set
            Question question = questions.get(pickIndex);
            questions.remove(pickIndex);
            System.out.println("Question:" + question.description + "?"); //Printing the question to the user
            System.out.println("Following are the choices for answers: ");
            Object[] answerChoicesKey = question.answerChoices.keySet().toArray();
            System.out.println("a. " + answerChoicesKey[0] + "\n" + "b." + answerChoicesKey[1] + "\n" + "c." + answerChoicesKey[2] + "\n" + "d." + answerChoicesKey[3]);
            System.out.println("Please input your answer as 1,2,3 or 4: ");
            try {
                int userAnswer = input.nextInt();
                count+=1;
                if (question.answerChoices.get(userAnswer-1)=="correct") { //Error Handling: If the user does not input a,b,c or d
                    String query = "INSERT INTO APP.STUDENTQUIZ VALUES(" + quizNumber + "," + question.questionNumber + "," + "Correct" + ")";
                    stmt.execute(query);
                }
                else {
                    String query = "INSERT INTO APP.STUDENTQUIZ VALUES(" + quizNumber + "," + question.questionNumber + "," + "Incorrect" + ")";
                    stmt.execute(query);
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Input Mismatch Exception: Please enter integers 1, 2 or 3");
                continue;
            } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }
        }
    }

    private void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    private void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
    
    
    
}
