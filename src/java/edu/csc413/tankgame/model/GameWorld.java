package edu.csc413.tankgame.model;

import java.util.*;

/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */
public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.
    private ArrayList<Entity> entities;
    private ArrayList<Entity> entitiesToAdd;
    private ArrayList<Entity> entitiesToRemove;

    public GameWorld() {

        entities = new ArrayList<>();
        entitiesToAdd = new ArrayList<>();
        entitiesToRemove = new ArrayList<>();
    }

    /** Returns a list of all entities in the game. */
    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesToAdd(){
        return entitiesToAdd;
    }

    public List<Entity> getEntitiesToRemove(){
        return entitiesToRemove;
    }

    /** Adds a new entity to the game. */
    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
    }

    //move entities to main list
    public void moveEntitiesToAdd(){
        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
    }

    //resets shell speed at the end of a game
    public void resetShellSpeed(){
        Tank.INITIAL_SHELL_COOLDOWN = 38;
    }

    //resets movement speed at the end of a game
    public void resetMovementSpeed(){
        Tank.MOVEMENT_MULTIPLIER = 1;
    }

    public void removeEntities(){
        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }
    //moves entities on main list to new removal list
    public void addEntitityToRemovalList(Entity a){
        entitiesToRemove.add(a);

    }




    /** Returns the Entity with the specified ID. */
    public Entity getEntity(String id) {
        for(Entity entity: entities){
            if(entity.getId() == id){
                return entity;
            }
        }
        return null;
    }

    /** Removes the entity with the specified ID from the game. */
    public void removeEntity(String id) {
        for(Entity entity: entities){
            if(entity.getId() == id){
                entities.remove(entity);
            }
        }
    }


}
