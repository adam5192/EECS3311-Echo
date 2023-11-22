
public class CalculateBMR {

	
	public static double calculateBMR(Date date, double weight, double height, Boolean gender, int bmrSetting, double FatLvl) 
    {
        double bmr = 0;
        int age = ( LocalDate.now().getYear() )- ( date.getYear() );
        
        // Miffin St Jeor BMR calculation
        if (bmrSetting == 0) { 
          if (gender) {
            bmr = (10 * weight + (6.25 * height ) - (5 * age) + 5);
          } else {
            bmr = (10 * weight + (6.25 * height ) - (5 * age) - 161);
          }
        } 
        // Revised Harris-Benedict BMR calculation 
        else if (bmrSetting == 1) {
          if (gender) {
            bmr = (13.397 * weight + (4.799 * height ) - (5.677 * age) + 88.362);
          } else {
            bmr = (9.247 * weight + (3.098 * height ) - (4.330 * age) + 447.593);
          }
        } 
        //Katch McArdle BMR calculation
        else if (bmrSetting == 2) {
          bmr = (370 + 21.6 * (1 - ( FatLvl / 100)) * FatLvl );
        }
        
    	return (Math.round(bmr));
    }

	
}
