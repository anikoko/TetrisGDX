package tetrisGDX;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TetrisTextRenderer extends ApplicationAdapter  {
    private SpriteBatch batch;
    private BitmapFont font;
    private String score;
    private String level;
   
    public void setScore(String score) {
		this.score = score;
	}
    
    public void setLevel(String level) {
    	this.level = level;
    }

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(1.5f);
		
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (score != null) {
		    batch.begin();
		    font.draw(batch, score, 100, 50);
		    batch.end();
        }
		if (level != null) {
	        batch.begin();
	        font.draw(batch, "\n" + level, 100, 50);
	        batch.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		
	}

}
