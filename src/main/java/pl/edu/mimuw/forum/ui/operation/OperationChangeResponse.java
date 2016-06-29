package pl.edu.mimuw.forum.ui.operation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;

public class OperationChangeResponse extends Operation{
	SuggestionViewModel content;
	String before;
	String after;
	
	public OperationChangeResponse(SuggestionViewModel content, String before, String after){
		this.content = content;
		this.before = before;
		this.after = after;
		this.reverse = new OperationChangeResponse(this);
	}
	public OperationChangeResponse(OperationChangeResponse operation){
		this.content = operation.content;
		this.after = operation.before;
		this.before = operation.after;
		this.reverse = operation;
	}
	
	@Override
	public void executed() {
		content.getResponse().set(after);
	}
	
	

}
