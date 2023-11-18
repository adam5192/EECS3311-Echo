public  class CalculateCaloriesBurned{

public static double calculateCaloriesBurned(double duration, double bmr, double intensity) {
    double CaloriesBurned = 0;

    CaloriesBurned = (bmr / 24.0) * intensity * duration;// Calculates calories burned based on Profile  and exercies data
    return CaloriesBurned;
  }

}
