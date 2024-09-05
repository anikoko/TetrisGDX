package tetris;

import java.awt.Color;

public class View {
	
	static final int BOX_SIZE = 29;
	static final int ORIGIN = 50;
	static final int LeftBorder=2;
	private Graphics graphics;

	public View(Graphics graphics) {
		this.graphics = graphics;
	}

	public void draw(TetrisModel model) {
		drawData(new DrawDataParameter(model.state.field, 0, 0, true));
		drawData(new DrawDataParameter(model.state.figure, model.state.position.y, model.state.position.x, false));
	}

	private void drawData(DrawDataParameter parameterObject) {
		
		for (int r = 0; r < parameterObject.fs.length; r++) {
			for (int c = 0; c < parameterObject.fs[r].length; c++) {
				if (!parameterObject.drawBackground && parameterObject.fs[r][c] == 0)
					continue;
				drawBox(r + parameterObject.row,c + parameterObject.col,parameterObject.fs[r][c]);
			}
		}
	}
	
	private void drawBox(int row, int col, int value) {
		graphics.drawBoxAt(ORIGIN + col * BOX_SIZE, ORIGIN + row * BOX_SIZE, BOX_SIZE, value);
	}
	
	public void writeRestart(String text, int row, int col) {
		graphics.drawRestart(text, row, col);

	}
	
	void showScore(int score) {
		graphics.showScore(score);
	}

	public void showLevel(int level) {
		graphics.showLevel(level);
		
	}


}
