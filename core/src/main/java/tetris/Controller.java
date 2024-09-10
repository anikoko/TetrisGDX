package tetris;

import com.badlogic.gdx.Gdx;

import tetrisGDX.GameOverScreen;
import tetrisGDX.TetrisGame;

public class Controller implements ModelListener, GameEventsListener {
	
	private TetrisModel model;
	private View view;
	
	public Controller(TetrisModel model, View view) {
		this.model = model;
		model.addListener(this);
		this.view = view;
	}
	
	
	
	@Override
	public void onChange(TetrisModel model) {
		view.draw(model);
		view.showScore(model.fullScore);
		view.showLevel(model.level);	
	}

	@Override
	public void scoreHasChanged(int score) {
		view.showScore(score);
		
	}
	
	@Override
	public void levelHasChanged(int level) {
		view.showLevel(level);
		
		
	}
	@Override
	public boolean gameOver() {
		return model.gameOver;
		
	}
	
	@Override
	public void slideDown() {
		model.slideDown();
	}

	@Override
	public void moveLeft() {
		model.moveLeft();
	}

	@Override
	public void moveRight() {
		model.moveRight();
	}

	@Override
	public void rotate() {
		model.rotate();
	}

	@Override
	public void drop() {
		model.drop();
	}
	
	public void restart(int width, int height, int maxColors) {
		model.initModel(width, height, maxColors);
	}




}
