package io.github.javagame.temp.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.javagame.temp.Character;
import io.github.javagame.temp.PC;
import io.github.javagame.temp.Projectiles;

import java.util.ArrayList;

public class Grapple extends Villain {


    public Grapple(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray, boolean gravity) {
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, textureFile, bulletArray, gravity);
        System.out.println("Enemy constructor called");
        System.out.println("Texture file: " + (textureFile != null));
        System.out.println("CharacterSprite: " + (CharacterSprite != null));

        // Distance to patrol in each direction
        this.startX = xspawn;
        this.movingRight = true;
        this.detectionRange = 5f;  // Range at which enemy can detect player
        this.attackRange = 4f;  // Range at which enemy can attack
        this.attackCooldown = 3f;  // Time between attacks
        this.currentCooldown = 0f;
        this.gunShot = bulletArray;
        this.hasGravity = false;

        //Disabled variables for this enemy type
        this.patrolDistance = 0f;
        this.patrolSpeed = 0f;
        this.isChasing = false;
        this.goingRight = true;



        // Set initial sprite position
        /*if (CharacterSprite != null) {
            CharacterSprite.setPosition(xspawn, yspawn);
            System.out.println("Enemy sprite position set to: " + xspawn + ", " + yspawn);
        }*/
    }


    public void update(float deltaTime, float leftbound, float rightbound, PC Target) {

        super.update();

        if (Target != null) {
            if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange) && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 1f)) {
                this.attack(Target);
            }

            }

        // Update hitbox position


        //this.position.set(CharacterSprite.getX(), CharacterSprite.getY());

        // Debug position
        // System.out.println("Enemy position: " + CharacterSprite.getX() + ", " + CharacterSprite.getY());
    }





    // Getters for enemy-specific properties
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }

}

