import java.sql.*;
import java.util.*;

import javax.swing.plaf.nimbus.State;
@SuppressWarnings("deprecation")
/**
 * NOTE: Change Connection urls, user, and password according to the local database.
 */
public class DBQuery {
   /**
    * Retrieve the nutrient value of a food from the database. The unit is kCal for calories and gram for all other nutrient.
    * Valid nutrient values per design choice: KCAL, PROT, FAT, CARB, OTHERS
    * Assumption: foodDesc input is one-to-one to the names in the value, otherwise, return 0.
    * @param foodDesc the name of an ingredient
    * @param nutrient the nutrient we want to find the value of (in )
    * @return an integer showing the total value of the specified value in that ingredient/food
    */
   public static int getNutrientVal(String foodDesc, String nutrient) throws SQLException{
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         if (!nutrient.equals("OTHERS")) {
            PreparedStatement statement = query.prepareStatement(
                    "select * from FoodName inner join NutrientAmount inner join NutrientName " +
                            "on FoodName.FoodID = NutrientAmount.FoodID and NutrientAmount.NutrientNameID = NutrientName.NutrientNameID " +
                            "where FoodDescription = ? and NutrientSymbol = ?"
            );
            statement.setString(1, foodDesc);
            statement.setString(2, nutrient);
            System.out.println(statement);
            try(ResultSet rs = statement.executeQuery()){
               int result = 0;
               while (rs.next()) {
                  result += rs.getInt("NutrientValue");
               }
               return result; //Only returns the first result of the set (by design, it should only give back a single value)
            }
         }
         else {
            PreparedStatement statement = query.prepareStatement(
                    "select * from FoodName inner join NutrientAmount inner join NutrientName " +
                            "on FoodName.FoodID = NutrientAmount.FoodID and NutrientAmount.NutrientNameID = NutrientName.NutrientNameID " +
                            "where FoodDescription = ? and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ?"
            );
            statement.setString(1, foodDesc);
            statement.setString(2, "KCAL");
            statement.setString(3, "KJ");
            statement.setString(4, "PROT");
            statement.setString(5, "FAT");
            statement.setString(6, "CARB");

            try (ResultSet rs = statement.executeQuery()) {
               int result = 0;
               while (rs.next()) {
                  // If unit is gram, add to sum as is
                  if (rs.getString("NutrientUnit").equals("g"))
                     result += rs.getInt("NutrientValue");
                     // If unit is miligram, convert to gram before adding
                  else if (rs.getString("NutrientUnit").equals("mg"))
                     result += rs.getDouble("NutrientValue") / 100;
                  // Else (microgram, etc.) ignore
               }
               return result;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
         return 0;
      }
   }

   /**
    * Retrieve all the ingredient descriptions in the database.
    * @return Array of Strings that contain of all food's descriptions in the database
    * @throws SQLException
    */
   public static String[] getIngredientNames() {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement("select * from FoodName", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         ResultSet rs = statement.executeQuery();

         // Set size of String array
         rs.last();
         String[] output = new String[rs.getRow()];
         rs.beforeFirst();

         for (int i = 0; rs.next(); i++) {
            output[i] = rs.getString("FoodDescription");
         }

         return output;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Find all the existing profile in the database and output the name of the users
    * @return an array of integer representing the users in the current database. If no profile exists, return null.
    */
   public static ArrayList<Integer> getUsers() {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement("select count(*) as ProfileCount from UserProfile");
         ResultSet rs = statement.executeQuery();
         rs.next();
         if (rs.getInt("ProfileCount") < 0) throw new SQLException();
         statement = query.prepareStatement("select UserId from UserProfile");
         rs = statement.executeQuery();
         ArrayList<Integer> out = new ArrayList<Integer>();

         while (rs.next()) {
            out.add(rs.getInt("UserID"));
         }

         return out;
      } catch (SQLException e) {
         return null;
      }
   }

   /**
    * Fetch the stored data of a previously created user
    * @param userID the userID assigned at creation of Profile
    * @return a new Profile object that represents the user's profile including settings and logs.
    */
   public static Profile getProfile(int userID) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement(
                 "select * from UserProfile " +
                         "where UserID = ?",
                 ResultSet.TYPE_SCROLL_INSENSITIVE, // Allow for first(), last(), etc. operations on ResultSet instance
                 ResultSet.CONCUR_UPDATABLE
         );
         statement.setInt(1, userID);
         ResultSet rs = statement.executeQuery(); // A set of rows representing logs of the profile being recovered.

         //if (rs.wasNull()) return null; // Returns null if no Profile matching userID is found

         // Profile object generation
         rs.first();
         Profile userProfile = new Profile(rs.getString("Username"), rs.getBoolean("Sex"), new java.util.Date(rs.getDate("Birth").getTime()),
                 rs.getDouble("CurrHeight"), rs.getDouble("CurrWeight"), rs.getInt("BMRSetting"), userID);
         userProfile.setUnit(rs.getBoolean("IsMetric"));
         userProfile.setFatLvl(rs.getInt("FatLvl"));

         // Log history regeneration
         List<Log> dataHistory = DBQuery.getDataLog(query, userID);
         List<Exercise> exerciseHistory = DBQuery.getExerciseLog(query, userID);
         List<Meal> mealHistory = DBQuery.getMealLog(query, userID);

         if (dataHistory == null || exerciseHistory == null || mealHistory == null) throw new SQLException();

         userProfile.setDataHistory(dataHistory);
         userProfile.setExerciseHistory(exerciseHistory);
         userProfile.setMealHistory(mealHistory);
         return userProfile;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /*
    * Find all rows in the DataLog table of the database and output a list of Meal objects
    * @param query required as a parameter to ensure singleton trait of the Connection instance
    * @param userID the integer value associating Log objects with a Profile instance
    * @return a List of Log objects associated with the input userID
    */
   public static List<Log> getDataLog(Connection query, int userID) {
      try {
         List<Log> output = new LinkedList<Log>();
         PreparedStatement statement = query.prepareStatement(
                 "select * from DataLog " +
                         "where UserID = ? group by LogDate"
         );
         statement.setInt(1, userID);

         ResultSet rs = statement.executeQuery();
         while(rs.next()) { // In case there is multiple logs with the same date of the same type from the same user
            output.add(new Log(rs.getDouble("LogHeight"), rs.getDouble("LogWeight"),
                    rs.getString("LogDate"), rs.getInt("UserID")));
         }
         return output;
      } catch(SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /*
    * Find all rows in the MealLog table of the database and output a list of Meal objects
    * @param query required as a parameter to ensure singleton trait of the Connection instance
    * @param userID the integer value associating Meal objects with a Profile instance
    * @return a List of Meal objects associated with the input userID
    */
   private static List<Meal> getMealLog(Connection query, int userID) {
      try {
         List<Meal> output = new LinkedList<Meal>();

         PreparedStatement statement = query.prepareStatement(
                 "select * from MealLog " +
                         "where UserID = ? group by LogDate, MealType",
                 ResultSet.TYPE_SCROLL_INSENSITIVE, // Allow for first(), last(), etc. operations on ResultSet instance
                 ResultSet.CONCUR_UPDATABLE
         );
         statement.setInt(1, userID);
         ResultSet rs = statement.executeQuery();
         Meal meal = new Meal();
         String lastIn = null;
         while(rs.next()) {
            if (lastIn == null || lastIn.equals(rs.getString("IngredientName"))) {// If first iteration or current ingredient and previous is part of the same meal, add to list as normal
               Ingredient e = new Ingredient(rs.getString("IngredientName"), rs.getInt("CaloVal"), rs.getInt("FatVal"),
                       rs.getInt("ProtVal"), rs.getInt("CarbVal"), rs.getInt("Others"), rs.getInt("Serving"));
               meal.addIngredient(e);

               lastIn = rs.getString("IngredientName");
            }
            else { // Current ingredient is for a different meal
               rs.previous(); // info required is from the previous index
               java.util.Date date = new java.util.Date(rs.getDate("LogDate").getTime());
               date.setYear(date.getYear() + 1970);

               meal.setType(rs.getString("MealType"));
               output.add(meal);

               // Restart the process for the next meal
               meal = new Meal();
               lastIn = null;
            }
         }

         return output;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /*
    * Find all rows in the Exercise table of the database and output a list of Exercise objects
    * @param query required as a parameter to ensure singleton trait of the Connection instance
    * @param userID the integer value associating Exercise objects with a Profile instance
    * @return a List of Exercise objects associated with the input userID
    */
   private static List<Exercise> getExerciseLog(Connection query, int userID) {
      try {
         List<Exercise> output = new LinkedList<Exercise>();
         PreparedStatement statement = query.prepareStatement(
                 "select * from Exercise " +
                         "where UserID = ? group by LogDate, LogTime, CaloBurnt, ExerciseTime, Intensity, ExerciseType"
         );

         statement.setInt(1, userID);
         ResultSet rs = statement.executeQuery();
         while(rs.next()) { // In case there is multiple logs with the same date of the same type from the same user
            output.add(new Exercise(rs.getString("LogDate"), rs.getString("LogTime"),
                    rs.getString("ExerciseType"), rs.getInt("Duration"), rs.getString("Intensity")));
         }

         return output;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Stores the Profile object into the database.
    */
   public static void storeProfile(Profile user) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         // Storing profile-specific values
         PreparedStatement statement = query.prepareStatement(
                 "insert into UserProfile (UserID, Sex, Birth, CurrHeight, CurrWeight, FatLvl, IsMetric, BMRSetting)" +
                         "values (?, ?, ?, ?, ?, ?, ?, ?) " +
                         "on duplicate key update " +
                         "Sex = ?, Birth = ?, CurrHeight = ?, CurrWeight = ?, FatLvl = ?, IsMetric = ?, BMRSetting = ?"
         );
         // In values
         statement.setInt(1, user.getUserID());
         statement.setBoolean(2, user.getSex());
         java.sql.Date date = new java.sql.Date(user.getBirth().getTime());
         statement.setDate(3, date);
         statement.setDouble(4, user.getHeight());
         statement.setDouble(5, user.getWeight());
         statement.setDouble(6, user.getFatLvl());
         statement.setBoolean(7, user.isMetric);
         statement.setInt(8, user.getCalcMethod());
         // On duplicates, update
         statement.setBoolean(9, user.getSex());
         statement.setDate(10, date);
         statement.setDouble(11, user.getHeight());
         statement.setDouble(12, user.getWeight());
         statement.setDouble(13, user.getFatLvl());
         statement.setBoolean(14, user.isMetric);
         statement.setInt(15, user.getCalcMethod());

         statement.executeUpdate();

         for(Log i : user.getDataHistory()) {storeLog(i);}
         for(Exercise i : user.getExerciseHistory()) {storeLog(i, user.getUserID());}
         for(Meal i : user.getMealHistory()) {storeLog(i, user.getUserID());}
      } catch (SQLException e) {e.printStackTrace();}
   }

   /**
    * Stores the given DataLog into the database.
    */
   public static void storeLog(Log log) throws SQLException {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         // Updating DataLog
         PreparedStatement statement = query.prepareStatement(
                 "insert into DataLog (LogDate, UserID, LogHeight, LogWeight)" +
                         "values (?, ?, ?, ?) " +
                         "on duplicate key update LogWeight = ?, LogHeight = ?"
         );
         statement.setString(1, log.getDate());
         statement.setInt(2, log.getUserID());
         statement.setDouble(3, log.getLogHeight());
         statement.setDouble(4, log.getLogWeight());
         statement.setDouble(5, log.getLogHeight());
         statement.setDouble(6, log.getLogWeight());
         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }

   public static void storeLog(Meal log, int userID) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         // Updating MealLog
         PreparedStatement statement = query.prepareStatement(
                 "insert into MealLog (LogDate, UserID, MealType, CaloVal, CarbVal, FatVal, ProtVal, OthersVal) " +
                         "values (?, ?, ?, ?, ?, ?, ?, ?) " +
                         "on duplicate key update " +
                         "CaloVal = ?, CarbVal = ?, FatVal = ?, ProtVal = ?, OthersVal = ?"
         );
         // Common attributes among ingredients of a meal
         statement.setString(1, log.getDate());
         statement.setInt(2, userID);
         statement.setString(3, log.getMealType());

         statement.setInt(4, log.calculateCalories());
         statement.setInt(5, log.calculateCarbs());
         statement.setInt(6, log.calculateFat());
         statement.setInt(7, log.calculateProtein());
         statement.setInt(8, log.calculateOthers());
         // If already existed, update
         statement.setInt(9, log.calculateCalories());
         statement.setInt(10, log.calculateCarbs());
         statement.setInt(11, log.calculateFat());
         statement.setInt(12, log.calculateProtein());
         statement.setInt(13, log.calculateOthers());

         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }

   /**
    * Stores the given ExerciseLog in the database.
    */
   public static void storeLog(Exercise log, int userID) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306/Project_Database", "root", "EECS3311_Project")) {
         // Updating ExerciseLog
         PreparedStatement statement = query.prepareStatement(
                 "insert into ExerciseLog (LogDate, LogTime, UserID, CaloBurnt, ExerciseTime, Intensity, ExerciseType) " +
                         "values (?, ?, ?, ?, ?, ?, ?) " +
                         "on duplicate key update CaloBurnt = ?, ExerciseTime = ?, Intensity = ?, ExerciseType = ?"
         );
         statement.setString(1, log.getDate());
         statement.setString(2, log.getTime());
         statement.setInt(3, userID);
         statement.setInt(4, log.getCaloriesBurned());
         statement.setInt(5, log.getDuration());
         statement.setString(6, log.getIntensity());
         statement.setString(7, log.getType());
         //If already existed, update
         statement.setInt(8, log.getCaloriesBurned());
         statement.setDouble(9, log.getDuration());
         statement.setString(10, log.getType());
         statement.setString(11, log.getIntensity());
         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }


   /*
    *
    */
   //Stub method, implement if needed
   public static void editProfile(Profile user) {

   }
}
