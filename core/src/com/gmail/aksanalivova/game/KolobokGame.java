package com.gmail.aksanalivova.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class KolobokGame extends Game {
	public SpriteBatch batch;
	public Stage stageDefault;
	public BitmapFont font;
	public Skin skinButton;
	public TextButton.TextButtonStyle textButtonStyle;

	public Skin skinLabel;

	public Skin skinDef;

	public Label.LabelStyle labelStyle;

	//HD resolution
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;

	public static final String TITLE = "Kolobok";

	@Override
	public void create () {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() started");
		float tileWidth = 512.0f;
		float tileHeight = 512.0f;
		float centerX = 300.0f;
		float centerY = 200.0f;
		int centeredTileNumberX = (int)(centerX/tileWidth);
		int centeredTileNumberY = (int)(centerY/tileHeight);;
		System.out.println("centeredTileNumberX= " + centeredTileNumberX + " centeredTileNumberY= " + centeredTileNumberY);
		for (int i = centeredTileNumberX-2; i <= centeredTileNumberX+2; i = i + 1) {
			System.out.println("i= " + i);
		}

		System.out.println("KolobokGame.Create called");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() before stageDefault = new Stage()");
		stageDefault = new Stage();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after stageDefault = new Stage()");
		//фоновая музыка
		Music music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after music.setVolume(0.1f);");
		//music.play();
		//создаем "рисовальшика" для игры.
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.getData().setScale(Gdx.graphics.getHeight()/300, Gdx.graphics.getHeight()/300);

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after font = new BitmapFont();");
		//Создаем скин по умолчанию для кнопок с текстом. Без скина не работают.
		skinLabel = new Skin();
		skinLabel.add("default", font);

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after skinLabel.add(\"default\", font);");
		skinButton = new Skin();
		skinButton.add("default", font);

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after skinButton.add(\"default\", font);");
		//создаем текстуру кнопки и задаем ей размер
		Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skinButton.add("background",new Texture(pixmap));

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after pixmap.fill();");

		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skinButton.newDrawable("background", Color.BLACK);
		textButtonStyle.down = skinButton.newDrawable("background", Color.BLACK);
		textButtonStyle.checked = skinButton.newDrawable("background", Color.BLACK);
		textButtonStyle.over = skinButton.newDrawable("background", Color.DARK_GRAY);
		textButtonStyle.font = skinButton.getFont("default");
		skinButton.add("default", textButtonStyle);

		labelStyle = new Label.LabelStyle();
		labelStyle.font = skinButton.getFont("default");
		labelStyle.fontColor = Color.RED;

		skinLabel.add("default", labelStyle);
		skinDef = new Skin(Gdx.files.internal("skin/shade/uiskin.json"));

		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ").format(Calendar.getInstance().getTime())+ "KolobokGame create() after skinDef = new Skin(Gdx.files.internal(\"skin/shade/uiskin.json\"));");

		setScreen(new KolobokScreen(this));
	}

	public void render() {
		//важно!!!
		//в этом методе и происходит отрисовка экрана
		super.render(); //
	}

	public void showErrorDialog(String errorMsg) {
/*
new Dialog("Some Dialog", skin, "dialog") {
   protected void result (Object object) {
      System.out.println("Chosen: " + object);
   }
}.text("Are you enjoying this demo?").button("Yes", true).button("No", false).key(Keys.ENTER, true)
   .key(Keys.ESCAPE, false).show(stage);
*/
		Dialog dialog = new Dialog("Some Dialog", skinDef, "dialog") {
			protected void result (Object object) {
				System.out.println("Chosen: " + object);
			}
		}.text(errorMsg).button("Ok", true).key(Input.Keys.ENTER, true);
		stageDefault.addActor(dialog);
		dialog.show(stageDefault);

	}

}
