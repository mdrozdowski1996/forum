package pl.edu.mimuw.forum.ui.operation;

import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;

public class OperationIsResponseAccepted extends Operation{
	private SuggestionViewModel sug;
	private boolean before;
	private boolean after;
	
	public OperationIsResponseAccepted(SuggestionViewModel sug, boolean before, boolean after){
		this.after = after;
		this.setBefore(before);
		this.sug = sug;
		this.reverse = new OperationIsResponseAccepted(this);
	}
	
	public OperationIsResponseAccepted(OperationIsResponseAccepted operation){
		this.after = operation.isBefore();
		this.before = operation.isAfter();
		this.reverse = operation;
		this.sug = operation.getSug();
	}

	@Override
	public void executed() {
		sug.getIsResponseAccepted().set(after);	
	}

	public boolean isBefore() {
		return before;
	}
	public boolean isAfter(){
		return after;
	}
	public SuggestionViewModel getSug(){
		return sug;
	}
	public void setBefore(boolean before) {
		this.before = before;
	}
	
}
