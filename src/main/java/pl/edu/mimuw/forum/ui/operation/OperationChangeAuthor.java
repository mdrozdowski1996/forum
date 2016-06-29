package pl.edu.mimuw.forum.ui.operation;

import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class OperationChangeAuthor extends Operation{
	private NodeViewModel content;
	private String before;
	private String after;
	
	public OperationChangeAuthor(NodeViewModel content, String before, String after){
		this.after = after;
		this.before = before;
		this.content = content;
		this.reverse = new OperationChangeAuthor(this, content, before, after);
	}
	public OperationChangeAuthor(OperationChangeAuthor operation, NodeViewModel content, String before, String after){
		this.after = before;
		this.before = after;
		this.content = content;
		this.reverse = operation;
	}

	@Override
	public void executed() {
		content.getAuthor().set(after);
	}

}
