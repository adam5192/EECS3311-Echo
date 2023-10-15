package EECS3311_Project.App;
import java.util.*;

import javax.management.InvalidAttributeValueException;

//TODO: Implement storing and accessing database
public class Log {
   private Date loggedDate;

   //Constructors
   public Log() {
      this(null);
   }

   public Log(Date logDate) {
      loggedDate = logDate;
   }

   //toString format: YY/MM/DD 
   @Override
   public String toString() {
      //TODO: Deprecated date-time object, change implementation
      return String.format("%2d/%2d/%2d", loggedDate.getYear(), loggedDate.getMonth(), loggedDate.getDay()); 
   }

   //TODO: Implement database queries and calorie/exercise intake logging
}

//For logging height and weight changes.
class DataLog extends Log {
   private double loggedHeight;
   private double loggedWeight;

   //Constructors
   public DataLog() {
      super();
      loggedHeight = 0.0;
      loggedWeight = 0.0;
   }

   public DataLog(double height, double weight, Date logDate) {
      super(logDate);
      loggedHeight = height;
      loggedWeight = weight;
   }

   //Getters
   public double getLoggedHeight() {return this.loggedHeight;}
   public double getLoggedWeight() {return this.loggedWeight;}
   
   //Might not need setters

   //toString format: YY/MM/DD - Height, Weight
   public String toString() {
      return super.toString() + String.format(" - Height: %.2f, Weight: %.2f", loggedHeight, loggedWeight);
   }
}

//For logging meals
class MealLog extends Log {
   private int caloIn;   //Calorie intake
   private String mealType; //Valid values: "Breakfast", "Lunch", "Dinner"

   //Constructors
   public MealLog() {
      super();
      caloIn = 0;
      mealType = "Breakfast";
   }

   public MealLog(int caloIn, String mealType, Date logDate) {
      super(logDate);
      try {
         if (caloIn < 0) 
            throw new InvalidAttributeValueException("Value of calorie intake cannot be negative.");
         else if (!(mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner")))
            throw new InvalidAttributeValueException("Invalid mealType value.");
      } catch (Exception e) {
         e.printStackTrace();
      }
      this.caloIn = caloIn;
      this.mealType = mealType;
   }

   //Getters
   public int getCaloIn() {return this.caloIn;}
   /**
    * Getter method for mealType.
    * @return the meal type associated with the log, null if the value of mealType is invalid.
    */
   public String getMealType() {return mealType;}

   //Setters
   public void setCaloIn(int newIn) {
      try {
         if (newIn < 0) throw new InvalidAttributeValueException("Calorie intake cannot be negative.");
      } catch (Exception e) {
         e.printStackTrace();
      }
      caloIn = newIn;
   }

   public void setExercise(String mealType) {
      try {
         if (!(mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner"))) 
            throw new InvalidAttributeValueException("Invalid mealType value.");
      } catch (Exception e) {
         e.printStackTrace();
      }
      this.mealType = mealType;
   }

   //Add intake into the log with matching date
   public static void setCaloIn(Date logDate, int caloIn) {
      //TODO: Implement find specified log in database using logDate.
      MealLog temp = new MealLog();
      try {
         if (caloIn < 0) throw new InvalidAttributeValueException("Calorie intake cannot be negative.");
      } catch (Exception e) {
         e.printStackTrace();
      }
      temp.caloIn += caloIn;
   }

   //toString format: YY/MM/DD - Meal Type - Amount
   public String toString() {
      return super.toString() + String.format(" - %s - Amount: %d", mealType, caloIn);
   }
}
