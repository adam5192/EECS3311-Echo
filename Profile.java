package EECS3311_Project.profile;
import java.util.*;

import javax.naming.directory.InvalidAttributesException;

public class Profile {
   //Data
   private char sex;
   private Date birth;
   private double height; //In centimeters
   private double weight;
   private double fatLvl;    //For Karth-McArdle's BMR Calc, range: 0-100
   private List<Log> history;

   //Settings
   private boolean isMetric; //True = Metric, False = Imperial
   private int bmrSetting;   //1 = Miffin St Jeor, 2 = Revised Harris-Benedict, 3 = Katch McArdle

   //Basic constructors
   public Profile() {
      this(' ', new Date(), 0.0, 0.0);
   }

   public Profile(char sex, Date birth, double height, double weight) {
      //Data
      this.sex = sex;
      this.birth = birth;
      this. height = height;
      this.weight = weight;

      //Log Update
      Date logDate = new Date(); //TODO: Implement get current date
      history = new ArrayList<Log>();
      history.add(new Log(this.height, this.weight, logDate));

      //Settings
      isMetric = false; //Default in Imperial
      bmrSetting = 1;
   }

   //Setters
   public void setSex(char sex) {
      //Not sure to use boolean instead :P
      this.sex = sex;
   }

   //Two options: Use whatever more convenient (or just ask for a specific one)
   public void setBirth(Date birth) {this.birth = birth;}
   public void setBirth(int year, int month, int day) {
      this.birth = new Date(year, month, day);
   }

   public void setHeight(double height) {
      //TODO: Implement get current date
      Date logDate = new Date();
      this.height = height;
      history.add(new Log(this.height, this.weight, logDate));
   }

   public void setWeight(double weight) {
      //TODO: Implement get current date
      Date logDate = new Date();
      this.weight = weight;
      history.add(new Log(this.height, this.weight, logDate));
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
         if (bmrSetting < 1 || bmrSetting > 3) throw new InvalidAttributesException("Invalid bmrSetting value, value must be between 1 to 3.");
      } catch (Exception e) {
         e.printStackTrace();
      }

      this.bmrSetting = bmrSetting;
   }

   // public void setHistory(Map<Date, Log> history) {
   //    this.history = history;
   // }

   //Getters
   public char getSex() {return sex;}
   public Date getBirth() {return birth;}
   public double getHeight() {
      if (isMetric)
         return height;
      else
         return (height / 100.0) * 3.281; //TODO: Handle conversion: 1m = 3.281ft (= ~3ft 2in)
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
}

class Log {
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
         loggedDate.getYear(), loggedDate.getMonth(), loggedDate.getDay(), loggedHeight, loggedWeight);
   }
}

