package App;
import java.sql.*;
import java.util.Date;
import java.util.*;
public class DBQuery {
   /**
    * Retrieve the nutrient value of a food from the database.
    * Valid nutrient values per design choice: KCAL, PROT, FAT, CARB, OTHERS
    * Assumption: foodDesc input is one-to-one to the names in the value, otherwise, return 0.
    * @param foodDesc the name of an ingredient
    * @param nutrient the nutrient we want to find the value of (in )
    * @return an integer showing the total value of the specified value in that ingredient/food
    */
   public static int getNutrientVal(String foodDesc, String nutrient) throws SQLException{
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         if (nutrient != "OTHERS") {
            PreparedStatement statement = query.prepareStatement(
               "select * from FoodName natural join NutrientAmount natural join NutrientName where FoodDescription = ? and NutrientSymbol = ?"
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
               "select * from FoodName natural join NutrientAmount natural join NutrientName where FoodDescription = ? " + 
               "and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ? and NutrientSymbol <> ?"
            ); // Get all other nutrients (will be changed)
            statement.setString(1, foodDesc);
            statement.setString(2, "KCAL");
            statement.setString(3, "KJ");
            statement.setString(4, "PROT");
            statement.setString(5, "FAT");
            statement.setString(6, "CARB");

            try (ResultSet rs = statement.executeQuery()) {
               int result = 0;
               while (rs.next())
                  result += rs.getInt("NutrientValue");
               return result;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
         return 0;
      }
   }

   /**
    * Fetch the stored data of a previously created user
    * @param userID the userID assigned at creation of Profile
    * @return a new Profile object that represents the user's profile including settings and logs.
    */
   public static Profile getProfile(int userID) throws SQLException{
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement("select * from ? where UserID = ?");
         statement.setString(1, "UserProfile");
         statement.setInt(2, userID);
         ResultSet rs = statement.executeQuery();

         Profile userProfile = new Profile(rs.getBoolean("Sex"), rs.getDate("Birth"), rs.getDouble("Height"), rs.getDouble("Weight"), userID);
         statement.setString(1, "ProfileLog");
         rs = statement.executeQuery();

         // a.setHistory

         return userProfile;
      }
   }

   /**
    * Restore the values of the logs to the correct values
    */
   private static void getLog(Date logDate, int logType) {

   }
}