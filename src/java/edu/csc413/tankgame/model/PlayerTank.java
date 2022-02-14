package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;

public class PlayerTank extends Tank{


    public PlayerTank(String id, double x, double y, double angle, double width, double height){
        super(id, x, y, angle, width, height);
        shellCooldown = INITIAL_SHELL_COOLDOWN;
    }



    //move method for a player tank using kb inputs
    public void move(GameWorld gameWorld){

        checkBounds(gameWorld);

        if(KeyboardReader.instance().upPressed()){
            moveForward(Constants.TANK_MOVEMENT_SPEED * MOVEMENT_MULTIPLIER);
        }
        if(KeyboardReader.instance().downPressed()){
            moveBackward(Constants.TANK_MOVEMENT_SPEED * MOVEMENT_MULTIPLIER);
        }
        if(KeyboardReader.instance().leftPressed()){
            turnLeft(Constants.TANK_TURN_SPEED);
        }
        if(KeyboardReader.instance().rightPressed()){
            turnRight(Constants.TANK_TURN_SPEED);
        }

        if(shellCooldown > 0) {
            shellCooldown--;
        }

        if(KeyboardReader.instance().spacePressed()) {
            fireShell(gameWorld);
        }

    }


    //firing shell method for a player tank
    private void fireShell(GameWorld gameWorld){
        if (shellCooldown == 0) {
            Shell newShell = new Shell(getShellX(), getShellY(), getAngle(), Constants.SHELL_WIDTH, Constants.SHELL_HEIGHT);
            gameWorld.addEntity(newShell);
            shellCooldown = INITIAL_SHELL_COOLDOWN;
        }
    }



}
