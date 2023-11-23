package App;
import java.sql.*;
import java.util.*;
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
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
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
   public static String[] getIngredientNames() throws SQLException {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
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
    * @return an array of integer representing the users in the current database. If
    * @throws SQLException
    */
   public static String[] getUsers() throws SQLException {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement("select count(*) as ProfileCount from UserProfile");
         int size = statement.executeQuery().getInt("ProfileCount");
         statement = query.prepareStatement("select Username from UserProfile");
         ResultSet rs = statement.executeQuery();
         String[] out = new String[size];
         
         for (int i = 0;rs.next() && i < size; i++) {
            out[i] = rs.getString("Username");
         }

         return out;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Fetch the stored data of a previously created user
    * @param userID the userID assigned at creation of Profile
    * @return a new Profile object that represents the user's profile including settings and logs.
    */
   public static Profile getProfile(int userID) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement(
            "select * from UserProfile inner join ProfileLog " + 
            "on UserProfile.UserID = ProfileLog.UserID" +
            "where UserID = ? order by ProfileLog.LogDate",
            ResultSet.TYPE_SCROLL_INSENSITIVE, // Allow for first(), last(), etc. operations on ResultSet instance
            ResultSet.CONCUR_UPDATABLE
            );
         statement.setInt(2, userID);
         ResultSet rs = statement.executeQuery(); // A set of rows representing logs of the profile being recovered.

         if (rs.wasNull()) return null; // Returns null if no Profile matching userID is found

         // Profile object generation
         rs.first();
         Profile userProfile = new Profile(rs.getString("Username"), rs.getBoolean("Sex"), new java.util.Date(rs.getDate("Birth").getTime()), 
                                 rs.getDouble("CurrHeight"), rs.getDouble("CurrWeight"), rs.getInt("BMRSetting"), userID);
         userProfile.setBMR(rs.getInt("BMR"));
         userProfile.setUnit(rs.getBoolean("IsMetric"));
         userProfile.setFatLvl(rs.getInt("FatLvl"));

         // Log history regeneration
         List<DataLog> dataHistory = new LinkedList<DataLog>(); // Better for add and deletion operations
         List<ExerciseLog> exerciseHistory = new LinkedList<ExerciseLog>();
         List<MealLog> mealHistory = new LinkedList<MealLog>();
         rs.beforeFirst();
         while (rs.next()) {
            PreparedStatement stmt;
            ResultSet log;
            switch (rs.getInt("LogType")) {
               case 1:
                  stmt = query.prepareStatement(
                     "select * from ProfileLog inner join DataLog " + 
                     "on ProfileLog.LogDate = DataLog.LogDate and ProfileLog.LogType = DataLog.LogType " +
                     "where ProfileLog.UserID = ? and ProfileLog.LogDate = ?"
                  );
                  log = stmt.executeQuery();
                  while(log.next()) { // In case there is multiple logs with the same date of the same type from the same user
                     java.util.Date date = new java.util.Date(log.getDate("LogDate").getTime());
                     date.setYear(date.getYear() + 1970);
                     dataHistory.add(new DataLog(log.getDouble("Height"), log.getDouble("Weight"),
                                 date, log.getInt("UserID")));
                  }
                  break;
               case 2:// TODO - Test for errors
                  stmt = query.prepareStatement(
                     "select * from ProfileLog inner join MealLog " + 
                     "on ProfileLog.LogDate = MealLog.LogDate and ProfileLog.LogType = MealLog.LogType " +
                     "where ProfileLog.UserID = ? and ProfileLog.LogDate = ?" +
                     "group by MealLog.MealID",
                     ResultSet.TYPE_SCROLL_INSENSITIVE, // Allow for first(), last(), etc. operations on ResultSet instance
                     ResultSet.CONCUR_UPDATABLE
                  );
                  log = stmt.executeQuery();
                  Ingredient[] list = new Ingredient[100]; // Assumption: No meal has more than 100 ingredients
                  String lastIn = null;
                  for(int i = 0; log.next() && i < 100;) { // In case there is multiple logs with the same date of the same type
                     if (lastIn.equals(null) || lastIn.equals(log.getString("IngredientName"))) {// If first iteration or current ingredient and previous is part of the same meal, add to list as normal
                        list[i++] = new Ingredient(log.getString("IngredientName"), log.getInt("CaloVal"), log.getInt("FatVal"),
                                                log.getInt("ProtVal"), log.getInt("CarbVal"), log.getInt("Others"), log.getInt("Serving"));
                        lastIn = log.getString("IngredientName");
                     }
                     else { // Current ingredient is for a different meal
                        log.previous(); // info required is from the previous index
                        java.util.Date date = new java.util.Date(log.getDate("LogDate").getTime());
                        date.setYear(date.getYear() + 1970);
                        mealHistory.add(new MealLog(log.getString("MealName"), list, log.getString("MealType"), date, log.getInt("UserID")));

                        // Restart the process for the next meal
                        list = new Ingredient[100];
                        lastIn = null;
                        i = 0;
                     }
                  }
                  break;
               case 3:
                  stmt = query.prepareStatement(
                     "select * from ProfileLog inner join ExerciseLog " + 
                     "on ProfileLog.LogDate = ExerciseLog.LogDate and ProfileLog.LogType = ExerciseLog.LogType " +
                     "where ProfileLog.UserID = ? and ProfileLog.LogDate = ?"
                  );
                  log = stmt.executeQuery();
                  while(log.next()) { // In case there is multiple logs with the same date of the same type from the same user
                     java.util.Date date = new java.util.Date(log.getDate("LogDate").getTime());
                     date.setYear(date.getYear() + 1970);
                     exerciseHistory.add(new ExerciseLog(log.getString("ExerciseName"), log.getString("Intensity"), log.getString("ExerciseType"),
                                 log.getInt("Duration"), date, log.getInt("UserID")));
                  }
                  break;
            }
         }

         userProfile.setDataHistory(dataHistory);
         userProfile.setExerciseHistory(exerciseHistory);
         userProfile.setMealHistory(mealHistory);
         return userProfile;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Stores the Profile object into the database.
    */
   public static void storeProfile(Profile user) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         // Storing profile-specific values
         PreparedStatement statement = query.prepareStatement(
            "insert into UserProfile (UserID, Sex, Birth, CurrHeight, CurrWeight, FatLvl, IsMetric, BMRSetting)" +
            "values (?, ?, ?, ?, ?, ?, ?, ?)" +
            "on duplicate key update" +
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

         for(DataLog i : user.getDataHistory()) {storeLog(i);}
         for(ExerciseLog i : user.getExerciseHistory()) {storeLog(i);}
         for(MealLog i : user.getMealHistory()) {storeLog(i);}
      } catch (SQLException e) {e.printStackTrace();}
   }

   /**
    * Stores the given DataLog into the database.
    */
    public static void storeLog(DataLog log) throws SQLException {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         // Updating ProfileLog
         PreparedStatement statement = query.prepareStatement(
            "insert into ProfileLog (LogDate, LogType, UserID)" +
            "values (?, ?, ?)" +
            "on duplicate key update LogDate = LogDate" // On duplicates (log already in database), do nothing
         );
         java.sql.Date date = new java.sql.Date(log.getDate().getTime());
         statement.setDate(1, date);
         statement.setInt(2, log.getLogType());
         statement.setInt(3, log.getUserID());
         statement.executeUpdate();

         // Updating DataLog
         statement = query.prepareStatement(
            "insert into DataLog (LogDate, LogType, UserID, LogHeight, LogWeight)" +
            "values (?, 1, ?, ?, ?)" +
            "on duplicate key update LogWeight = ?, LogHeight = ?"
         );
         statement.setDate(1, date);
         statement.setInt(2, log.getUserID());
         statement.setDouble(3, log.getLogHeight());
         statement.setDouble(4, log.getLogWeight());
         statement.setDouble(5, log.getLogHeight());
         statement.setDouble(6, log.getLogWeight());
         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }

   public static void storeLog(MealLog log) {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         // Updating ProfileLog
         PreparedStatement statement = query.prepareStatement(
            "insert into ProfileLog (LogDate, LogType, UserID)" +
            "values (?, ?, ?)" +
            "on duplicate key update LogDate = LogDate" // On duplicates (log already in database), do nothing
         );
         java.sql.Date date = new java.sql.Date(log.getDate().getTime());
         statement.setDate(1, date);
         statement.setInt(2, log.getLogType());
         statement.setInt(3, log.getUserID());
         statement.executeUpdate();

         // Updating MealLog
         statement = query.prepareStatement(
            "insert into MealLog (LogDate, LogType, UserID, MealType, MealName, CaloVal, CarbVal, FatVal, ProtVal, OthersVal)" +
            "values (?, 2, ?, ?, ?, ?, ?, ?, ?, ?)" +
            "on duplicate key update" +
            "CaloVal = ?, CarbVal = ?, FatVal = ?, ProtVal = ?, OthersVal = ?, Serving =?"
         );
         // Common attributes among ingredients of a meal
         statement.setDate(1, date);
         statement.setInt(2, log.getUserID());
         statement.setString(3, log.getType());

         statement.setString(4, log.getName());
         statement.setInt(5, log.calculateCalories());
         statement.setInt(6, log.calculateCarbs());
         statement.setInt(7, log.calculateFat());
         statement.setInt(8, log.calculateProtein());
         statement.setInt(9, log.calculateOthers());
         // If already existed, update
         statement.setInt(10, log.calculateCalories());
         statement.setInt(11, log.calculateCarbs());
         statement.setInt(12, log.calculateFat());
         statement.setInt(13, log.calculateProtein());
         statement.setInt(14, log.calculateOthers());

         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }

   /**
    * Stores the given ExerciseLog in the database.
    */
   public static void storeLog(ExerciseLog log) throws SQLException {
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         // Updating ProfileLog
         PreparedStatement statement = query.prepareStatement(
            "insert into ProfileLog (LogDate, LogType, UserID)" +
            "values (?, ?, ?)" +
            "on duplicate key update LogDate = LogDate" // On duplicates (log already in database), do nothing
         );
         java.sql.Date date = new java.sql.Date(log.getDate().getTime());
         statement.setDate(1, date);
         statement.setInt(2, log.getLogType());
         statement.setInt(3, log.getUserID());
         statement.executeUpdate();

         // Updating ExerciseLog
         statement = query.prepareStatement(
            "insert into DataLog (LogDate, LogType, UserID, CaloBurnt, ExerciseTime, ExerciseName, Intensity, ExerciseType)" +
            "values (?, 3, ?, ?, ?, ?, ?, ?)" +
            "on duplicate key update CaloBurnt = ?, ExciseTime = ?, ExerciseName = ?, Intensity = ?, ExerciseType = ?"
         );
         statement.setDate(1, date);
         statement.setInt(2, log.getUserID());
         statement.setInt(3, log.getCaloBurnt());
         statement.setDouble(4, log.getDuration());
         statement.setString(5, log.getName());
         statement.setString(6, log.getIntensity());
         statement.setString(7, log.getType());
         //If already existed, update
         statement.setInt(8, log.getCaloBurnt());
         statement.setDouble(9, log.getDuration());
         statement.setString(10, log.getName());
         statement.setString(11, log.getIntensity());
         statement.setString(12, log.getType());
         statement.executeUpdate();
      } catch (SQLException e) {e.printStackTrace();}
   }

}
