
public class Cell {
	
	/*
	 * Java MinesWeeper game
	 * 
	 * Made by uMolcho
	 * ***Not finished - should add starting page
	 */
	
	private boolean hidden;
	private boolean marker;
	private int type;
	
	public Cell() {
		this.hidden = true;
		this.marker = false;
		this.type = 0;
	}
	
	public boolean isMine() {
		return (this.type == 9);
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void appendType() {
		this.type++;
	}
	
	public boolean isHidden() {
		return this.hidden;
	}
	
	public boolean isMarked() {
		return this.marker;
	}
	
	public void mark() {
		this.marker = !this.marker;
	}
	
	public void unhide() {
		this.hidden = false;
		this.marker = false;
	}
	
	public String toString() {
		return Integer.toString(this.type) + ", marked = " + this.marker + ", hidden = " + this.hidden ;
	}
	
	
	

}
