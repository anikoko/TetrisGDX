package tetrisGDX;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TetrisGame extends Game implements ScreenListener {

	public SpriteBatch batch;
	public TetrisScreen tetrisScreen;

	@Override
	public void create() {
		batch = new SpriteBatch();
		tetrisScreen = new TetrisScreen();
		setScreen(tetrisScreen);
		tetrisScreen._stage.addScreenListener(this);
		

	}
//
//	 @Override
//	    public void render () {
//	        super.render(); 
//	    }

	 @Override
	 public void dispose () {
	     batch.dispose();
	 }

	@Override
	public void gameOver(int score, int level) {
		System.out.println("GameOver triggered in TetrisGame");
		setScreen(new GameOverScreen(this, score, level));
		
	}

	@Override
	public void restart() {
		tetrisScreen.dispose(); 
	    tetrisScreen = new TetrisScreen(); 
	    setScreen(tetrisScreen);
	    tetrisScreen._stage.addScreenListener(this);
	}

}
