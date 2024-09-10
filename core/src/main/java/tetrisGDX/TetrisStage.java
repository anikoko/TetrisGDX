package tetrisGDX;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import tetris.Controller;
import tetris.Graphics;
import tetris.ModelListener;
import tetris.StageListener;
import tetris.TetrisModel;
import tetris.View;

public class TetrisStage extends Stage implements Graphics, StageListener{
	
	static Color[] COLORS = {
			Color.DARK_GRAY, Color.RED, Color.GREEN, 
			Color.BLUE, Color.CYAN, Color.YELLOW, 
			Color.MAGENTA, Color.MAROON};

	private ShapeRenderer shape;
	private View view;
	private TetrisModel model;
	public Controller controller;
	private TetrisTextRenderer textRenderer;
	private Timer.Task task;
	float speed = 1.0f;

	final List<ScreenListener> screenListeners = new ArrayList<>();

	public void addScreenListener(ScreenListener screenListener) {
		screenListeners.add(screenListener);

	}
	public TetrisStage() {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true);
		setViewport(new ScreenViewport(camera));
		shape = new ShapeRenderer();
		Gdx.graphics.setWindowedMode(400, 700);
	}

	public void init() {
		model = new TetrisModel(TetrisModel.DEFAULT_WIDTH,TetrisModel.DEFAULT_HEIGHT, TetrisModel.DEFAULT_COLORS_NUMBER);
		view = new View(this);

		controller = new Controller(model, view);
		
		textRenderer = new TetrisTextRenderer();
		textRenderer.create();
		
		
		model.addListener(controller);
		model.addStageListener(this);
		
		scheduleTask();
		
		Gdx.input.setInputProcessor(this);

		addListener(new InputListener() {

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Input.Keys.LEFT:
					controller.moveLeft();
					break;
				case Input.Keys.RIGHT:
					controller.moveRight();
					break;
				case Input.Keys.SPACE:
					controller.drop();
					break;
				case Input.Keys.UP:
					controller.rotate();
					break;
				case Input.Keys.DOWN: 
					controller.slideDown();
					break;
				case Input.Keys.R: {
					speed = 1.0f;
					controller.restart(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT,
							TetrisModel.DEFAULT_COLORS_NUMBER);
					for (ScreenListener screenListener : screenListeners) {
						screenListener.restart();
					}
					break;
					}
				case Input.Keys.Q: 
					System.exit(0);
					break;
				
				}
				return true;
			}

		});

	}
	
	
	@Override
	public void draw() {
		
		textRenderer.render();
		view.draw(model);
		if (controller.gameOver()) {
			for (ScreenListener screenListener : screenListeners) {
				screenListener.gameOver(model.fullScore, model.level);
			}
			return;
		}
	}

// <----------- Graphics ------------>	
	@Override
	public void drawBoxAt(int x, int y, int size, int colorIndex) {
		Camera camera = getViewport().getCamera();
		camera.update();

		shape.setProjectionMatrix(camera.combined);

		shape.begin(ShapeType.Filled);
		shape.setColor(COLORS[colorIndex]);
		shape.rect(x, y, size, size);
		shape.end();
	}


	@Override
	public void showScore(int score) {
		textRenderer.setScore("Score: " + score);		
	}

	@Override
	public void showLevel(int level) {
		textRenderer.setLevel("Level: " + level);
	}
	
// <--------------- Stage Listener --------->
	@Override
	public void levelHasChanged(int level) {
		speed = (float) Math.pow(0.9f, level);
		rescheduleTask();
	}
	
	public void scheduleTask() {
		task = new Timer.Task() {

			@Override
			public void run() {
				controller.slideDown();
				
			}
		};
		Timer.schedule(task, 1.0f, speed);
	}
	
	public void rescheduleTask() {
		if (task != null) {
	        task.cancel();
	    }
	    scheduleTask();
	}

 

}
