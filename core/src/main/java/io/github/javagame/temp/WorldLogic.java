package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class WorldLogic {
    float gravity = 0;
    public Texture platformHolder;
    public SpriteBatch spriteBatch;
    FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;
    float deltaTime = Gdx.graphics.getDeltaTime();


    public void createWorld () {
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
    }


    public void resize(int width, int height) {
        viewportWorld.update(width, height, true); // true centers the camera
    }


    public void renderWorld () {
        /*ScreenUtils.clear(Color.BLACK);*/
        viewportWorld.apply();

    }
}


/* private void logic(FitViewport viewport, Character PCharacter, Platform platforms[]) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float deltaTime = Gdx.graphics.getDeltaTime();
        float PCWidth = PCharacter.getWidth();
        float PCHeight = PCharacter.getHeight();
        float PlatformHeight = platforms[0].getHeight();
        float PlatformWidth = platforms[0].getWidth();
        float jumpPower = 30f;
        float delta = Gdx.graphics.getDeltaTime();





        PCharacter.PChitbox.set(PCharacter.getX(), PCharacter.getY(), PCWidth, PCHeight); //Hitboxes
        Platformhitbox.set(Platform.getX() + .5f, Platform.getY() + .1f, PlatformWidth / 2, PlatformHeight / 5);
        //Clamps
        PCharacter.setX(MathUtils.clamp(PCharacter.getX(), 0 - .5f, worldWidth - PCWidth + .5f));
        PCharacter.setY(MathUtils.clamp(PCharacter.getY(), 0, worldHeight - PCHeight));
        //Ground Detection
        if (PCharacter.getY() == 0 || PChitbox.overlaps(Platformhitbox)) {
            onGround = true; gravity = 0;canJump = true;
        }
        else if (PCharacter.getY() != 0 && !PChitbox.overlaps(Platformhitbox)) {
            onGround = false; gravity = 6f; canJump = false;
        }
        // Gravity and air Speed
        if ( !onGround ) {PCharacter.setY(PCharacter.getY() - gravity *  deltaTime); speed = 5f;}
        if (onGround) {canJump = true; speed = 4f;}
        //Hit detection and overlap
        if (PChitbox.overlaps(Platformhitbox)) {
            if( PChitbox.getY() > Platformhitbox.getY()){onGround = true; gravity = 0;canJump = true;}
            else if (PChitbox.getY() < Platformhitbox.getY()) {jumpTimer = 1f;}
            if( PChitbox.getX() >= Platformhitbox.getX() && Gdx.input.isKeyPressed(Input.Keys.A) && !onGround){PCharacter.translateX(speed * delta);}
            else if (PChitbox.getX() <= Platformhitbox.getX() && Gdx.input.isKeyPressed(Input.Keys.D) && !onGround) {PCharacter.translateX(-speed * delta);}

        }

        private void platformGenerator() {
            float platformWidth = 2f;
            float platformHeight = .5f;
            float worldWidth = viewport.getWorldWidth();
            float worldHeight = viewport.getWorldHeight();


            Platform.setSize(platformWidth, platformHeight );
            Platform.setX(worldWidth / 2 - 1);
            Platform.setY(worldHeight / 2 - 1);

        }*/
