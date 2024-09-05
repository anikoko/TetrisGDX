package tetris;

public interface ModelListener {
	
	void onChange(TetrisModel model);

	void scoreHasChanged(int score);

	void levelHasChanged(int level);

	boolean gameOver();

}
