public class Calculator {

    public static double calculateBMR( Profile user) 
    {
        double bmr = 0;
        int age = ( LocalDate.now().getYear() )- ( user.getBirth().getYear() );
        
        // Miffin St Jeor BMR calculation
        if (user.getCalcMethod() == 0) { 
          if (user.getSex()) {
            bmr = (10 * user.getWeight() + (6.25 * user.getHeight()) - (5 * age) + 5);
          } else {
            bmr = (10 * user.getWeight() + (6.25 * user.getHeight()) - (5 * age) - 161);
          }
        } 
        // Revised Harris-Benedict BMR calculation 
        else if (user.getCalcMethod() == 1) {
          if (user.getSex()) {
            bmr = (13.397 * user.getWeight() + (4.799 * user.getHeight()) - (5.677 * age) + 88.362);
          } else {
            bmr = (9.247 * user.getWeight() + (3.098 * user.getHeight()) - (4.330 * age) + 447.593);
          }
        } 
        //Katch McArdle BMR calculation
        else if (user.getCalcMethod() == 2) {
          bmr = (370 + 21.6 * (1 - (user.getFatLvl() / 100)) * user.getWeight());
        }
        
    	return (Math.round(bmr));
    }
    public static double calculateCaloriesBurned(double duration, double bmr, double intensity) {
      double CaloriesBurned = 0;
  
      CaloriesBurned = (bmr / 24.0) * intensity * duration;// Calculates calories burned based on Profile  and exercies data
      return CaloriesBurned;
    }
  
  }
