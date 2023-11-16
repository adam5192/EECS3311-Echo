package App;
import java.util.ArrayList;
import java.util.List;

class Exercise  {
    private String date; // date of exercise
    private String time;
    private String type; // walking, running, etc...
    private int duration; // in minutes
    private String intensity; // low, medium, high, very high

    // constructor to initialize exercise data
    public Exercise(String date, String time, String type, int duration, String intensity) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.duration = duration;
        this.intensity = intensity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public String getIntensity() {
        return intensity;
    }

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    // this method calculates calories burnt
    public int calculateCaloriesBurnt(double BMR) {
        double factor = switch (intensity) {
            case "low" -> 0.5;
            case "medium" -> 0.7;
            case "high" -> 0.9;
            case "very high" -> 1.2;
            default -> 0;
        };
        return (int) (BMR * factor * (duration / 60.0)); // calories burned = BMR * duration (hours) * factor
    }

    @Override
    public String toString() {
        return "Exercise{" + "date='" + date + '\'' + ", time='" + time + '\'' + ", type='" + type + '\'' + ", duration=" + duration + ", intensity='" + intensity + '\'' + "}";
    }
}

class ExerciseLogger {
    private List<Exercise> exercises; // List to store the exercises.

    // initialize exercise logger
    public ExerciseLogger() {
        exercises = new ArrayList<>();
    }

    // add an exercise to the list
    public void logExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    // get list of all exercises
    public List<Exercise> getExercises() {
        return exercises;
    }
}

// TODO: Implement database helper

//public class exerciseLog {
//    public static void main(String[] args) {
//        // sample bmr, this will be retrieved from user profile
//        double userBMR = 2000;
//
//        // create an example exercise
//        Exercise morningJog = new Exercise("2023-10-19", "09:30", "jogging", 25, "medium");
//
//        // Display the estimated calories burnt during the walk.
//        System.out.println("Calories burnt: " + morningJog.calculateCaloriesBurnt(userBMR));
//
//        // create and exercise logger that logs all exercises
//        ExerciseLogger myExerciseLogger = new ExerciseLogger();
//
//        // add morningJog to logger
//        myExerciseLogger.logExercise(morningJog);
//
//        // displays all logged exercises
//        System.out.println("logged exercises: " + myExerciseLogger.getExercises());
//    }
//}