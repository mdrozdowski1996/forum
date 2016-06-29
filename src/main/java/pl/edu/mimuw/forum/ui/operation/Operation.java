package pl.edu.mimuw.forum.ui.operation;

public abstract class Operation {
	protected Operation reverse;
	
	public abstract void executed();
	
	public Operation getReverse() {
		return reverse;
	}
	public void setReverse(Operation reverse) {
		this.reverse = reverse;
	}
	
}
