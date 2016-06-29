package pl.edu.mimuw.forum.ui.operation;

import javafx.beans.value.WritableValue;
import pl.edu.mimuw.forum.data.*;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;

public class OperationChangeContent extends Operation{
	private NodeViewModel node;
	private String before;
	private String after;
	
	public OperationChangeContent(String before, String after, NodeViewModel node){
		super();
		this.node = node;
		this.after = after;
		this.before = before;
		this.reverse = new OperationChangeContent(this, before, after, node);	
	}
	
	public OperationChangeContent(OperationChangeContent change, String before, String after, NodeViewModel node){
		super();
		this.after = before;
		this.before = after;
		this.node = change.node;
		this.reverse = change;
	}
	
	public void executed(){
		node.getContent().set(after);
	}

}
