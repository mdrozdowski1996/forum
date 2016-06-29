package pl.edu.mimuw.forum.ui.operation;

import javafx.beans.property.IntegerProperty;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.models.SurveyViewModel;

public class OperationLikeUp extends Operation{
	SurveyViewModel content;
	int before;
	int after;
	
	public OperationLikeUp(SurveyViewModel content, int before, int after){
		this.content = content;
		this.before = before;
		this.after = after;
		this.reverse = new OperationLikeUp(this);
	}
	public OperationLikeUp(OperationLikeUp operation){
		this.content = operation.getContent();
		this.before = operation.getAfter();
		this.after = operation.getBefore();
		this.reverse = operation;
	}
	
	@Override
	public void executed() {
		content.getLikes().set(after);
		//System.out.println(this.before + " " + this.after);
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
