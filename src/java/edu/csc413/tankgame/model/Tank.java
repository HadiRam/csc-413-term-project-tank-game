package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public abstract class Tank extends Entity{
    protected static int INITIAL_SHELL_COOLDOWN = 38;
    protected static int MOVEMENT_COOLDOWN = 20;
    protected static double MOVEMENT_MULTIPLIER = 1;
    protected int shellCooldown;
    protected int tankHP = 3;

    public Tank(String id, double x, double y, double angle, double width, double height){
        super(id, x, y, angle, width, height);

    }

    public void lowerShellCooldown(){
        INITIAL_SHELL_COOLDOWN = (INITIAL_SHELL_COOLDOWN/2);
    }



    public void increaseMovementSpeed(){
        MOVEMENT_MULTIPLIER = 1.50;}

    public  void increaseTankHP(){
        tankHP += 1;
    }

    public boolean checkTankHP(){
        if(tankHP == 0){
            return true;
        }
        return false;
    }

    public void lowerTankHP(){
        tankHP--;
    }

    @Override
    public void checkBounds(GameWorld gameWorld){
        if(getX() < Constants.TANK_X_LOWER_BOUND){
             setXTankLowerBound();
        }

        if(getX() > Constants.TANK_X_UPPER_BOUND){
             setXTankUpperBound();
        }

        if(getY() < Constants.TANK_Y_LOWER_BOUND){
            setYTankLowerBound();
        }

        if(getY() > Constants.TANK_Y_UPPER_BOUND){
            setYTankUpperBound();
        }
    }

    //Starter code used to find the offset of the shell.
    protected double getShellX() {
        return getX() + Constants.TANK_WIDTH / 2 + 45.0 * Math.cos(getAngle()) - Constants.SHELL_WIDTH / 2;
    }

    protected double getShellY() {
        return getY() + Constants.TANK_HEIGHT / 2 + 45.0 * Math.sin(getAngle()) - Constants.SHELL_HEIGHT / 2;
    }


}
