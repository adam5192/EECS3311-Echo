package EECS3311_Project.App;
import java.util.*;

@SuppressWarnings("deprecation")
public class Application {
   private static List<Profile> userProfiles;
   private static int currentUser;

   public Application() {
      userProfiles = new ArrayList<Profile>();
   }

   public void loadProfile(int userId) {
      if (userProfiles.isEmpty()) requestProfileCreation(); //If no profile is found, prompt for profile into and create a new profile
      currentUser = userId;
      displayProfile(userProfiles.get(userId));
   }

   /**
    * Display a profile creation splash screen and prompts the user for the basic data to create a new profile.
    */
   private static void requestProfileCreation() {
      //TODO: Implement UI call to prompt user for data
      Scanner data = new Scanner(System.in);
      Profile newProfile = new Profile();
      System.out.println("User sex (true for male, false for female):");
      boolean sex = data.nextBoolean();
      newProfile.setSex(sex);
      System.out.println("User's date of birth (DD/MM/YYYY):");
      String[] birthIn = data.nextLine().split("/");
      int[] dates = new int[3];
      for (int i = 0; i < 2; i++)
         //Always take only the first 3 integers, the rest are considered redundant.
         dates[i] = Integer.parseInt(birthIn[i]);
      newProfile.setBirth(dates[2], dates[1], dates[0]);
      System.out.println("Preferred unit of measurement (true for metric, false for imperial):");
      boolean isMetric = data.nextBoolean();
      newProfile.setUnit(isMetric);
      
      System.out.println("User height (centimeter or foot only):");
      double height = data.nextDouble();
      newProfile.setHeight(height);
      System.out.println("User weight (kilogram or lbs only):");
      double weight = data.nextDouble();
      newProfile.setWeight(weight);
      
      data.close();
      userProfiles.add(newProfile);
   }

   //TODO Implement draws/displays UIs
   public static void displayProfile(Profile current) {}
   public static void displayLoadingBar() {}
   public static void displayEditScreen() {}
   public static void displayCaloChart() {}
   public static void displayNutrientChart() {}

   //Adding new logs
   public void addMeal(int caloIn, String mealType) {
      Date date = new Date();
      date.setYear(date.getYear()+1900);
      addMeal(caloIn, mealType, date);
   }
   public void addMeal(int caloIn, String mealType, Date logDate) {userProfiles.get(currentUser).addLog(caloIn, mealType, logDate);}

   public void addExercise(int caloBurnt, double time) {
      Date date = new Date();
      date.setYear(date.getYear()+1900);
      addExercise(caloBurnt, time, date);
   }
   public void addExercise(int caloBurnt, double time, Date logDate) {userProfiles.get(currentUser).addLog(caloBurnt, time, logDate);}

   public void addData(double height, double weight) {
      Date date = new Date();
      date.setYear(date.getYear()+1900);
      addData(height, weight, date);
   }
   public void addData(double height, double weight, Date logDate) {userProfiles.get(currentUser).addLog(height, weight, logDate);}

}
