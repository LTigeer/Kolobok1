package com.gmail.aksanalivova.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PlayerFood {
    private static final int countFramesOnAnomation = 10; // кол-во кадров в анимации
    public static final int playerFoodHeightTextere = 100; //высота текстуры в анимации.

    private Vector3 position; //позиция на экравне
    private Rectangle bounds; //прямоугольник, опысывающий еду. Необходим для просчета столкновений
    private Animation playerFoodAnimation;
    private Texture texture; //текстура, содержащая кадровую анимацию
    private boolean isShow;//необходим, т.к. еда с экрана пропадает при наезде игроком

    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }


    public PlayerFood(float x, float y){
        position = new Vector3(x, y, 0);
        texture = new Texture("coin-sprite.png");
        playerFoodAnimation = new Animation(new TextureRegion(texture, playerFoodHeightTextere*countFramesOnAnomation, playerFoodHeightTextere), countFramesOnAnomation, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() /countFramesOnAnomation, playerFoodHeightTextere);
        setIsShow(true);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getPlayerFood() {
        return playerFoodAnimation.getFrame();
    }

    public void update(float dt){
        playerFoodAnimation.update(dt);
    }

    public boolean collision(Rectangle player){
        return player.overlaps(bounds);
    }

}