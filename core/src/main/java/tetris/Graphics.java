package tetris;

public interface Graphics {

	void drawBoxAt(int i, int j, int size, int colorIndex);
	void showScore(int score);
	void showLevel(int level);
}
