package com.gmail.aksanalivova.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.Random;

public class KolobokScreen implements Screen {
    private final KolobokGame game;
    private TextButton stoptButton; //кнопка выхода из игры
    private TextButton centerButton; //кнопка центрирования персонажа и камеры на экране
    private Label labelWeightPlayer; //метка для отображения "массы" игрока
    private Label labelFPS; //метка для отображения FPS


    private OrthographicCamera cam; //камера - это "окно", через которое пользователь смотрит на игровой мир

    private TiledMap tileMap; // тайлируемая подложка. Для теста сделана
    private OrthogonalTiledMapRenderer tileMapRenderer; // рисовальщик для тайловой подложки

    private final int velocity = 5; // скорость движения персонажа.

    private Stage stage; // на него добавляются графические компоненты по типу иерархии. Удобно
    private Touchpad touchpad; //джойстик
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private int columns;
    private int rows;
    private int tileWidth;
    private int tileHeight;
    private int mapWidthInPixels;
    private int mapHeightInPixels;

    private PlayerBall playerBall;

    private int countFood = 10;
    private ArrayList<PlayerFood> playerFoods;
    private int scoreGame = 0;


    private Sound eatSound;

    private Dialog dialog;

    private boolean endGame = false;


    public KolobokScreen(KolobokGame game) {
        this.game = game;
    }


    @Override
    public void show() {
        //метод вызывается при создании экрана.
        Gdx.gl.glClearColor(0, 0, 1, 1); //blue

        //создаем окно, через которое юзер смотрит на игровой мир
        cam = new OrthographicCamera();
        cam.setToOrtho(false, game.WIDTH, game.HEIGHT);
        game.batch.setProjectionMatrix(cam.combined);

        //создаем карту-подложку
        tileMap = new TmxMapLoader().load("desert1920_1920.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get(0);
        columns = layer.getWidth();
        rows = layer.getHeight();
        tileWidth = (int) layer.getTileWidth();
        tileHeight = (int) layer.getTileHeight();
        mapWidthInPixels = columns * tileWidth;
        mapHeightInPixels = rows * tileHeight;


        System.out.println("columns = " + columns + " rows = " + rows + " tileWidth = " + tileWidth + " tileHeight = " + tileHeight);
        System.out.println("map size = " + mapWidthInPixels + " * " + mapHeightInPixels);

//---------------------------------------------------
        //создаем джойстик
        //Create a touchpad skinButton
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("Touchpad/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("Touchpad/touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skinButton
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchKnob.setMinHeight(Gdx.graphics.getWidth()/10);
        touchKnob.setMinWidth(Gdx.graphics.getWidth()/10);
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, Gdx.graphics.getWidth()/5, Gdx.graphics.getWidth()/5);

        //Create a Stage and add TouchPad
        stage = new Stage();
        stage.addActor(touchpad);
//-----------------------------------------------------

        //создали кнопку выхода из игры и распологаем в правом верхнем углу
        stoptButton = new TextButton("StopGame", game.skinButton); // Use the initialized skinButton
        stoptButton.setPosition(Gdx.graphics.getWidth() - stoptButton.getWidth(), Gdx.graphics.getHeight() - stoptButton.getHeight());
        stage.addActor(stoptButton);
// --------------------------------------------------------------------------------------
        stoptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

            ;
        });
// --------------------------------------------------------------------------------------
        //создали кнопку центрирования и распологаем в левом верхнем углу
        centerButton = new TextButton("onCenter", game.skinButton); // Use the initialized skinButton
        centerButton.setPosition(0, Gdx.graphics.getHeight() - stoptButton.getHeight());
        stage.addActor(centerButton);
// --------------------------------------------------------------------------------------
        centerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onCenterScreen();
            }
        });

        //Указываем либе, что все нажатия кнопок будет отрабатывать стейдж
        Gdx.input.setInputProcessor(stage);

        //создаем главнойго персонаджа
        playerBall = new PlayerBall(mapWidthInPixels / 2 - playerBall.getPlayerBallHeightTextere() / 2,
                mapHeightInPixels / 2 - playerBall.getPlayerBallHeightTextere() / 2);

