package App;
import java.sql.*;
import java.util.Date;
import java.util.*;
public class DBQuery {
   /**
    * Retrieve the nutrient value of a food from the database.
    * @param foodName the name of an ingredient
    * @param nutrient the nutrient we want to find the value of
    * @return an integer showing the total value of the specified value in that ingredient/food
    */
   public static int getNutrientVal(String foodName, String nutrient) throws SQLException{
      try (Connection query = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "EECS3311_Project")) {
         PreparedStatement statement = query.prepareStatement(
            "select * from FoodName natural join NutrientAmount natural join NutrientName where FoodDescription = ? and NutrientName = ?"
         );
         statement.setString(1, foodName);
         statement.setString(2, nutrient);
         System.out.println(statement);
         ResultSet rs = statement.executeQuery();

         int result = 0;
         while(rs.next()) {
            result += rs.getInt("NutrientValue");
         }
         return result; //Only returns the first result of the set (by design, it should only give back a single value)
      }
   }

   /**
    * Fetch the stored data of a previously created user
    * @param userID the userID assigned at creation of Profile
    * @return
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
   private static void getLogs(Date logDate, int logType) {

   }
}
