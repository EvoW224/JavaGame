package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class    Character {

    public float deltaTime = Gdx.graphics.getDeltaTime();

    //Movement Related Variables
    public Vector2 position; // Position of character
    public Vector2 velocity; // Velocity (speed and direction)
    public Vector2 acceleration; // Acceleration due to movement/gravity

    public boolean onGround;
    public boolean onAir;
    public boolean isJumping;

    public float maxSpeed;
    public float terminalVelocity = 24f;
    public float gravity = 3.0f; // Simulated gravity
    public float friction = 0; // Horizontal friction
    public float jumpStrength = 30f; // Base jump force
    public float speed = 0f;
    public float dvdt = 0.8f;
    public float jumpTimer = 0;
    public float yspeed;// Tracks how long the jump button is held
    public float maxJumpTime = 0f; // Maximum time jump can be held (1 second)

    //Local Viewport Variable
    public Viewport viewport; // For boundary handling

    //HitBox
    public Rectangle CharacterHitbox;

    // Stats In-Game
    public int HitPoints;
    public int DamageStat;

    //Texture
    public Texture CharacterTexture;
    public Sprite CharacterSprite;
    public SpriteBatch CharacterSpriteBatch;

    //Getter Functions
    public float getXPosition() { return this.position.x;}
    public float getYPosition() { return this.position.y;}
    public float getMaxSpeed() {  return this.maxSpeed;}
    public int getHitPoints() {   return this.HitPoints;}
    public int getDamageStat() {  return this.DamageStat;}
    public Vector2 getPosition() {return this.position;}
    public boolean isOnGround() { return this.onGround;}
    public boolean isOnAir() {    return this.onAir;}

    public void characterCreation (Texture textureName, float width, float height) {
        /*this.CharacterTexture = new Texture(textureName);*/
        this.CharacterSprite = new Sprite(textureName);
        CharacterSprite.setSize(width,height);
        CharacterSprite.setPosition(position.x, position.y);
        /*System.out.println("Player Position: " + CharacterSprite.getX() + ", " + CharacterSprite.getY());
        System.out.println("Texture Loaded: " + (CharacterSprite.getTexture() != null));*/


    }

    //Constructor
    public Character(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile) {
        this.viewport = viewport;
        this.maxSpeed = maxSpeed;
        this.CharacterHitbox = new Rectangle(xspawn, yspawn, width, height);

        this.position = new Vector2(xspawn, yspawn); // Initial position
        /*this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);*/

        this.onGround = true;
        this.onAir = false;
        this.isJumping = false;

        this.HitPoints = HP;
        this.DamageStat = damageStat;
        this.characterCreation(textureFile, width, height);
    }



    public boolean entityOverlap (Rectangle otherEntityHitbox) {
        return (this.CharacterHitbox.overlaps(otherEntityHitbox));
    }

    public boolean isDead() {
        return (HitPoints <= 0);
    }

    /*public void drawCharacter(float worldWidth, float worldHeight) {

    }*/


    public void disposeCharacter() {
        CharacterTexture.dispose();
    }






}