// --------------------------------------------------------------------------------------
        //создаем текстовую метку для отображения массы персонажа
        labelWeightPlayer = new Label("Weight = " + playerBall.getWeight(), game.skinLabel);
        labelWeightPlayer.setPosition(Gdx.graphics.getWidth() - stoptButton.getWidth() + 20, Gdx.graphics.getHeight() - stoptButton.getHeight() - labelWeightPlayer.getHeight() - 20);
        stage.addActor(labelWeightPlayer);
        labelFPS = new Label("Score = " + scoreGame + "/" + countFood, game.skinLabel);
        labelFPS.setPosition(Gdx.graphics.getWidth() - stoptButton.getWidth() + 20, Gdx.graphics.getHeight() - stoptButton.getHeight()  - labelWeightPlayer.getHeight() - labelFPS.getHeight() - 20);
        stage.addActor(labelFPS);

        createFood();

        //звук воспроизводится, когда происходит столкновение перса и еды
        eatSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));

        //центрируем всё на экране
        onCenterScreen();

        dialog = new Dialog("", game.skinDef, "dialog") {
            protected void result(Object object) {
                startGame();
            }
        }.button("Ok", true, game.textButtonStyle).key(Input.Keys.ENTER, true);
        dialog.text("You won", game.labelStyle);
        dialog.hide();
        stage.addActor(dialog);

        System.out.println("MainScreen.show called");
    }


    @Override
    public void render(float delta) {
        //в этом методе происходит отрисовка экрана. Метод вызывается самой либой циклично.
        float newX;
        float newY;
        float oldCamPsitionX;
        float oldCamPsitionY;
        //координатная сетка начинается в левом нижнем углу
        if (touchpad.isTouched()) {
            System.out.println("% = " + touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
            System.out.println("x = " + touchpad.getKnobX() + " " + touchpad.getKnobY());
            System.out.println("cam x = " + cam.position.x + " cam y = " + cam.position.y);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
        ;

        tileMapRenderer.setView(cam);
        tileMapRenderer.render();

        cam.update();

        game.batch.setProjectionMatrix(cam.combined);

        //Move cam and smileImgSprite with TouchPad
        oldCamPsitionX = cam.position.x;
        oldCamPsitionY = cam.position.y;

        //отработка нажатий джойстика
        if (touchpad.isTouched() && !endGame) {
            newX = touchpad.getKnobPercentX() * velocity;
            newY = touchpad.getKnobPercentY() * velocity;
            if (oldCamPsitionX + newX < 0 + playerBall.getBounds().height / 2
                    || oldCamPsitionX + newX > 1920 - playerBall.getBounds().height / 2
                    || oldCamPsitionY + newY < 0 + playerBall.getBounds().height / 2
                    || oldCamPsitionY + newY > 1920 - playerBall.getBounds().height / 2) {
                //ничего не делаем, выход за границы карты
            } else {
                cam.translate(newX, newY, 0);
                playerBall.setPosition(playerBall.getPosition().x + newX,
                        playerBall.getPosition().y + newY);

            }
        }

        updateFoods(delta);

        playerBall.update(delta);

        processCollision();


        game.batch.begin(); //начало цикла отрисовки

        drawFoods();

        // отрисовка главного персонажа
        playerBall.draw(game.batch);

        game.batch.end();//окончание цикла отрисовки

        if (scoreGame == countFood && !endGame) {
            endGame = true;
            dialog.setPosition(playerBall.getPosition().x, playerBall.getPosition().y);
            dialog.show(stage);
            dialog.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
            dialog.setY(Math.round((stage.getHeight() - dialog.getHeight()) / 2));
            dialog.setX(Math.round((stage.getWidth() - dialog.getWidth()) / 2));
        }

        labelWeightPlayer.setText("Weight = " + playerBall.getWeight());
        labelFPS.setText("Score = " + scoreGame + "/" + countFood);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("MainScreen.resize called");

    }

    @Override
    public void pause() {
        System.out.println("MainScreen.pause called");

    }

    @Override
    public void resume() {
        System.out.println("MainScreen.resume called");

    }

    @Override
    public void hide() {
        Gdx.app.exit();
        System.out.println("MainScreen.hide called");
    }

    @Override
    public void dispose() {
        System.out.println("MainScreen.dispose called");
    }

    //центрирование карты и перса по центру камеры
    private void onCenterScreen() {
        cam.position.set(mapWidthInPixels / 2, mapHeightInPixels / 2, 0);

        playerBall.setPosition(mapWidthInPixels / 2 - playerBall.getPlayerBallHeightTextere() / 2,
                mapHeightInPixels / 2 - playerBall.getPlayerBallHeightTextere() / 2);
        System.out.println("MainScreen.onCenterScreen called");
    }

    private void startGame() {
        endGame = false;
        scoreGame = 0;
        onCenterScreen();
        createFood();
        playerBall.dropWeight();
    }

    private void createFood() {
        //Создаем "еду"
        playerFoods = new ArrayList<>();

        int min = 0 + 100;
        int max = 1920 - 100;
        int diff = max - min;
        Random random = new Random(System.currentTimeMillis());

        for (int i = 1; i <= countFood; i++) {
            int x = random.nextInt(diff);
            x = x + min;
            int y = random.nextInt(diff);
            y = y + min;
            PlayerFood playerFood = new PlayerFood(x, y);
            playerFoods.add(playerFood);
        }
    }

    private void updateFoods(float delta) {
        for (PlayerFood playerFood : playerFoods) {
            playerFood.update(delta);
        }
    }

    private void processCollision() {
        //отработка столкновений.
        for (PlayerFood playerFood : playerFoods) {
            if (playerFood.collision(playerBall.getBounds())) {
                if (playerFood.getIsShow()) {
                    scoreGame = scoreGame + 1;
                    playerBall.setWeight(playerBall.getWeight() + 20);
                    eatSound.play();
                }
                playerFood.setIsShow(false);
            }

        }
    }

    private void drawFoods() {
        //отрисовка еды на экране
        for (PlayerFood playerFood : playerFoods) {
            if (playerFood.getIsShow()) {
                game.batch.draw(playerFood.getPlayerFood(), playerFood.getPosition().x, playerFood.getPosition().y);
            }
        }
    }

}

