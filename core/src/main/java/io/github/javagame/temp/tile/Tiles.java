package io.github.javagame.temp.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.javagame.temp.PC;

public class Tiles {
    public float xcoords;
    public float ycoords;
    public float width = 2f;
    public float height = 2f;
    public Texture texture;
    public Sprite TileSprite;
    public Boolean hasCollision;
    public Boolean isPlatform;

    public Tiles (float xcoords, float ycoords, Texture texture, Boolean isPlatform, Boolean hasCollision) {
        this.TileSprite = new Sprite(texture);
        this.TileSprite.setSize(width,height);
        this.TileSprite.setPosition(xcoords, ycoords);
        this.isPlatform = isPlatform;
        this.hasCollision = hasCollision;
    }

    public void update (PC player) {
        if (isPlatform) {
            // Possible part to debug
            if((player.CharacterSprite.getY() - (this.ycoords + this.height)) > -0.5 && (player.CharacterSprite.getY() - (this.ycoords + this.height))<=0 )  {
                player.CharacterSprite.setY(this.ycoords + this.height);
                player.yspeed = 0f;
                player.onAir = false;
                player.onGround = true;
                player.jumpTimer = 0f;
            }
        }
      /*  if (hasCollision){}*/
    }
}
