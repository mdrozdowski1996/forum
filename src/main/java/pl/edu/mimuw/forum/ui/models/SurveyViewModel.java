package pl.edu.mimuw.forum.ui.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import pl.edu.mimuw.forum.data.Survey;
import pl.edu.mimuw.forum.data.Node;
import pl.edu.mimuw.forum.ui.controllers.DetailsPaneController;
import pl.edu.mimuw.forum.ui.controllers.MainPaneController;
import pl.edu.mimuw.forum.ui.operation.OperationDislikeUp;
import pl.edu.mimuw.forum.ui.operation.OperationLikeUp;

public class SurveyViewModel extends NodeViewModel {
	
	public static final String NAME = "Survey";

	private final IntegerProperty likesProperty;
	private final IntegerProperty dislikesProperty;
	
	public SurveyViewModel(String content, String author) {
		this(new Survey(content, author));
	}
	
	public SurveyViewModel(Survey survey) {
		super(survey);
		
		likesProperty = new SimpleIntegerProperty(survey.getLikes());
		
		dislikesProperty = new SimpleIntegerProperty(survey.getDislikes());
		
		if(MainPaneController.redoFlag == 0){likesProperty.addListener((observable, oldValue, newValue ) -> {
			if(oldValue.intValue() < newValue.intValue()){
				OperationLikeUp change = new OperationLikeUp(this, oldValue.intValue(), newValue.intValue());
				MainPaneController.history.add(change);
			}
		});}
		
		if(MainPaneController.redoFlag == 0){dislikesProperty.addListener((observable, oldValue, newValue) -> {
			if(oldValue.intValue() < newValue.intValue() ){
				OperationDislikeUp change = new OperationDislikeUp(this, oldValue.intValue(), newValue.intValue());
				MainPaneController.history.add(change);
			}
		});}
		
	}
	
	
	public IntegerProperty getLikes() {
		return likesProperty;
	}
	
	public IntegerProperty getDislikes() {
		return dislikesProperty;
	}
	
	@Override
	protected Node createDocument() {
		return new Survey(getContent().get(), getAuthor().get(), likesProperty.get(), dislikesProperty.get());
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void presentOn(DetailsPaneController detailsPaneController) {
		detailsPaneController.present(this);
	}

}
