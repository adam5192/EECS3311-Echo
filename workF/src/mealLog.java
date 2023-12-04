
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

    public int calculateOthers() {
        return ingredients.stream().mapToInt(Ingredient::getOthers).sum();
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

    public void removeMeal(Meal meal) {
        meals.remove(meal);
    }

    //return logged meals
    public List<Meal> getMeals() {
        return meals;
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
