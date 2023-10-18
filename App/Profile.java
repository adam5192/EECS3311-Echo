package EECS3311_Project.App;
import java.util.*;
import javax.naming.directory.InvalidAttributesException;

@SuppressWarnings("deprecation")
//Date values of:
//Year as (year - 1900) (automatically corrected aside from default )
//Month from 0-11 (corrected in log.toString() but be mindful of month in)
//Date is as normal.

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

      //Log Update
      Date logDate = new Date();
      logDate.setYear(logDate.getYear()+1900);
      logDate.setMonth(logDate.getMonth());
      history = new ArrayList<Log>();
      history.add(new DataLog(this.height, this.weight, logDate, this.userId));

      //Settings
      userId = nextId++;
      isMetric = false; //Default in Imperial
      bmrSetting = 0;
   }

   //Setters
   public void setSex(boolean sex) {
      //Not sure to use boolean instead :P
      this.sex = sex;
   }

   //Two options: Use whatever more convenient (or just ask for a specific one)
   public void setBirth(Date birth) {this.birth = birth;}
   public void setBirth(int year, int month, int day) {
      this.birth = new Date(year, month, day); //TODO: Deprecated object, possible need for different implementation
   }

   public void setHeight(double height) {
      //TODO: Implement get current date & handling 'x ft y in' as input
      Date logDate = new Date();
      //Adjustments to date values for the correct display
      logDate.setYear(logDate.getYear()+1900);
      logDate.setMonth(logDate.getMonth());
      if (isMetric) 
         this.height = height;
      else
         this.height = height * 100 / 3.281;

      history.add(new DataLog(this.height, this.weight, logDate, this.userId));
   }

   public void setWeight(double weight) {
      //TODO: Implement get current date & handling 'x ft y in' as input
      Date logDate = new Date();
      logDate.setYear(logDate.getYear()+1900);
      logDate.setMonth(logDate.getMonth());
      if (isMetric)
         this.weight = weight;
      else 
         this.weight = weight / 2.2046;
      history.add(new DataLog(this.height, this.weight, logDate, this.userId));
   }

   public void setFatLvl(double fatLvl) {
      try {
         if (fatLvl < 0.0 || fatLvl > 100.0) throw new InvalidAttributesException("Invalid fatLvl value, value must be between 0.0 to 100.0");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.fatLvl = fatLvl;
   }

   public void setUnit(boolean isMetric) {this.isMetric = isMetric;}

   public void setBMR(int bmrSetting) {
      try {
         if (bmrSetting < 0 || bmrSetting > 2) throw new InvalidAttributesException("Invalid bmrSetting value, value must be between 1 to 3.");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.bmrSetting = bmrSetting;
   }

   // public void setHistory(Map<Date, Log> history) {
   //    this.history = history;
   // }

   //Getters
   public boolean getSex() {return sex;}
   public Date getBirth() {return birth;}
   public double getHeight() {
      if (isMetric)
         return height;
      else
         return (height / 100.0) * 3.281; //Handle conversion: 1m = 3.281ft (= ~3ft 2in)
   }
   public double getWeight() {
      if (isMetric)
         return weight;
      else
         return weight * 2.2046; //Conversion rate: 1kg = 2.2046lbs

   }

   public double getFatLvl() {return fatLvl;}

   public boolean getIsMetric() {return isMetric;}

   //Not sure if calc method should just return the name or the value
   public int getCalcMethod() {return bmrSetting;}

   public String getHistory() {
      String out = "";
      for (int i = 0; i < history.size(); i++)
         out += history.get(i).toString()+"\n";
      return out;
   }

   //Log creation and management
   /**
    * Overloaded method for adding a new log to the profile.
    */
   public void addLog(double height, double weight, Date logDate) {
      this.history.add(new DataLog(height, weight, logDate, this.userId));
   }
   public void addLog(int caloIn, String mealType, Date logDate) {
      this.history.add(new MealLog(caloIn, mealType, logDate, this.userId));
   }
   public void addLog(int caloBurnt, double time, Date logDate) {
      this.history.add(new ExerciseLog(caloBurnt, time, logDate, this.userId));
   }

   /**
    * Remove the first log that matched (based on specified date and type) from history and returns it. Returns null if none matches.
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

   //Temp test method
   public static void main(String args[]) {
      //Creation
      Profile user0 = new Profile(); //Default
      Profile user1 = new Profile(false, new Date(1974, 06, 10), 155.0, 50.0);
      
      //Getters
      if (user0.getSex() && user0.getBirth() == null && user0.getHeight() == 0.0 && user0.getWeight() == 0.0)
         System.out.println("Correct default values.");
      
      if (!user1.getSex() && user1.getBirth().equals(new Date(1974, 06, 10)) 
         && user1.getHeight() == (155.0 * 3.281 / 100) && user1.getWeight() == 50.0 * 2.2046)
         System.out.println("Correct data assignments.");

      if (!user1.isMetric && user1.getFatLvl() == 0)
         System.out.println("Correct default settings.");

      //Setters
      user1.setBirth(2002, 03, 24);
      if (user1.getBirth().equals(new Date(2002, 03, 24))) System.out.println("Correct setBirth(int, int, int).");

      user1.setBirth(new Date(2023, 10, 06));
      if (user1.getBirth().equals(new Date(2023, 10, 06))) System.out.println("Correct setBirth(Date).");

      user1.setFatLvl(20);
      if (user1.getFatLvl() == 20) System.out.println("Correct setFatLvl.");

      user1.isMetric = true;
      user1.setHeight(123.0);
      user1.setWeight(75.0);
      if (user1.getHeight() == 123.0) System.out.println("Correct setHeight().");
      if (user1.getWeight() == 75.0) System.out.println("Correct setWeight().");
      
      user1.isMetric = false;
      if (user1.getHeight() == 123.0 * 3.281 / 100) System.out.println("Correct setHeight().");
      if (user1.getWeight() == 75.0 * 2.2046) System.out.println("Correct setWeight().");

      //Add, remove logs
      Date today = new Date(2023, 11, 17);
      user1.addLog(170.0, 120.0, today);
      user1.addLog(1200, "Lunch", today);
      user1.addLog(200, 20.0, today);
      System.out.println(user1.getHistory());

      user1.removeLog(today, 1);
      System.out.println(user1.getHistory());

      System.out.println("End of test.");
   }
}
