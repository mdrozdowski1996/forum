package pl.edu.mimuw.forum.ui.models;

import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.edu.mimuw.forum.data.Suggestion;
import pl.edu.mimuw.forum.data.Node;
import pl.edu.mimuw.forum.ui.controllers.DetailsPaneController;
import pl.edu.mimuw.forum.ui.controllers.MainPaneController;
import pl.edu.mimuw.forum.ui.operation.OperationChangeResponse;
import pl.edu.mimuw.forum.ui.operation.OperationChangeText;
import pl.edu.mimuw.forum.ui.operation.OperationIsResponseAccepted;

public class SuggestionViewModel extends NodeViewModel {

	public static final String NAME = "Suggestion";
	
	private final SimpleStringProperty responseProperty;
	private final SimpleBooleanProperty isResponseAccepted;
	//private final SimpleBooleanProperty isIndeterminateProperty;

	public SuggestionViewModel(String content, String author, String response) {
		this(new Suggestion(content, author, response));
	}

	public SuggestionViewModel(Suggestion suggestion) {
		super(suggestion);
		
		responseProperty = new SimpleStringProperty(suggestion.getResponse());

		isResponseAccepted = new SimpleBooleanProperty(
				Optional.ofNullable(suggestion.getIsResponseAccepted()).orElse(false));
		if(MainPaneController.redoFlag == 0)responseProperty.addListener((observable, oldValue, newValue) -> {
			OperationChangeResponse change = new OperationChangeResponse(this, oldValue, newValue);
			MainPaneController.history.add(change);
		});
		if(MainPaneController.redoFlag == 0)isResponseAccepted.addListener((observable, oldValue, newValue) -> {
			OperationIsResponseAccepted change = new OperationIsResponseAccepted(this, oldValue, newValue);
			MainPaneController.history.add(change);
		});
		//isIndeterminateProperty = new SimpleBooleanProperty(sugestia.isAutorUsatysfakcjonowany() == null);
	}
	
	public StringProperty getResponse() {
		return responseProperty;
	}
	
	public BooleanProperty getIsResponseAccepted() {
		return isResponseAccepted;
	}
	
	@Override
	protected Node createDocument() {
		Suggestion s = new Suggestion(getContent().get(), getAuthor().get(), responseProperty.get());
		s.setIsResponseAccepted(isResponseAccepted.get());
		return s;
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
