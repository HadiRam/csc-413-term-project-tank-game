package edu.csc413.tankgame.model;

public class Wall extends Entity{
    // this class will need an id, an x, y, blank move method, deal with checking balance,
    // deal with collision detection
    protected int wallHP = 3;

    public Wall(String id, double x, double y, double angle, double width, double height) {
        super(id, x, y, angle, width,height);
    }

    public boolean checkWallHP(){
        if(wallHP == 0){
            return true;
        }
        return false;
    }

    public void lowerWallHP(){
        wallHP--;
    }


    //Walls don't need to move.
    @Override
    public void move(GameWorld gameWorld) {

    }
    @Override
    public void checkBounds(GameWorld gameWorld) {

    }
}
