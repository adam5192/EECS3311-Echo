import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

public class Profile {
   private static int nextId = 0;

   //Data
   private int userId;  //Keeps track of which profile is being opened
   private boolean sex; //true = male, false = female
   private Date birth;
   private double height; //In centimeters
   private double weight; //In kg
   private double fatLvl;    //For Karth-McArdle's BMR Calc, range: 0-100
   private List<Log> history;

   //Settings
   public boolean isMetric; //True = Metric, False = Imperial
   private int bmrSetting;   //0 = Miffin St Jeor, 1 = Revised Harris-Benedict, 2 = Katch McArdle

   //Basic constructors
   public Profile() {
      this(true, null, 0.0, 0.0);
   }

   public Profile(boolean sex, Date birth, double height, double weight) {
      //Data
      this.sex = sex;
      this.birth = birth;
      this.height = height;
      this.weight = weight;
      this.fatLvl = 0;

      //Log Update
      Date logDate = new Date();
      logDate.setYear(logDate.getYear() + 1900);
      logDate.setMonth(logDate.getMonth());
      history = new LinkedList<Log>();
      history.add(new DataLog(this.height, this.weight, logDate, this.userId));

      //Settings
      userId = nextId++;
      isMetric = true; //Default in Metric
      bmrSetting = 0;
   }

   // Recreating profile saved in the database
   public Profile(boolean sex, Date birth, double height, double weight, int userID) {
      //Settings
      this.userId = userID;
      isMetric = true; //Default in Metric
      bmrSetting = 0;

      //Data
      this.sex = sex;
      this.birth = birth;
      this.setHeight(height);
      this.setWeight(weight);
   }

   //Setters
   public void setSex(boolean sex) {
      //Not sure to use boolean instead :P
      this.sex = sex;
   }

   //Two options: Use whatever more convenient (or just ask for a specific one)
   public void setBirth(Date birth) {
      this.birth = birth;
   }

   public void setBirth(int year, int month, int day) {
      this.birth = new Date(year, month, day); //TODO: Deprecated object, possible need for different implementation
   }

   //Conversion rate: 1m = 3.281ft
   public void setHeight(double height) {
      try {
         //TODO: Implement get current date & handling 'x ft y in' as input
         Date logDate = new Date();
         //Adjustments to date values for the correct display
         logDate.setYear(logDate.getYear() + 1900);
         logDate.setMonth(logDate.getMonth());

         if (isMetric)
            this.height = height;
         else {
            this.height = (height / 3.281) / 100.0; //Convert feet to centimeters
         }
         history.add(new DataLog(this.height, this.weight, logDate, this.userId));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Conversion rate: 1kg = 2.204bs
   public void setWeight(double weight) {
      try {
         Date logDate = new Date();
         //Adjustments to date values for the correct display
         logDate.setYear(logDate.getYear() + 1900);
         logDate.setMonth(logDate.getMonth());
         if (isMetric)
            this.weight = weight;
         else
            this.weight = weight / 2.204; // Convert pounds input to centimeters
         history.add(new DataLog(this.height, this.weight, logDate, this.userId));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setFatLvl(double fatLvl) {
      try {
         if (fatLvl < 0.0 || fatLvl > 100.0)
            throw new InvalidAttributesException("Invalid fatLvl value, value must be between 0.0 to 100.0");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.fatLvl = fatLvl;
   }

   public void setUnit(boolean isMetric) {
      this.isMetric = isMetric;
   }

   public void setBMR(int bmrSetting) {
      try {
         if (bmrSetting < 0 || bmrSetting > 2)
            throw new InvalidAttributesException("Invalid bmrSetting value, value must be between 1 to 3.");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.bmrSetting = bmrSetting;
   }

   // Sets the history log of a profile to a pre-generated history
   public void setHistory(List<Log> history) {
      this.history = history;
   }

   //Getters
   public boolean getSex() {
      return sex;
   }

   public Date getBirth() {
      return birth;
   }

   public double getHeight() {
      return height;
   }

   public double getWeight() {
      return weight;

   }

   public double getFatLvl() {
      return fatLvl;
   }

   public boolean getIsMetric() {
      return isMetric;
   }

   //Not sure if calc method should just return the name or the value
   public int getCalcMethod() {
      return bmrSetting;
   }

   public String getHistory() {
      String out = "";
      for (int i = 0; i < history.size(); i++)
         out += history.get(i).toString() + "\n";
      return out;
   }

   //Log creation and management

   /**
    * Overloaded method for adding a new log to the profile.
    */
   public void addLog(double height, double weight, Date logDate) {
      history.add(new DataLog(height, weight, logDate, this.userId));
   }

   public void addLog(Ingredient[] ingredients, String mealType, Date logDate) {
      MealLog meal = new MealLog(mealType, logDate, this.userId);
      for (Ingredient i : ingredients) {
         if (i != null)
            meal.addIngredient(i);
      }
      history.add(meal);
   }

   public void addLog(int caloBurnt, double time, Date logDate) {
      history.add(new ExerciseLog(caloBurnt, time, logDate, this.userId));
   }

   /**
    * Remove the first log that matched (based on specified date and type) from history and returns it. Returns null if none matches.
    *
    * @param type 0 for any log, 1 for DataLog, 2 for MealLog, 3 for ExerciseLog
    */
   public Log removeLog(Date logDate, int type) {
      //TODO: Implement better search algorithm (Quicksearch)
      Log removedLog = null;

      //type: 0 = all, 1 = data, 2 = meals, 3 = exercise, default: do nothing
      for (int i = 0; i < history.size(); i++) {
         if (history.get(i).getLogType() == type) {
            removedLog = history.remove(i);
            break;
         }
      }

      return removedLog;
   }
}
