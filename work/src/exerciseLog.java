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
        return (int) ((BMR/24) * factor * (duration));
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
