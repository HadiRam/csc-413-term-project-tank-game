package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import java.awt.event.ActionEvent;
import java.util.List;

public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld gameWorld;



    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameWorld = new GameWorld();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }

            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();

        };
        new Thread(gameRunner).start();


    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {

        //Model aspect - creating tanks for player and ai
        Tank tank = new PlayerTank(
                Constants.PLAYER_TANK_ID,
                Constants.PLAYER_TANK_INITIAL_X,
                Constants.PLAYER_TANK_INITIAL_Y,
                Constants.PLAYER_TANK_INITIAL_ANGLE,
                Constants.TANK_WIDTH,
                Constants.TANK_HEIGHT);
        Tank aiTank = new AiTankSlowMover(Constants.AI_TANK_1_ID,
                Constants.AI_TANK_1_INITIAL_X,
                Constants.AI_TANK_1_INITIAL_Y,
                Constants.AI_TANK_1_INITIAL_ANGLE,
                Constants.TANK_WIDTH,
                Constants.TANK_HEIGHT);

        Tank aiTank2 = new AiTankFastMover(Constants.AI_TANK_2_ID,
                Constants.AI_TANK_2_INITIAL_X,
                Constants.AI_TANK_2_INITIAL_Y,
                Constants.AI_TANK_2_INITIAL_ANGLE,
                Constants.TANK_WIDTH,
                Constants.TANK_HEIGHT);
        PowerUp attackSpeedBoost = new AttackSpeedBoost("Attack-Speed-Boost",
                PowerUp.POWERUP1_X,
                PowerUp.POWERUP1_Y,
                0.0,
                PowerUp.POWERUP_WIDTH,
                PowerUp.POWERUP_HEIGHT
        );
        PowerUp SpeedBoost = new SpeedBoost("Speed-Boost",
                PowerUp.POWERUP2_X,
                PowerUp.POWERUP2_Y,
                0.0,
                PowerUp.POWERUP_WIDTH,
                PowerUp.POWERUP_HEIGHT
        );
        HealthPotion healthPot = new HealthPotion("Health-Potion",
                HealthPotion.HealthPotion_X,
                HealthPotion.HealthPotion_Y,
                0.0,
                HealthPotion.WIDTH,
                HealthPotion.HEIGHT
        );


        //walls
        List<WallInformation> wallInfos = WallInformation.readWalls();
        int id = 0;
        for (WallInformation wallInfo : wallInfos){
            //create wall entity and add to gameworld
            Entity wall= new Wall("wall-" + id, wallInfo.getX(), wallInfo.getY(), 0.0,Constants.WALL_WIDTH,Constants.WALL_HEIGHT);
            runGameView.addSprite("wall-" + id++,
                    wallInfo.getImageFile(),
                    wallInfo.getX(),wallInfo.getY(),
                    0.0);
            gameWorld.addEntity(wall);

        }

        gameWorld.addEntity(healthPot);
        gameWorld.addEntity(SpeedBoost);
        gameWorld.addEntity(attackSpeedBoost);
        gameWorld.addEntity(tank);
        gameWorld.addEntity(aiTank);
        gameWorld.addEntity(aiTank2);
        //move entities from "entitiesToAdd" list to entity list
        gameWorld.moveEntitiesToAdd();


        //view aspect adds the tanks/shells/powerups to screen.
        runGameView.addSprite(tank.getId(),
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                tank.getX(),
                tank.getY(),
                tank.getAngle());
        runGameView.addSprite(aiTank.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(),
                aiTank.getY(),
                aiTank.getAngle());
        runGameView.addSprite(aiTank2.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getX(),
                aiTank2.getY(),
                aiTank2.getAngle());
        runGameView.addSprite(attackSpeedBoost.getId(),
                RunGameView.POWERUP_IMAGE_FILE,
                attackSpeedBoost.getX(),
                attackSpeedBoost.getY(),
                attackSpeedBoost.getAngle());
        runGameView.addSprite(SpeedBoost.getId(),
                RunGameView.POWERUP_IMAGE_FILE,
                attackSpeedBoost.getX(),
                attackSpeedBoost.getY(),
                attackSpeedBoost.getAngle());
        runGameView.addSprite(healthPot.getId(),
                RunGameView.HEALTH_IMAGE_FILE,
                healthPot.getX(),
                healthPot.getY(),
                healthPot.getAngle());


    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame() {
        for(Entity entity : gameWorld.getEntities()){
            entity.move(gameWorld);
            entity.checkBounds(gameWorld);

        }

        // Loop for collision detection. Must avoid self collisions and repeat collisions.

        for(int i = 0; i < gameWorld.getEntities().size(); i++){
            for(int j = i+1; j < gameWorld.getEntities().size(); j++){
                Entity entityA = gameWorld.getEntities().get(i);
                Entity entityB = gameWorld.getEntities().get(j);
                if(areEntitiesColliding(entityA,entityB)){
                    handleCollision(entityA,entityB);
                }
            }
        }

        //recognize possibility for new shells/power ups

        for(Entity entity: gameWorld.getEntitiesToAdd()){
            runGameView.addSprite(
                    entity.getId(),
                    RunGameView.SHELL_IMAGE_FILE,
                    entity.getX(),
                    entity.getY(),
                    entity.getAngle());
        }
        gameWorld.moveEntitiesToAdd();

        //removing entities sprites
        for(Entity enitityToRemove: gameWorld.getEntitiesToRemove()){
            runGameView.removeSprite(enitityToRemove.getId());
        }
        //removing enttites from game world
        gameWorld.removeEntities();

        for(Entity entity : gameWorld.getEntities()) {
            runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getX(),
                    entity.getY(), entity.getAngle());
        }




        //This handles the transition to the end game screen if escape is pressed, player tank is killed, or both ai tanks are killed.
        if(KeyboardReader.instance().escapePressed()){
            return false;
        }else if(!(gameWorld.getEntities().contains(gameWorld.getEntity(Constants.AI_TANK_1_ID)) || gameWorld.getEntities().contains(gameWorld.getEntity(Constants.AI_TANK_2_ID)))){
            return false;
        }else if(!(gameWorld.getEntities().contains(gameWorld.getEntity(Constants.PLAYER_TANK_ID)))) {
            return false;
        }

        return true;
    }





    private boolean areEntitiesColliding(Entity entityA, Entity entityB){
       return((entityA.getX() < entityB.getXBound()) && (entityA.getXBound() > entityB.getX())
       && (entityA.getY() < entityB.getYBound()) && (entityA.getYBound() > entityB.getY()));
    }

    private void handleCollision(Entity entityA, Entity entityB) {

        if (entityA instanceof Tank && entityB instanceof Tank) {

            //Calculate distances
            double distanceLeft = entityA.getXBound() - entityB.getX();
            double distanceRight = entityB.getXBound() - entityA.getX();
            double distanceUp = entityA.getYBound() - entityB.getY();
            double distanceDown = entityB.getYBound() - entityA.getY();

            //find smallest distance
            if (distanceLeft < distanceRight && distanceLeft < distanceUp && distanceLeft < distanceDown) {
                //move first tank left by half distance left and second tank right by half distance left
                entityA.setX((entityA.getX()) - (distanceLeft / 2));
                entityB.setX((entityB.getX()) + (distanceLeft / 2));
            } else if (distanceRight < distanceLeft && distanceRight < distanceUp && distanceRight < distanceDown) {
                entityA.setX((entityA.getX()) + (distanceRight / 2));
                entityB.setX((entityB.getX()) - (distanceRight / 2));
            } else if (distanceUp < distanceLeft && distanceUp < distanceRight && distanceUp < distanceDown) {
                entityA.setY((entityA.getY()) - (distanceUp / 2));
                entityB.setY((entityB.getY()) + (distanceUp / 2));
            } else if (distanceDown < distanceLeft && distanceDown < distanceRight && distanceDown < distanceUp) {
                entityA.setY((entityA.getY()) + (distanceDown / 2));
                entityB.setY((entityB.getY()) - (distanceDown / 2));
            }

        } else if (entityA instanceof Shell && entityB instanceof Shell) {
            //removes shells that collide

            gameWorld.getEntities().remove(entityA);
            gameWorld.getEntities().remove(entityB);
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityB.getX(),entityB.getY());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityA.getX(),entityA.getY());
            runGameView.removeSprite(entityA.getId());
            runGameView.removeSprite(entityB.getId());


        } else if (entityA instanceof Shell && entityB instanceof Tank) {
            //removes shell after collision, lowers tank hp by 1, if tank hp 0, removes tank

            gameWorld.getEntities().remove(entityA);
            runGameView.removeSprite(entityA.getId());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityA.getX(),entityA.getY());
            ((Tank) entityB).lowerTankHP();
            if (((Tank) entityB).checkTankHP()) {
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY,entityB.getX(),entityB.getY());
                gameWorld.getEntities().remove(entityB);
                runGameView.removeSprite(entityB.getId());
            }

        } else if (entityA instanceof Tank && entityB instanceof Shell) {
            gameWorld.getEntities().remove(entityB);
            runGameView.removeSprite(entityB.getId());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityB.getX(),entityB.getY());
            ((Tank) entityA).lowerTankHP();
            if (((Tank) entityA).checkTankHP()) {
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY,entityA.getX(),entityA.getY());
                gameWorld.getEntities().remove(entityA);
                runGameView.removeSprite(entityA.getId());
            }

            //tank with wall
        } else if (entityA instanceof Tank && entityB instanceof Wall) {
            //Calculate distances
            double distanceLeft = entityA.getXBound() - entityB.getX();
            double distanceRight = entityB.getXBound() - entityA.getX();
            double distanceUp = entityA.getYBound() - entityB.getY();
            double distanceDown = entityB.getYBound() - entityA.getY();

            //find smallest distance
            if (distanceLeft < distanceRight && distanceLeft < distanceUp && distanceLeft < distanceDown) {
                entityA.setX((entityA.getX()) + (distanceLeft));
            } else if (distanceRight < distanceLeft && distanceRight < distanceUp && distanceRight < distanceDown) {
                entityA.setX((entityA.getX()) - (distanceRight));
            } else if (distanceUp < distanceLeft && distanceUp < distanceRight && distanceUp < distanceDown) {
                entityA.setY((entityA.getY()) + (distanceUp));
            } else if (distanceDown < distanceLeft && distanceDown < distanceRight && distanceDown < distanceUp) {
                entityA.setY((entityA.getY()) - (distanceDown));
            }
            // wall with tank
        } else if (entityA instanceof Wall && entityB instanceof Tank) {
            //Calculate distances
            double distanceLeft = entityA.getXBound() - entityB.getX();
            double distanceRight = entityB.getXBound() - entityA.getX();
            double distanceUp = entityA.getYBound() - entityB.getY();
            double distanceDown = entityB.getYBound() - entityA.getY();

            //find smallest distance
            if (distanceLeft < distanceRight && distanceLeft < distanceUp && distanceLeft < distanceDown) {
                entityB.setX((entityB.getX()) + (distanceLeft));
            } else if (distanceRight < distanceLeft && distanceRight < distanceUp && distanceRight < distanceDown) {
                entityB.setX((entityB.getX()) - (distanceRight));
            } else if (distanceUp < distanceLeft && distanceUp < distanceRight && distanceUp < distanceDown) {
                entityB.setY((entityB.getY()) + (distanceUp));
            } else if (distanceDown < distanceLeft && distanceDown < distanceRight && distanceDown < distanceUp) {
                entityB.setY((entityB.getY()) - (distanceDown));
            }
            //shell with wall
        } else if (entityA instanceof Shell && entityB instanceof Wall) {
            //remove shell from game as well as it's sprite.
            gameWorld.getEntities().remove(entityA);
            runGameView.removeSprite(entityA.getId());
            //add animation
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityA.getX(),entityA.getY());
            //lower wall hp, if wall dies remove from game as well as sprite.
            ((Wall) entityB).lowerWallHP();
            if (((Wall) entityB).checkWallHP()) {
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY,entityB.getX(),entityB.getY());
                gameWorld.getEntities().remove(entityB);
                runGameView.removeSprite(entityB.getId());
            }

            //wall with shell
        } else if (entityA instanceof Wall && entityB instanceof Shell) {

            gameWorld.getEntities().remove(entityB);
            runGameView.removeSprite(entityB.getId());
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY,entityB.getX(),entityB.getY());
            ((Wall) entityA).lowerWallHP();
            if (((Wall) entityA).checkWallHP()) {
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY,entityA.getX(),entityA.getY());
                gameWorld.getEntities().remove(entityA);
                runGameView.removeSprite(entityA.getId());
            }
            //tank attack speed powerup
        } else if (entityA instanceof PlayerTank && entityB instanceof AttackSpeedBoost) {
            gameWorld.getEntities().remove(entityB);
            runGameView.removeSprite(entityB.getId());

            ((Tank) entityA).lowerShellCooldown();
        } else if (entityA instanceof AttackSpeedBoost && entityB instanceof PlayerTank) {
            gameWorld.getEntities().remove(entityA);
            runGameView.removeSprite(entityA.getId());

            ((Tank) entityB).lowerShellCooldown();

            //tank speed boost powerup
        } else if (entityA instanceof Tank && entityB instanceof SpeedBoost) {
            gameWorld.getEntities().remove(entityB);
            runGameView.removeSprite(entityB.getId());

            ((Tank) entityA).increaseMovementSpeed();


        } else if (entityA instanceof SpeedBoost && entityB instanceof Tank) {
            gameWorld.getEntities().remove(entityA);
            runGameView.removeSprite(entityA.getId());

            ((Tank) entityB).increaseMovementSpeed();

            //tank health with health potion
        } else if (entityA instanceof Tank && entityB instanceof HealthPotion) {
            gameWorld.getEntities().remove(entityB);
            runGameView.removeSprite(entityB.getId());

            ((Tank) entityA).increaseTankHP();


        } else if (entityA instanceof HealthPotion && entityB instanceof Tank) {
            gameWorld.getEntities().remove(entityA);
            runGameView.removeSprite(entityA.getId());

            ((Tank) entityB).increaseTankHP();
        }
    }
    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        runGameView.reset();
        gameWorld.getEntities().clear();
        gameWorld.getEntitiesToRemove().clear();
        gameWorld.getEntitiesToAdd().clear();
        gameWorld.resetShellSpeed();
        gameWorld.resetMovementSpeed();



    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
