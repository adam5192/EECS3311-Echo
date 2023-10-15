package EECS3311_Project.App;
import java.util.*;
import javax.naming.directory.InvalidAttributesException;

public class Profile {
   //Data
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
      this. height = height;
      this.weight = weight;

      //Log Update
      Date logDate = new Date(); //TODO: Implement get current date
      history = new ArrayList<Log>();
      history.add(new DataLog(this.height, this.weight, logDate));

      //Settings
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
      if (isMetric) 
         this.height = height;
      else
         this.height = height * 100 / 3.281;

      history.add(new DataLog(this.height, this.weight, logDate));
   }

   public void setWeight(double weight) {
      //TODO: Implement get current date & handling 'x ft y in' as input
      Date logDate = new Date();
      if (isMetric)
         this.weight = weight;
      else 
         this.weight = weight / 2.2046;
      history.add(new DataLog(this.height, this.weight, logDate));
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


   //Temp test method
   public static void main(String args[]) {
      //Creation
      Profile user0 = new Profile(); //Default
      Profile user1 = new Profile(false, new Date(1974, 06, 10), 155.0, 50.0);
      
      //Getters
      if (!(user0.getSex() && user0.getBirth() == null) && user0.getHeight() == 0.0 && user0.getWeight() == 0.0)
         System.out.println("Incorrect default values.");
      
      if (!(!user1.getSex() && user1.getBirth().equals(new Date(1974, 06, 10)) 
         && user1.getHeight() == (155.0 / 100 * 3.281) && user1.getWeight() == 50.0 * 2.2046))
         System.out.println("Incorrect data assignments.");

      if (!(!user1.isMetric && user1.getFatLvl() == 0))
         System.out.println("Incorrect default settings.");

      //Setters
      user1.setBirth(2002, 03, 24);
      if (!user1.getBirth().equals(new Date(2002, 03, 24))) System.out.println("Incorrect setBirth(int, int, int).");

      user1.setBirth(new Date(2023, 10, 06));
      if (!user1.getBirth().equals(new Date(2023, 10, 06))) System.out.println("Incorrect setBirth(Date).");

      user1.setFatLvl(20);
      if (user1.getFatLvl() != 20) System.out.println("Incorrect setFatLvl.");

      user1.isMetric = true;
      user1.setHeight(123.0);
      user1.setWeight(75.0);
      if (user1.getHeight() != 123.0) System.out.println("Incorrect setHeight().");
      if (user1.getWeight() != 75.0) System.out.println("Incorrect setWeight().");
      
      user1.isMetric = false;
      if (user1.getHeight() != 123.0 * 3.281 / 100) System.out.println("Incorrect setHeight().");
      if (user1.getWeight() != 75.0 * 2.2046) System.out.println("Incorrect setWeight().");

      System.out.println("End of test.");
   }
}
