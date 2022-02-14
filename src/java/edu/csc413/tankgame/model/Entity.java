package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

/**
 * A general concept for an entity in the Tank Game. This includes everything that can move or be interacted with, such
 * as tanks, shells, walls, power ups, etc.
 */
public abstract class Entity {
    private String id;
    private double x;
    private double y;
    private double angle;
    private double width;
    private double height;

    public Entity(String id, double x, double y, double angle, double width, double height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.width = width;
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX){
        x = newX;
    }

    public void setY(double newY){
        y = newY;
    }

    //set method for boundries
    public void setXTankLowerBound(){
        x = Constants.TANK_X_LOWER_BOUND;
    }
    public void setXTankUpperBound(){
        x = Constants.TANK_X_UPPER_BOUND;
    }
    public void setYTankLowerBound(){
        y = Constants.TANK_Y_LOWER_BOUND;
    }
    public void setYTankUpperBound(){
        y = Constants.TANK_Y_UPPER_BOUND;
    }



    //Getting x bound and y bound for a entity (x bound means right end of entity, y bound means lower end of entity)
    public double getXBound() {
        return (x + width);
    }

    public double getYBound() { return (y + height); }


    public double getAngle() {
        return angle;
    }

    protected void moveForward(double movementSpeed) {
        x += movementSpeed * Math.cos(angle);
        y += movementSpeed * Math.sin(angle);
    }

    protected void moveBackward(double movementSpeed) {
        x -= movementSpeed * Math.cos(angle);
        y -= movementSpeed * Math.sin(angle);
    }

    protected void turnLeft(double turnSpeed) {
        angle -= turnSpeed;
    }

    protected void turnRight(double turnSpeed) {
        angle += turnSpeed;
    }

    /** All entities can move, even if the details of their move logic may vary based on the specific type of Entity. */
    public abstract void move(GameWorld gameWorld);
    public abstract void checkBounds(GameWorld gameWorld);
}
