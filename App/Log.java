package EECS3311_Project.App;
import java.util.*;

public class Log {
   private Date loggedDate;
   private double loggedHeight;
   private double loggedWeight;

   //Constructors
   public Log() {
      this(0.0, 0.0, null);
   }

   public Log(double height, double weight, Date logDate) {
      loggedHeight = height;
      loggedWeight = weight;
      loggedDate = logDate;
   }

   //Getters
   public double getLoggedHeight() {return this.loggedHeight;}
   public double getLoggedWeight() {return this.loggedWeight;}

   //toString format: YY/MM/DD - Data
   @Override
   public String toString() {
      return String.format("%2d/%2d/%2d - Height: %.2f, Weight: %.2f", 
         loggedDate.getYear(), loggedDate.getMonth(), loggedDate.getDay(), loggedHeight, loggedWeight); //TODO: Deprecated date-time object, change implementation
   }

   //TODO: Implement database queries and calorie/exercise intake logging
}
