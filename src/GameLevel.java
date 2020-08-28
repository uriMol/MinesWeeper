
public enum GameLevel {
	/*
	 * Java MinesWeeper game
	 * 
	 * Made by uMolcho
	 * ***Not finished - should add starting page
	 */
	
	Begginer(10, 15),
	Medium(16, 40),
	Hard(25, 80);
	
	private int rows;
	private int mines;
	
	private GameLevel(int rows, int mines) {
		this.rows = rows;
		this.mines = mines;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getMines() {
		return this.mines;
	}

}
