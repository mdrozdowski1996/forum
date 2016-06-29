package pl.edu.mimuw.forum.ui.operation;

import pl.edu.mimuw.forum.ui.controllers.*;
import pl.edu.mimuw.forum.data.*;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.tree.ForumTreeItem;

public class Add extends Operation{
	private NodeViewModel content;
	private NodeViewModel parent;
	private int index;
	
	public Add(NodeViewModel content, NodeViewModel parent, int index){
		super();
		this.content = content;
		this.parent = parent;
		this.index = index;
		this.reverse = new Remove(this);
	}
	
	public Add(NodeViewModel child, NodeViewModel parent){
		this.parent = parent;
		this.content = child;
		this.index = parent.getChildren().size();
		this.reverse = new Remove(this);
	}
	
	public Add(Remove operation, int index){
		super();
		this.content = operation.getContent();
		this.parent = operation.getParent();
		this.reverse = operation;
		this.index = index;
	}
	
	public NodeViewModel getContent() {
		return content;
	}
	
	public void setContent(NodeViewModel content) {
		this.content = content;
	}
	
	@Override
	public void executed(){
		this.parent.getChildren().add(index, content);
	}

	public NodeViewModel getParent() {
		return parent;
	}

	public void setParent(NodeViewModel parent) {
		this.parent = parent;
	}
	

}
