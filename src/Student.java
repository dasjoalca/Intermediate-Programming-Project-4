/**
 * Lenell Davis
 * CMIS 242
 * Project 4
 * 5/8/16
 * Student.java
 */

public class Student {
    public final String name;
    public final String major;
    public double gpa;
    public int creditHours;
    
    /**
     * Constructor for the Student Class
     * Sets the name, major and GPA for each student depending on input
     */
    public Student(String guiName, String guiMajor){
        this.name = guiName;
        this.major = guiMajor;
        gpa = 0.00;
        creditHours = 0;
    }
    
    /**
     * Called when Update is Selected
     */
    public double courseCompleted(double letterGrade, int credits, double currentGPA){
        double newGPA = (letterGrade * credits) / credits;
        double returnedGPA = updateGPA(newGPA, currentGPA);
        
        return returnedGPA;
    }
    
    /**
     * Called when Find is Selected. Returns string about student.
     */
    public String toString(int id, String name, String major, double gpa){
        String returnTo = String.format("Student ID: %d\nStudent Name: %s\nMajor: %s\nGPA: %.2f", id, name, major, gpa);
        return returnTo;
    }
    
    /**
     * Updates the GPA when multiple Grades are entered
     */    
    public double updateGPA(double newGPA, double currentGPA){
        double updatedGPA = 0.0;
        if(currentGPA > 0 ){
            updatedGPA = (currentGPA + newGPA) / 2;
        }
        else{
            updatedGPA = newGPA;
        }
        return updatedGPA;
    }
    
}
