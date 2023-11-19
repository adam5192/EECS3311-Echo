import java.util.*;
import javax.management.InvalidAttributeValueException;

//For logging height and weight changes.
class DataLog extends Log {
   private double logHeight;
   private double logWeight;

   //Constructors
   public DataLog(int userId) {
      super(userId);
      logHeight = 0.0;
      logWeight = 0.0;
      super.setLogType(1);
   }

   public DataLog(double height, double weight, Date logDate, int userId) {
      super(logDate, userId);
      logHeight = height;
      logWeight = weight;
      super.setLogType(1);
   }

   //Setters
   public void setLogHeight(double height) {
      try {
         if (height < 0.0) throw new InvalidAttributeValueException("Height cannot be negative.");
         this.logHeight = height;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setLogWeight(double weight) {
      try {
         if (weight < 0.0) throw new InvalidAttributeValueException("Weight cannot be negative.");
         this.logWeight = weight;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Getters
   public double getLogHeight() {return this.logHeight;}
   public double getLogWeight() {return this.logWeight;}

   //toString format: YY/MM/DD - Height, Weight
   public String toString() {
      return super.toString() + String.format(" - Height: %.2f, Weight: %.2f", logHeight, logWeight);
   }
}

//For logging individual meals
class MealLog extends Log {
   private String mealType; //Valid values: "Breakfast", "Lunch", "Dinner", "Snack"
   private List<Ingredient> ingredients;
   private String mealName;
   //Constructors
   public MealLog(int userId) {
      super(userId);
      mealType = "Breakfast";
      ingredients = new ArrayList<Ingredient>();
      super.setLogType(2);
   }

   public MealLog(String mealName, String mealType, Date logDate, int userId) {
      super(logDate, userId);
      try {
         if (!(mealType.equals("Breakfast")
                 || mealType.equals("Lunch")
                 || mealType.equals("Dinner")
                 || mealType.equals("Snack")))
            throw new InvalidAttributeValueException("Invalid mealType value.");

         this.mealType = mealType;
         this.mealName = mealName;
         ingredients = new ArrayList<Ingredient>();
         super.setLogType(2);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public MealLog(String mealName, Ingredient[] ingredients, String mealType, Date logDate, int userId) {
      this(mealName, mealType, logDate, userId);
      for (Ingredient i : ingredients)
         this.ingredients.add(i);
   }

   //Getters
   /**
    * Getter method for mealType.
    * @return the meal type associated with the log, null if the value of mealType is invalid.
    */
   public String getType() {return mealType;}
   public String getName() {return mealName;}
   public List<Ingredient> getIngredients() {return ingredients;}

   //Setters
   public void setType(String mealType) {
      try {
         if (!(mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner") || mealType.equals("Snack")))
            throw new InvalidAttributeValueException("Invalid mealType value.");
         this.mealType = mealType;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Add ingredients to the meal
   public void addIngredient(Ingredient in) {
      ingredients.add(in);
   }

   //Calculate the calorie, protein, fat and carb content of the meal
   public int calculateCalories() {
      return ingredients.stream().mapToInt(Ingredient::getCalories).sum(); //converts ingredients list to stream and gets calories of each ingredient
   }

   public int calculateProtein() {
      return ingredients.stream().mapToInt(Ingredient::getProtein).sum();
   }

   public int calculateFat() {
      return ingredients.stream().mapToInt(Ingredient::getFat).sum();
   }

   public int calculateCarbs() {
      return ingredients.stream().mapToInt(Ingredient::getCarbs).sum();
   }

   public int calculateOthers() {
      return ingredients.stream().mapToInt(Ingredient::getOthers).sum();
   }

   //toString format: YY/MM/DD - Meal Type - Calorie Intake
   @Override
   public String toString() {
      return super.toString() + String.format(" - %s - Calorie Intake: %d", mealType, calculateCalories());
   }

   public String getIngredientString() {
      String out = "";
      for (int i = 0 ; i < ingredients.size(); i++) {
         out += ingredients.get(i).toString() + "\n";
      }
      return out;
   }
}

//This class represents a single ingredient, and includes all of the macros in it (calories, fat, protein, etc...)
class Ingredient {
   //declare all vairables of an ingredient
   private String name;
   private int calories;
   private int fat;
   private int protein;
   private int carbs;
   private int others;
   private int serving;
   //likely will be more variables

   //Default constructor
   public Ingredient() {
      this.name = "";
      this.calories = 0;
      this.fat = 0;
      this.protein = 0;
      this.carbs = 0;
      this.others = 0;
      this.serving = 0;
   }

   //Constructor with all parameters
   public Ingredient(String name, int calories, int fat, int protein, int carbs, int others, int serving) {
      this.name = name;
      this.calories = calories;
      this.fat = fat;
      this.protein = protein;
      this.carbs = carbs;
      this.others = others;
      this.serving = serving;
   }

   //getters and setters below

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getCalories() {
      return calories;
   }

   public void setCalories(int calories) {
      this.calories = calories;
   }

   public void setFat(int fat) {
      this.fat = fat;
   }

   public int getFat() {
      return fat;
   }

   public void setProtein(int protein) {
      this.protein = protein;
   }

   public int getProtein() {
      return protein;
   }

   public void setCarbs(int carbs) {
      this.carbs = carbs;
   }

   public int getCarbs() {
      return carbs;
   }

   public void setOthers(int others) {
      this.others = others;
   }
   public int getOthers() {
      return others;
   }

   public void setServing(int serving) {
        this.serving = serving;
    }

    public int getServing(){
        return serving;
    }
   //will likely have additional variables

   @Override
   public String toString() {
      return "Ingredient{" + "name= " + name + '\'' + ", calories = " + calories + "cals, fat = " + fat + "g, protein = " + protein + "g, carbs = " + carbs + "g}";
   }

}

class ExerciseLog extends Log {
   private int caloBurnt;
   private String exerciseName;
   private double time;

   //Constructor
   public ExerciseLog(int userId) {
      super(userId);
      caloBurnt = 0;
      time = 0.0;
      super.setLogType(3);
   }

   public ExerciseLog(int caloBurnt, double time, Date logDate, int userId) {
      super(logDate, userId);
      this.caloBurnt = caloBurnt;
      this.time  = time;
      super.setLogType(3);
   }

   //Setters
   public void setCaloBurnt(int caloBurnt) {
      try {
         if (caloBurnt < 0) throw new InvalidAttributeValueException("The value of calorie burnt cannot be negative.");
         this.caloBurnt = caloBurnt;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setTime(double time) {
      try {
         if (time < 0.0) throw new InvalidAttributeValueException("Time spent exercising cannot be negative.");
         this.time = time;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Getters
   public int getCaloBurnt() {return this.caloBurnt;}
   public String getName() {return exerciseName;}
   public double getTime() {return this.time;}

   //toString format: Date - Time exercised - Calorie burnt
   public String toString() {
      return super.toString() + String.format(" - %.2f min - %d cal burnt", time, caloBurnt);
   }
}

@SuppressWarnings("deprecation")
//TODO: Implement storing and accessing database
public class Log {
   private int userId;
   private Date loggedDate;
   private int logType; //0 for basic, 1 for data, 2 for meal, 3 for exercise

   //Constructors
   public Log(int userId) {
      this(new Date(), userId);
      loggedDate.setYear(loggedDate.getYear() + 1900);
   }

   public Log(Date logDate, int userId) {
      loggedDate = logDate;
      this.userId = userId;
      logType = 0;
   }

   //Setters
   public void setDate(Date logDate) {
      this.loggedDate = logDate;
   }

   public void setLogType(int type) {
      this.logType = type;
   }

   //Getters
   public Date getDate() {
      return loggedDate;
   }

   public int getLogType() {
      return logType;
   }

   public int getUserID() {return userId;}

   //toString format: YY/MM/DD 
   @Override
   public String toString() {
      return String.format("%d/%2d/%2d", loggedDate.getYear(), loggedDate.getMonth() + 1, loggedDate.getDate());
   }
}
//   //TODO: Implement database queries and calorie/exercise intake logging
//   public static void main(String[] args) {
//      //DataLog
//      int dummyID = 0;
//      DataLog data = new DataLog(dummyID);
//      System.out.println(data.toString());
//      data.setLogHeight(170);
//      data.setLogWeight(80);
//      System.out.println(data.toString());
//
//      data = new DataLog(180, 70, new Date(2023, 9, 15), dummyID);
//      System.out.println(data.toString()+"\n");
//
//      //MealLog
//      // examples
//      Ingredient tomato = new Ingredient("Tomato", 20, 0, 1, 6, 0);
//      Ingredient bread = new Ingredient("Bread", 255, 3, 8, 51, 0);
//      Ingredient egg = new Ingredient("Egg", 145, 11, 14, 2, 0);
//
//      // create Meal object
//      MealLog breakfast = new MealLog(dummyID);
//      breakfast.setType("Breakfast");
//      breakfast.setDate(new Date(2023, 9, 14));
//      breakfast.addIngredient(tomato);
//      breakfast.addIngredient(bread);
//      breakfast.addIngredient(egg);
//
//      // display all meals logged in the MealLogger
//      System.out.println(breakfast.toString());
//      System.out.println(breakfast.getIngredientString());
//
//      //ExerciseLog
//      ExerciseLog exercise = new ExerciseLog(dummyID);
//      System.out.println(exercise.toString());
//      exercise.setCaloBurnt(420);
//      exercise.setTime(30);
//      System.out.println(exercise.toString());
//
//      exercise = new ExerciseLog(600, 120, new Date(2023, 9, 19), dummyID);
//      System.out.println(exercise.toString()+"\n");
//   }
//}
