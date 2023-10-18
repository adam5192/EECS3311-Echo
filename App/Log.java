package EECS3311_Project.App;
import java.util.*;
import javax.management.InvalidAttributeValueException;

//TODO: Implement storing and accessing database
public class Log {
   private int userId;
   private Date loggedDate;
   private int logType; //0 for basic, 1 for data, 2 for meal, 3 for exercise

   //Constructors
   public Log(int userId) {
      this(null, userId);
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

   public int getLogType() {return logType;}

   //toString format: YY/MM/DD 
   @Override
   public String toString() {
      //TODO: Deprecated date-time object, change implementation
      return String.format("%d/%2d/%2d", loggedDate.getYear(), loggedDate.getMonth(), loggedDate.getDate()); 
   }

   //TODO: Implement database queries and calorie/exercise intake logging
   public static void main(String[] args) {
        // examples
        Ingredient tomato = new Ingredient("Tomato", 20, 0, 1, 6);
        Ingredient bread = new Ingredient("Bread", 255, 3, 8, 51);
        Ingredient egg = new Ingredient("Egg", 145, 11, 14, 2);

        // create Meal object
        int dummyID = 0;
        MealLog breakfast = new MealLog(dummyID);
        breakfast.setType("Breakfast");
        breakfast.setDate(new Date(2023, 10, 14));
        breakfast.addIngredient(tomato);
        breakfast.addIngredient(bread);
        breakfast.addIngredient(egg);

        System.out.println("Total calories: " + breakfast.calculateCalories());
        System.out.println("Total protein: " + breakfast.calculateProtein());
        System.out.println("Total fat: " + breakfast.calculateFat());
        System.out.println("Total carbs: " + breakfast.calculateCarbs());

        // create meal logger object and add breakfast to it
        MealLogger myMealLogger = new MealLogger();
        myMealLogger.logMeal(breakfast);

        // display all meals logged in the MealLogger
        System.out.println("All logged meals: " + myMealLogger.getMeals());
   }
}

//For logging height and weight changes.
class DataLog extends Log {
   private double loggedHeight;
   private double loggedWeight;

   //Constructors
   public DataLog(int userId) {
      super(userId);
      loggedHeight = 0.0;
      loggedWeight = 0.0;
      super.setLogType(1);
   }

   public DataLog(double height, double weight, Date logDate, int userId) {
      super(logDate, userId);
      loggedHeight = height;
      loggedWeight = weight;
      super.setLogType(1);
   }

   //Getters
   public double getLoggedHeight() {return this.loggedHeight;}
   public double getLoggedWeight() {return this.loggedWeight;}
   
   //Might not need setters

   //toString format: YY/MM/DD - Height, Weight
   public String toString() {
      return super.toString() + String.format(" - Height: %.2f, Weight: %.2f", loggedHeight, loggedWeight);
   }
}

//For testing output of mealLog (to be moved to Profile as a function of querying)
class MealLogger {
    private List<MealLog> meals; //this list holds each meal

    public MealLogger() {
        meals = new ArrayList<>();
    }

    //add meal to list
    public void logMeal(MealLog meal) {
        meals.add(meal);
    }

    //return logged meals
    public List<MealLog> getMeals() {
        return meals;
    }
}

//For logging individual meals
class MealLog extends Log {
   private int caloIn;   //Calorie intake
   private String mealType; //Valid values: "Breakfast", "Lunch", "Dinner", "Snack"
   private List<Ingredient> ingredients;

   //Constructors
   public MealLog(int userId) {
      super(userId);
      caloIn = 0;
      mealType = "Breakfast";
      super.setLogType(2);
   }

   public MealLog(int caloIn, String mealType, Date logDate, int userId) {
      super(logDate, userId);
      try {
         if (caloIn < 0) 
            throw new InvalidAttributeValueException("Value of calorie intake cannot be negative.");
         else if (!(mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner") || mealType.equals("Snack")))
            throw new InvalidAttributeValueException("Invalid mealType value.");

         this.caloIn = caloIn;
         this.mealType = mealType;
         super.setLogType(2);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Getters
   public int getCaloIn() {return this.caloIn;}
   /**
    * Getter method for mealType.
    * @return the meal type associated with the log, null if the value of mealType is invalid.
    */
   public String getType() {return mealType;}

   //Setters
   public void setCaloIn(int newIn) {
      try {
         if (newIn < 0) throw new InvalidAttributeValueException("Calorie intake cannot be negative.");
         caloIn = newIn;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void setType(String mealType) {
      try {
         if (!(mealType.equals("Breakfast") || mealType.equals("Lunch") || mealType.equals("Dinner"))) 
            throw new InvalidAttributeValueException("Invalid mealType value.");
         this.mealType = mealType;
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   //Add intake into the log with matching date
   public static void setCaloIn(Date logDate, int caloIn, int userId) {
      //TODO: Implement find specified log in database using logDate.
      MealLog temp = new MealLog(userId);
      try {
         if (caloIn < 0) throw new InvalidAttributeValueException("Calorie intake cannot be negative.");
         temp.caloIn += caloIn;
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

   //toString format: YY/MM/DD - Meal Type - Amount
   public String toString() {
      return super.toString() + String.format(" - %s - Calorie Intake: %d", mealType, caloIn);
   }
}

class ExerciseLog extends Log {
   //TODO: Implement to match Calculator
   private int caloBurnt;
   private double time;

   //Constructor
   public ExerciseLog(int userId) {
      super(userId);
      caloBurnt = 0;
      time = 0.0;
      super.setLogType(3);
   }

   public ExerciseLog(int caloBurnt, double time,Date logDate, int userId) {
      super(logDate, userId);
      this.caloBurnt = caloBurnt;
      this.time  = time;
      try {
         if (caloBurnt < 0) 
            throw new InvalidAttributeValueException("Value of calorie burnt cannot be negative.");
         else if (time < 0.0)
            throw new InvalidAttributeValueException("Value of time cannot be negative.");

         this.caloBurnt = caloBurnt;
         this.time = time;
         super.setLogType(3);
      } catch (Exception e) {
         e.printStackTrace();
      }
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
   public double getTime() {return this.time;}

   //toString format: Date - Time exercised - Calorie burnt
   public String toString() {
      return super.toString() + String.format(" - %.2f min - %d cal burnt", time, caloBurnt);
   }
}

//This class represents a single ingredient, and includes all of the macros in it (calories, fat, protein, etc...)
class Ingredient {
    //declare all variables of an ingredient
    private String name;
    private int calories;
    private int fat;
    private int protein;
    private int carbs;
    //likely will be more variables
    
    //Default constructor
    public Ingredient() {
        this.name = "";
        this.calories = 0;
        this.fat = 0;
        this.protein = 0;
        this.carbs = 0;
    }

    //Constructor with all parameters
    public Ingredient(String name, int calories, int fat, int protein, int carbs) {
        this.name = name;
        this.calories = calories;
        this.fat = fat;
        this.protein = protein;
        this.carbs = carbs;
    }

    //Getters and setters below

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

    //will likely have additional variables

    @Override
    public String toString() {
        return "Ingredient{" + "name= " + name + '\'' + ", calories = " + calories + ", fat = " + fat + ", protein = " + protein + ", carbs = " + carbs + '}';
    }

}
