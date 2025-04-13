package io.github.javagame.temp.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Room {
    public ArrayList<Tiles> CurrentRoom = new ArrayList<>(150);
    Viewport FoR;
    Texture wallRightTexture = new Texture("rightWall.png");
    Texture wallLeftTexture = new Texture("leftWall.png");
    Texture topWallTexture = new Texture("topWall.png");
    Texture lowWallTexture = new Texture("lowWall.png");
    Texture topRightTexture = new Texture("topRightCorner.png");
    Texture topLeftTexture = new Texture("topLeftCorner.png");
    Texture bottomLeftTexture = new Texture("bottomLeftCorner.png");
    Texture bottomRightTexture = new Texture("bottomRightCorner.png");
    Texture emptynessOfTheVoid = new Texture("void.png");


    public Room (Viewport viewport){FoR = viewport;}

    public void setTiles() {
        for (int i = 0; i < 150; ++i) {
            Tiles tile;



            if (i <= 14) {
                tile = new Tiles((i%15) * 2,(i / 15) * 2 ,lowWallTexture, true, true);
            }
            else if (i <= 19) {
                    tile = new Tiles((i % 15) * 2, (i / 15) * 2 , emptynessOfTheVoid, false, true);
            }
            else if (i <= 44) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 59) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 74) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 89) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 104) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 119) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 134) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,emptynessOfTheVoid, false, true);
            }
            else if (i <= 149) {
                tile = new Tiles((i % 15) * 2,(i / 15) * 2 ,topWallTexture, true, true);
            }
            else {
                tile = null;
            }

            if (i % 15 == 0) {
                if (i/15 == 0) { tile = new Tiles((i % 15) * 2,(i / 15) * 2, bottomLeftTexture, false, true);
                   /* tile.texture = bottomLeftTexture;
                    tile.hasCollision = true;*/
                }
                else if (i / 15 == 9) {tile = new Tiles((i % 15) * 2,(i / 15) * 2, topLeftTexture, false, true);
                    /*tile.texture = topLeftTexture;
                    tile.hasCollision = true;*/
                }
                else { tile = new Tiles((i % 15) * 2,(i / 15) * 2,wallLeftTexture, false, true);
                   /* tile.texture = wallLeftTexture;
                    tile.hasCollision = true;*/
                }
            }
            if (i % 15 == 14) {
                if (i/15 == 0) { tile = new Tiles((i % 15) * 2,(i / 15) * 2,bottomRightTexture, false, true);
                   /* tile.texture = bottomRightTexture;
                    tile.hasCollision = true;*/
                }
                else if (i / 15 == 9) {tile = new Tiles((i % 15) * 2,(i / 15) * 2, topRightTexture, false, true);
                    /*tile.texture = topRightTexture;
                    tile.hasCollision = true;*/
                }
                else {tile = new Tiles((i % 15) * 2,(i / 15) * 2,wallRightTexture, false, true);
                   /* tile.texture = wallRightTexture;
                    tile.hasCollision = true;*/
                }
            }

            CurrentRoom.add(tile);



        }
    }


}
