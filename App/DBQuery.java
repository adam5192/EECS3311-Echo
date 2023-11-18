package App;
import java.sql.*;
import java.util.Date;
import java.util.*;
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
         PreparedStatement statement = query.prepareStatement("select * from FoodName");
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
    * Fetch the stored data of a previously created user
    * @param userID the userID assigned at creation of Profile
    * @return a new Profile object that represents the user's profile including settings and logs.
    */
   public static Profile getProfile(int userID) throws SQLException{
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement(
            "select * from UserProfile inner join ProfileLog " + 
            "on UserProfile.UserID = ProfileLog.UserID" +
            "where UserID = ? order by ProfileLog.LogDate"
            );
         statement.setInt(2, userID);
         ResultSet rs = statement.executeQuery(); // A set of rows representing logs of the profile being recovered.

         // Profile object generation
         rs.first();
         Profile userProfile = new Profile(rs.getBoolean("Sex"), rs.getDate("Birth"), 
                                 rs.getDouble("CurrHeight"), rs.getDouble("CurrWeight"), userID);
         userProfile.setBMR(rs.getInt("BMR"));
         userProfile.setUnit(rs.getBoolean("IsMetric"));
         userProfile.setFatLvl(rs.getInt("FatLvl"));

         // Log history regeneration
         List<Log> history = new LinkedList<Log>(); // Better for add and deletion operations
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
                     history.add(new DataLog(log.getDouble("Height"), log.getDouble("Weight"),
                                 log.getDate("LogDate"), log.getInt("UserID")));
                  }
                  break;
               case 2:// TODO - Implement MealLog adding
                  stmt = query.prepareStatement(
                     "select * from ProfileLog inner join MealLog " + 
                     "on ProfileLog.LogDate = MealLog.LogDate and ProfileLog.LogType = MealLog.LogType " +
                     "where ProfileLog.UserID = ? and ProfileLog.LogDate = ?" +
                     "group by MealLog.MealID"
                  );
                  log = stmt.executeQuery();
                  Ingredient[] list = new Ingredient[100]; // Assumption: No meal has more than 100 ingredients
                  String lastIn = null;
                  for(int i = 0; log.next() && i < 100;) { // In case there is multiple logs with the same date of the same type
                     if (lastIn.equals(null) || lastIn.equals(log.getString("IngredientName"))) {// If first iteration or current ingredient and previous is part of the same meal, add to list as normal
                        list[i++] = new Ingredient(log.getString("IngredientName"), log.getInt("CaloVal"), log.getInt("FatVal"),
                                                log.getInt("ProtVal"), log.getInt("CarbVal"), log.getInt("Others"));
                        lastIn = log.getString("IngredientName");
                     }
                     else { // Current ingredient is for a different meal
                        log.previous(); // info required is from the previous index
                        history.add(new MealLog(list, log.getString("MealType"), log.getDate("LogDate"), log.getInt("UserID")));

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
                     history.add(new DataLog(log.getInt("CaloBurnt"), log.getDouble("ExerciseTime"),
                                 log.getDate("LogDate"), log.getInt("UserID")));
                  }
                  break;
            }
         }

         userProfile.setHistory(history);
         return userProfile;
      } catch (SQLException e) {
         e.printStackTrace();
         return null;
      }
   }
}
