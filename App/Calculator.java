public class Calculator {

    public static double calculateBMR(boolean gender, int age, double weight, double height, int bmrSetting, double bodyFat) {
      double bmr = 0;
  
      // Miffin St Jeor BMR calculation
      if (bmrSetting == 0) { 
        if (gender) {
          bmr = (10 * weight + (6.25 * height) - (5 * age) + 5);
        } else {
          bmr = (10 * weight + (6.25 * height) - (5 * age) - 161);
        }
      } 
      // Revised Harris-Benedict BMR calculation 
      else if (bmrSetting == 1) {
        if (gender) {
          bmr = (13.397 * weight + (4.799 * height) - (5.677 * age) + 88.362);
        } else {
          bmr = (9.247 * weight + (3.098 * height) - (4.330 * age) + 447.593);
        }
      } 
      //Katch McArdle BMR calculation
      else if (bmrSetting == 2) {
        bmr = (370 + 21.6 * (1 - (bodyFat / 100)) * weight);
      } else {}
  
      return bmr;
    }
    public static double calculateCaloriesBurned(double duration, double bmr, double intensity) {
      double CaloriesBurned = 0;
  
      CaloriesBurned = (bmr / 24.0) * intensity * duration;// Calculates calories burned based on Profile  and exercies data
      return CaloriesBurned;
    }
  
  }
