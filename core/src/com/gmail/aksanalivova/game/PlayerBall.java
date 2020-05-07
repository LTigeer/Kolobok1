package com.gmail.aksanalivova.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class PlayerBall {
    private static final int countFramesOnAnomation = 12;
    private static final int playerBallHeightTextere = 64;

    private Vector3 position; //позиция на экравне
    private Rectangle bounds; //прямоугольник, опысывающий главного персонажа. Необходим для просчета столкновений
    private Animation playerBallAnimation;
    private Texture textureBall; //текстура, содержащая кадровую анимацию
    private int startWeight = 100;

    private int weight;

    public PlayerBall(float x, float y){
        position = new Vector3(x, y, 0);

        textureBall = new Texture("pokeballs.png");
        playerBallAnimation = new Animation(new TextureRegion(textureBall, playerBallHeightTextere*countFramesOnAnomation, playerBallHeightTextere), countFramesOnAnomation, 1.0f);

        bounds = new Rectangle(x, y, textureBall.getWidth() /countFramesOnAnomation, playerBallHeightTextere);
        dropWeight();
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getPlayerBall() {
        return playerBallAnimation.getFrame();
    }

    public void update(float dt){
        playerBallAnimation.update(dt);
    }

    //устанавливает позицию
    public void setPosition (float x, float y){
        position.set(x, y, 0);
        bounds.setPosition(position.x, position.y);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(getPlayerBall(), getPosition().x, getPosition().y);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void dropWeight() {
        setWeight(startWeight);
    }

    public static int getCountFramesOnAnomation() {
        return countFramesOnAnomation;
    }

    public static int getPlayerBallHeightTextere() {
        return playerBallHeightTextere;
    }
}