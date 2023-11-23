package App;
import java.util.*;
import javax.management.InvalidAttributeValueException;

//For logging height and weight changes.
public class Log {
   private int userId;
   private String logDate;
   private double logHeight;
   private double logWeight;

   //Constructors
   public Log(int userId) {
      this(0, 0, "", userId);
   }

   public Log(double height, double weight, String logDate, int userId) {
      this.logDate = logDate;
      this.userId = userId;
      logHeight = height;
      logWeight = weight;
   }

   //Setters
   public void setDate(String logDate) {this.logDate = logDate;}
   public void setLogHeight(double height) {
      try {
         if (height < 0.0) throw new InvalidAttributeValueException("Height cannot be negative.");
         this.logHeight = height;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setLogWeight(double weight) {
      try {
         if (weight < 0.0) throw new InvalidAttributeValueException("Weight cannot be negative.");
         this.logWeight = weight;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Getters
   public String getDate() {return logDate;}
   public int getUserID() {return userId;}
   public double getLogHeight() {return this.logHeight;}
   public double getLogWeight() {return this.logWeight;}

   //toString format: YY/MM/DD 
   @Override
   public String toString() {
      return String.format("%s - Height: %.2f, Weight: %.2f", logDate, logHeight, logWeight);
   }
}
//   
//   public static void main(String[] args) {
//      //DataLog
//      int dummyID = 0;
//      DataLog data = new DataLog(dummyID);
//      System.out.println(data.toString());
//      data.setLogHeight(170);
//      data.setLogWeight(80);
//      System.out.println(data.toString());
//
//      data = new DataLog(180, 70, new Date(2023, 9, 15), dummyID);
//      System.out.println(data.toString()+"\n");
//
//      //MealLog
//      // examples
//      Ingredient tomato = new Ingredient("Tomato", 20, 0, 1, 6, 0);
//      Ingredient bread = new Ingredient("Bread", 255, 3, 8, 51, 0);
//      Ingredient egg = new Ingredient("Egg", 145, 11, 14, 2, 0);
//
//      // create Meal object
//      MealLog breakfast = new MealLog(dummyID);
//      breakfast.setType("Breakfast");
//      breakfast.setDate(new Date(2023, 9, 14));
//      breakfast.addIngredient(tomato);
//      breakfast.addIngredient(bread);
//      breakfast.addIngredient(egg);
//
//      // display all meals logged in the MealLogger
//      System.out.println(breakfast.toString());
//      System.out.println(breakfast.getIngredientString());
//
//      //ExerciseLog
//      ExerciseLog exercise = new ExerciseLog(dummyID);
//      System.out.println(exercise.toString());
//      exercise.setCaloBurnt(420);
//      exercise.setTime(30);
//      System.out.println(exercise.toString());
//
//      exercise = new ExerciseLog(600, 120, new Date(2023, 9, 19), dummyID);
//      System.out.println(exercise.toString()+"\n");
//   }
//}
