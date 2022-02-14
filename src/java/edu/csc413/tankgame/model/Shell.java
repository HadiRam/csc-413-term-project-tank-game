package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.view.RunGameView;

public class Shell extends Entity{

    public Shell (double x, double y, double angle,double width, double height){
        super(generateId(), x, y, angle, width, height);

    }


    private static int nextId = 0;

    private static String generateId(){
        String id =  "shell-" + nextId;
        nextId++;
        return id;
    }

    @Override
    public void move(GameWorld gameWorld) {
        moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }

    @Override
    public void checkBounds(GameWorld gameWorld) {
        //check if shell is out of bounds.
        //if it is, remove it from game world.
        // will run into same problem with "entitiesToAdd".

      if(getX() < Constants.SHELL_X_LOWER_BOUND){
            gameWorld.addEntitityToRemovalList(gameWorld.getEntity(getId())); // adding to seperate list, then removed via gameworld class
        }

        if(getX() > Constants.SHELL_X_UPPER_BOUND){
            gameWorld.addEntitityToRemovalList(gameWorld.getEntity(getId()));
        }

        if(getY() < Constants.SHELL_Y_LOWER_BOUND){
            gameWorld.addEntitityToRemovalList(gameWorld.getEntity(getId()));
        }

        if(getY() > Constants.SHELL_Y_UPPER_BOUND){
            gameWorld.addEntitityToRemovalList(gameWorld.getEntity(getId()));
        }

    }
}
