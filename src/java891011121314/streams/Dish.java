package java891011121314.streams;

import java.util.List;

public class Dish {

    public enum Type  { Vegetarian, Vegan , Fish , Meat}

    Type type;

    int calories;

    String name;

    private Dish(Type type, int calories, String name) {
        this.type = type;
        this.calories = calories;
        this.name = name;
    }

    public static List<Dish> menu()
    {
        Dish dish1 = new Dish(Type.Vegan,100,"dish1");

        Dish dish2 = new Dish(Type.Vegetarian,200,"dish2");
        Dish dish3 = new Dish(Type.Fish,300,"dish3");
        Dish dish4 = new Dish(Type.Meat,400,"dish4");
        Dish dish5 = new Dish(Type.Vegetarian,500,"dish5");


       return  List.of(dish1,dish2,dish3,dish4,dish5);

    }


    public Type getType()
    {
        return type;
    }

    public int getCalories()
    {
        return calories;
    }


    public String getName()
    {
        return name;
    }


    @Override
    public String toString() {
        return "Dish{" +
                "type=" + type +
                ", calories=" + calories +
                ", name='" + name + '\'' +
                '}';
    }
}
