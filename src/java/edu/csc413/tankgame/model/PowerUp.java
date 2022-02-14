package edu.csc413.tankgame.model;

public abstract class PowerUp extends Entity{
    public static double POWERUP_WIDTH = 20;
    public static double POWERUP_HEIGHT = 50;
    public static double POWERUP1_X = 500.0;
    public static double POWERUP1_Y = 600;
    public static double POWERUP2_X = 500;
    public static double POWERUP2_Y = 100;


    public PowerUp(String id, double x, double y, double angle, double width, double height) {
        super(id, x, y, angle=0.0, width=POWERUP_WIDTH, height=POWERUP_HEIGHT);
    }



}
