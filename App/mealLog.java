package App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class will represent a single meal. It contains data such as data, type of meal(breakfast, lunch, dinner)
//and the ingredients in the meal.
class Meal {
    private String date; //holds date that meal was eaten
    private String mealType; // Breakfast, Lunch, Dinner, Snack
    private List<Ingredient> ingredients;

    public Meal() {
        this.ingredients = new ArrayList<>();
    }

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

    //Setters below
    public void setType(String mealType) {
        this.mealType = mealType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //add ingredient to the list
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    //Override toString method to represent meal information in readable format
    @Override
    public String toString() {
        return "Meal{" + "date='" + date + '\'' + ", meal type='" + mealType + '\'' + ", ingredients=" + ingredients + "}";
    }

    public String getMealType() {
        return this.mealType;
    }

    public String getDate() {
        return this.date;
    }

    //will likely have additional variables
}

//This class represents a single ingredient, and includes all of the macros in it (calories, fat, protein, etc...)
class Ingredient {
    //declare all vairables of an ingredient
    private String name;
    private int calories;
    private int fat;
    private int protein;
    private int carbs;
    private int grams;
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

    //will likely have additional variables

    @Override
    public String toString() {
        return "Ingredient{" + "name= " + name + '\'' + ", calories = " + calories + ", fat = " + fat + ", protein = " + protein + ", carbs = " + carbs + '}';
    }

}

//This class keeps track of multiple meals
class MealLogger {
    private List<Meal> meals; //this list holds each meal

    public MealLogger() {
        meals = new ArrayList<>();
    }

    //add meal to list
    public void logMeal(Meal meal) {
        meals.add(meal);
    }

    //return logged meals
    public List<Meal> getMeals() {
        return meals;
    }
}

// TODO: Implement database helper

//temporary test cases for running without database
//public class mealLog {
//    public static void main(String[] args) {
//        // examples
//        Ingredient tomato = new Ingredient("Tomato", 20, 0, 1, 6, 100);
//        Ingredient bread = new Ingredient("Bread", 255, 3, 8, 51, 100);
//        Ingredient egg = new Ingredient("Egg", 145, 11, 14, 2, 100);
//
//        // create Meal object
//        Meal breakfast = new Meal();
//        breakfast.setType("Breakfast");
//        breakfast.setDate("10/14/2023");
//        breakfast.addIngredient(tomato);
//        breakfast.addIngredient(bread);
//        breakfast.addIngredient(egg);
//
//        System.out.println("Total calories: " + breakfast.calculateCalories());
//        System.out.println("Total protein: " + breakfast.calculateProtein());
//        System.out.println("Total fat: " + breakfast.calculateFat());
//        System.out.println("Total carbs: " + breakfast.calculateCarbs());
//
//        // create meal logger object and add breakfast to it
//        MealLogger myMealLogger = new MealLogger();
//        myMealLogger.logMeal(breakfast);
//
//        // display all meals logged in the MealLogger
//        System.out.println("All logged meals: " + myMealLogger.getMeals());
//    }
//}
