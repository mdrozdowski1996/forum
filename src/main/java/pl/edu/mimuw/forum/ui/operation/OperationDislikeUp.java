package pl.edu.mimuw.forum.ui.operation;

import javafx.beans.property.IntegerProperty;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.models.SurveyViewModel;

public class OperationDislikeUp extends Operation{
	SurveyViewModel content;
	int before;
	int after;
	
	public OperationDislikeUp(SurveyViewModel content, int before, int after){
		this.content = content;
		this.before = before;
		this.after = after;
		this.reverse = new OperationDislikeUp(this);
	}
	public OperationDislikeUp(OperationDislikeUp operation){
		this.content = operation.getContent();
		this.before = operation.getAfter();
		this.after = operation.getBefore();
		this.reverse = operation;
	}
	
	@Override
	public void executed() {
		content.getDislikes().set(after);
	}
	public int getBefore(){
		return before;
	}
	public int getAfter(){
		return after;
	}
	public SurveyViewModel getContent(){
		return content;
	}

}
