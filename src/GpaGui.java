/**
 * Lenell Davis
 * CMIS 242
 * Project 4
 * 5/8/16
 * GpaGui.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class GpaGui extends JFrame{
    private final Font font = new Font ("Arial", Font.BOLD, 18);
    
    //Takes care of the Frames for the Error corrections, Successful input and the Update Grade Frames
    private final JFrame frame = new JFrame();
    private final JFrame gradeFrame = new JFrame("Grade information");
    private final String[] boxOption = {"Insert", "Delete", "Find", "Update"};
    private final String[] gradeOption = {"A", "B", "C", "D", "F"};
    private final String[] creditsOption = {"3 credits", "6 credits"};
    
    //Creates the Labels for the Gui
    private final JLabel idLabel = new JLabel("ID: (Numbers only)");
    private final JLabel nameLabel = new JLabel("First and Last Name:");
    private final JLabel majorLabel = new JLabel("Major:");
    private final JLabel selectLabel = new JLabel("Choice Selection:");
    
    //Creates the TextFields for the Gui
    private final JTextField idField = new JTextField(25);
    private final JTextField nameField = new JTextField(25);
    private final JTextField majorField = new JTextField();
    
    //Creates the ComboBox and Button for the Gui
    private final JComboBox selectBox = new JComboBox(boxOption);
    private final JButton processBtn = new JButton("Process Request");
    
    //Creates the Panels for the Gui
    private final JPanel mainPanel = new JPanel();
    private final JPanel optionPanel = new JPanel(); 
    
    //Creates the Hashmap and Student Object for the database
    private final HashMap<Integer, Student> studentHash = new HashMap<>();
    private Student newStudent;
    
    
    /**
     * Gui Constructor
     */
    public GpaGui(){
        setFrame();
        setAttributes();
        setPanels();
        gradeFrame.addWindowListener(new CloseGradeFrame());
    }
    
    /*
    * Sets the attributes for the frame
    */
    private void setFrame() {     
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student Database");
        add(mainPanel);
        addWindowListener(new CloseGradeFrame());
    }
    
    /**
     * Sets Attributes for Components
     */
    private void setAttributes(){
        optionPanel.setLayout(new GridLayout(4,2));
        idLabel.setFont(font);
        nameLabel.setFont(font);
        majorLabel.setFont(font);
        selectLabel.setFont(font);
        processBtn.setFont(font);
        selectBox.setFont(font);
        processBtn.addActionListener(new ButtonClick());
    }
    
    /**
     * Adds components to the panels
     * Adds the panels to the main panel
     */
    private void setPanels(){
        optionPanel.add(idLabel);
        optionPanel.add(idField);
        optionPanel.add(nameLabel);
        optionPanel.add(nameField);
        optionPanel.add(majorLabel);
        optionPanel.add(majorField);
        optionPanel.add(selectLabel);
        optionPanel.add(selectBox);
        
        mainPanel.add(optionPanel);
        mainPanel.add(processBtn);
    }
    
    
    /**
     * CallStudent method 
     * Called after entries are validated.
     * Performs an action based on which element of the ComboBox is selected
     */
    private void callStudent(){
        String selectedOption = (String)selectBox.getSelectedItem(); 
        int currentStudentID = Integer.parseInt(idField.getText());
        
        switch(selectedOption){
            case "Insert":
                newStudent = new Student(nameField.getText(), majorField.getText());
                studentHash.put(currentStudentID, newStudent);
                success("Insertion");
                emptyTextFields();
                break;
            case "Delete":
                studentHash.remove(currentStudentID);
                success("Deletion");
                emptyTextFields();
                break;
            case "Find":
                try{
                    Student currentStudent = studentHash.get(currentStudentID);
                    String db_StudentName = currentStudent.name;
                    String db_StudentMajor = currentStudent.major;
                    Double db_StudentGPA = currentStudent.gpa;

                    String showUser = newStudent.toString(currentStudentID, db_StudentName, db_StudentMajor, db_StudentGPA);
                    
                    JOptionPane.showMessageDialog(frame, showUser);
                    emptyTextFields();
                }
                catch(NullPointerException n){
                    JOptionPane.showMessageDialog(frame, "The Student you requested was not found. Verify ID Number and try again.");
                    emptyTextFields();
                }
                break;
            case "Update":
                double letterValue = getLetterGrade();
                int creditValue = getCredits();
                Student currentStudent = studentHash.get(currentStudentID);
                double db_StudentGPA = currentStudent.gpa;
                double updatedGPA = newStudent.courseCompleted(letterValue, creditValue, db_StudentGPA);
                currentStudent.gpa = updatedGPA;
                emptyTextFields();
                break;
            default:
                      
        }
    }
    
    /**
     * Clears the textFields for new inputs
     */
    public void emptyTextFields(){
        idField.setText("");
        nameField.setText("");
        majorField.setText("");
    }
    
    /**
     * Part 1 of the Validation chain. 
     * Validates that the id is an integer
     */
    private void guiIdValidation(){
        String id = idField.getText();
        
        try{
            int idNumber = Integer.parseInt(id);
            guiNameValidation();
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame, "Please enter an integer.");
        }
    }
    
    /**
     * Part 2 of the Validation chain. 
     * Validates that the name is a string of the correct pattern using regex
     */
    private void guiNameValidation(){
        //.* tests the entire string and not one character. The \\s test for white space
        String regexName = "[A-Za-z][^0-9].*"+" "+"[A-Za-z][^0-9].*";
        String name = nameField.getText();

        if(name.matches(regexName)){
            guiMajorValidation();
        }
        else{
            JOptionPane.showMessageDialog(frame, "Please enter a name. Example: Susan Rogers");
        }

    }
    
    /**
     * Part 3 of the Validation chain. 
     * Validates that the major is a string of the correct pattern using regex
     */
    private void guiMajorValidation(){
        //.* tests the entire string and not one character. The \\s test for white space
        String regexName = "[A-Za-z][^0-9].*"+" "+"[A-Za-z][^0-9].*";
        String regexMajor = "[A-Za-z][^0-9].*";
        String major = majorField.getText();

        if(major.matches(regexName)){
            callStudent();
        }
        else if (major.matches(regexMajor)){
            callStudent();
        }
        else{
            JOptionPane.showMessageDialog(frame, "Please enter a major. Example: Anthropology or Computer Science");
        }
    }
    /**
     * Shows that the operation was a success
     */
    private void success(String a){
        JOptionPane.showMessageDialog(frame, a + " Operation Successful.");
    }
    
    /**
     * Shows inputDialog for Letter Grade
     */
    private double getLetterGrade(){
        String letter = (String)JOptionPane.showInputDialog(gradeFrame, "Please Choose a Letter Grade", "Letter Grade Option", JOptionPane.QUESTION_MESSAGE, null, gradeOption, gradeOption[0]);
        double returnLetterValue = 5.0;
        
        try{
            switch(letter){
                case "A":
                    returnLetterValue = 4.0;
                break;
                case "B":
                    returnLetterValue = 3.0;
                break;
                case "C":
                    returnLetterValue = 2.0;
                break;
                case "D":
                    returnLetterValue = 1.0;
                break;
                case "F":
                    returnLetterValue = 0.0;
                break;
            }
        }
        catch(NullPointerException n){
            System.out.println("Null Object Created. returnLetterValue not being set. Will manually set returnLetterValue to 0.0");
            returnLetterValue = 0.0;
        }
        return returnLetterValue;
    }
    
    /**
     * Shows inputDialog for Credit Hours
     */
    private int getCredits(){
        String credit = (String)JOptionPane.showInputDialog(gradeFrame, "Please Choose the Number of Credits", "Credit Option", JOptionPane.QUESTION_MESSAGE, null, creditsOption, creditsOption[0]);
        int returnCreditValue = 1;
        
        try{
            switch(credit){
                case "3 credits":
                    returnCreditValue = 3;
                break;
                case "6 credits":
                    returnCreditValue = 6;

                break;
                default:
                    returnCreditValue = 1;
            }
        }
        catch(NullPointerException n){
            System.out.println("Null Object Created. returnCreditValue not being set. Will manually set returnCreditValue to 1");
        }
        
        return returnCreditValue;
    }
    
    /**
     * Inner class that controls the Button
     * Begins the Validation process before calling the callStudent Method
     * Ensures that all fields are valid before the operation can begin
     */
    private class ButtonClick implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            guiIdValidation();
        }
    }

    /**
     *Inner class that handles the WindowClose Event
     */
    private class CloseGradeFrame extends WindowAdapter{

        @Override
        public void windowClosed(WindowEvent e){
            System.exit(0);
        }
    }
    
}
