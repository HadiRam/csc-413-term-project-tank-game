package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class AiTankSlowMover extends Tank{


    public AiTankSlowMover(String id, double x, double y, double angle, double width, double height){
        super(id, x, y, angle, width, height);
        shellCooldown = 20;
    }

    @Override
    public void move(GameWorld gameWorld){
        checkBounds(gameWorld);

        //AI logic to make sure the ai tank always faces the player tank
        Entity PlayerTank = gameWorld.getEntity(Constants.PLAYER_TANK_ID);
        double dx = PlayerTank.getX() - getX();
        double dy = PlayerTank.getY() - getY();
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -=
                Math.floor(angleDifference / Math.toRadians(360.0) + 0.5)
                        * Math.toRadians(360.0);
        if (angleDifference < -Math.toRadians(3.0)) {
            turnRight(Constants.TANK_TURN_SPEED);
        } else if (angleDifference > Math.toRadians(3.0)) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }

        //Logic to make the tank follow the player around every



        //This gives the tank a unique movement style by "inching forward"
        //using a small cool-down period
        if(MOVEMENT_COOLDOWN > 0){
            MOVEMENT_COOLDOWN--;
        }if(MOVEMENT_COOLDOWN <= 0){
            MOVEMENT_COOLDOWN = 50;
        }

        //allows the ai tank to fire a shell about every movement it makes
        if (MOVEMENT_COOLDOWN < 10) {
            shellCooldown -= 2;
            moveForward(Constants.TANK_MOVEMENT_SPEED);
            fireShell(gameWorld);

        }


    }


    private void fireShell(GameWorld gameWorld){
        if (shellCooldown == 0) {
            Shell newShell = new Shell(getShellX(), getShellY(), getAngle(), Constants.SHELL_WIDTH, Constants.SHELL_HEIGHT);
            gameWorld.addEntity(newShell);
            shellCooldown = 20;
        }
    }



}
