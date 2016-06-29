package pl.edu.mimuw.forum.ui.operation;

import pl.edu.mimuw.forum.data.*;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.tree.ForumTreeItem;

public class Remove extends Operation{
	private NodeViewModel content;
	private NodeViewModel parent;
	
	public Remove(NodeViewModel content, NodeViewModel parent){
		super();
		this.content = content;
		this.parent = parent;
		this.reverse = new Add(this, parent.getChildren().lastIndexOf(content));
	}
	
	public Remove(Add operation){
		super();
		this.setParent(operation.getParent());
		this.setContent(operation.getContent());
		this.reverse = operation;
	}

	public NodeViewModel getContent() {
		return content;
	}

	public void setContent(NodeViewModel content) {
		this.content = content;
	}
	
	@Override
	public void executed(){
		this.parent.getChildren().remove(content);
	}

	public NodeViewModel getParent() {
		return parent;
	}

	public void setParent(NodeViewModel parent) {
		this.parent = parent;
	}

}
