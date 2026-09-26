import java.util.Scanner;

class UserProfile {
    private String usn;
    private int Age;
    private String Sex;
    private double kg;
    private double height;
    public UserProfile(String usn, int Age, String Sex, double kg, double height) {
        this.usn = usn;
        this.Age = Age;
        this.Sex = Sex;
        this.kg = kg;
        this.height = height;
    }
    public double BMR() {
        if (Sex.equalsIgnoreCase("male")) {
            return (10 * kg) + (6.25 * height) - (5 * Age) + 5;
        } else {
            return (10 * kg) + (6.25 * height) - (5 * Age) - 161;
        }
    }
    public double TDEE() {
        return BMR() * 1.55;
    }
}

class Meal {
    private String usn;
    private double calories;
    public Meal(String usn, double calories) {
        this.usn = usn;
        this.calories = calories;
    }
    public String getusn() {
        return usn;
    }
    public double kcal() {
        return calories;
    }
    public String getType() {
        return "Meal";
    }
}

class FoodMeal extends Meal {
    public FoodMeal(String usn, double calories) {
        super(usn, calories);
    }
    public String getType() {
        return "Food";
    }
}

class DrinkMeal extends Meal {
    public DrinkMeal(String usn, double calories) {
        super(usn, calories);
    }
    public String getType() {
        return "Drink";
    }
}

class CalorieTracker {
    private Meal[] meals;
    private int mealCount;
    public CalorieTracker(int size) {
        meals = new Meal[size];
        mealCount = 0;
    }
    public boolean addMeal(Meal meal) {
        if (mealCount < meals.length) {
            meals[mealCount] = meal;
            mealCount++;
            return true;
        }
        return false;
    }
    public double kcalupd() {
        double total = 0;
        for (int i = 0; i < mealCount; i++) {
            total += meals[i].kcal();
        }
        return total;
    }
    public void displayMeals() {
        if (mealCount == 0) {
            System.out.println("No meals logged yet.");
            return;
        }
        for (int i = 0; i < mealCount; i++) {
            System.out.printf(
                "(%d) %s || Type : %s || Calories : %.0f kcal ||%n",
                i + 1,
                meals[i].getusn(),
                meals[i].getType(),
                meals[i].kcal()
            );
        }
    }
}

public class TDEEApp {
    static Scanner input = new Scanner(System.in);
    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    static void waitForEnter() {
        System.out.println();
        System.out.print("Press ENTER to continue...");
        input.nextLine();
    }
    static void loading(String messAge, int seconds) {
        long endTime = System.currentTimeMillis() + (seconds * 1000L);
        int dots = 0;
        while (System.currentTimeMillis() < endTime) {
            dots++;
            if (dots > 3) {
                dots = 1;
            }
            String loadingDots = "";
            for (int i = 0; i < dots; i++) {
                loadingDots += ".";
            }
            System.out.print("\r" + messAge + loadingDots + "   ");
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.print("\r" + messAge + "... DONE!\n");
    }
    static void showHeader(String title) {
        System.out.println("||||||| " + title + " |||||||");
        System.out.println("===============================");
    }
    public static void main(String[] args) {
        clearScreen();
        showHeader("ARCH APP");
        System.out.println("The Number #1 Java Fitness Meal Logger");
        System.out.println();
        System.out.print("START? (y/n): ");
        String start = input.nextLine();
        if (!start.equalsIgnoreCase("y")) {
            clearScreen();
            System.out.println("Goodbue!");
            return;
        }
        clearScreen();
        loading("LOADING ARCH APP", 3);
        clearScreen();
        showHeader("CREATE PROFILE");
        System.out.println("First, let's set up your profile.");
        System.out.println();
        System.out.print("usn : ");
        String usn = input.nextLine();
        System.out.print("Age : ");
        int Age = Integer.parseInt(input.nextLine());
        System.out.print("Sex : ");
        String Sex = input.nextLine();
        System.out.print("KG : ");
        double kg = Double.parseDouble(input.nextLine());
        System.out.print("HEIGHT IN CM : ");
        double height = Double.parseDouble(input.nextLine());
        clearScreen();
        loading("CREATING ACCOUNT", 4);
        UserProfile user = new UserProfile(
            usn,
            Age,
            Sex,
            kg,
            height
        );
        double tdee = user.TDEE();
        CalorieTracker tracker = new CalorieTracker(50);
        boolean running = true;
        while (running) {
            clearScreen();
            showHeader("ARCH APP");
            double consumed = tracker.kcalupd();
            double remaining = tdee - consumed;
            System.out.printf(
                "|| TDEE: %.0f kcal || REMAINING KCAL: %.0f ||%n",
                tdee,
                remaining
            );
            System.out.println();
            System.out.println("(1) Log a Meal");
            System.out.println("(2) Meal History");
            System.out.println("(3) Exit");
            System.out.println();
            System.out.print("ENTER CODE: ");
            String choice = input.nextLine();
            if (choice.equals("1")) {
                clearScreen();
                showHeader("LOG A MEAL");
                consumed = tracker.kcalupd();
                remaining = tdee - consumed;
                System.out.printf(
                    "|| TDEE: %.0f kcal || REMAINING KCAL: %.0f ||%n",
                    tdee,
                    remaining
                );
                System.out.println();
                System.out.print("Meal usn : ");
                String mealusn = input.nextLine();
                System.out.print("Calories in kcal : ");
                double calories = Double.parseDouble(input.nextLine());
                System.out.print("Type (1 = Food, 2 = Drink): ");
                int type = Integer.parseInt(input.nextLine());
                double newTotal = consumed + calories;
                if (newTotal > tdee) {
                    System.out.println();
                    System.out.print(
                        "MEAL WILL EXCEED TDEE, Are you sure to log? (y/n): "
                    );
                    String confirm = input.nextLine();
                    if (!confirm.equalsIgnoreCase("y")) {
                        System.out.println("Meal was not logged.");
                        waitForEnter();
                        continue;
                    }
                }
                clearScreen();
                loading("LOGGING MEAL", 3);
                Meal meal;
                if (type == 1) {
                    meal = new FoodMeal(mealusn, calories);
                } else {
                    meal = new DrinkMeal(mealusn, calories);
                }
                if (tracker.addMeal(meal)) {
                    System.out.println("MEAL SUCCESSFULLY LOGGED");
                } else {
                    System.out.println("MEAL LOG IS FULL.");
                }
                waitForEnter();
            } else if (choice.equals("2")) {
                clearScreen();
                showHeader("MEAL HISTORY");
                consumed = tracker.kcalupd();
                remaining = tdee - consumed;
                System.out.printf(
                    "|| TDEE: %.0f kcal || REMAINING KCAL: %.0f ||%n",
                    tdee,
                    remaining
                );
                System.out.println();
                tracker.displayMeals();
                System.out.println();
                System.out.print("ENTER \"e\" TO EXIT: ");
                String exit = input.nextLine();
                while (!exit.equalsIgnoreCase("e")) {
                    System.out.print("ENTER \"e\" TO EXIT: ");
                    exit = input.nextLine();
                }
            } else if (choice.equals("3")) {
                clearScreen();
                showHeader("ARCH APP");
                System.out.println("THANK YOU FOR USING ARCH APP");
                running = false;
            } else {
                System.out.println();
                System.out.println("INVALID CODE.");
                waitForEnter();
            }
        }
        input.close();
    }
}