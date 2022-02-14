package edu.csc413.tankgame.model;

public class HealthPotion extends Entity{
    public static double WIDTH = 43.0;
    public static double HEIGHT = 40.0;
    public static double HealthPotion_X = 50;
    public static double HealthPotion_Y = 100;

    public HealthPotion(String id, double x, double y, double angle, double width, double height) {
        super(id, x, y, angle=0.0, width=WIDTH, height=HEIGHT);
    }

    @Override
    public void move(GameWorld gameWorld) {

    }

    @Override
    public void checkBounds(GameWorld gameWorld) {

    }
}
