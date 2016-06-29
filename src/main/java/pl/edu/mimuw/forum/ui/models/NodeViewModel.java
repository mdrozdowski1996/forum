package pl.edu.mimuw.forum.ui.models;

import java.util.stream.Collectors;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import pl.edu.mimuw.forum.data.Node;
import pl.edu.mimuw.forum.ui.controllers.DetailsPaneController;
import pl.edu.mimuw.forum.ui.controllers.MainPaneController;
import pl.edu.mimuw.forum.ui.operation.OperationChangeAuthor;
import pl.edu.mimuw.forum.ui.operation.OperationChangeContent;

public class NodeViewModel {

	public static final String NAME = "";

	final private StringProperty authorProperty;
	final private StringProperty contentProperty;
	final private ListProperty<NodeViewModel> childrenProperty;

	public NodeViewModel(String content, String author) {
		this(new Node(content, author));
	}

	public NodeViewModel(Node node) {
		authorProperty = new SimpleStringProperty(node.getAuthor());

		contentProperty = new SimpleStringProperty(node.getContent());

		childrenProperty = new SimpleListProperty<NodeViewModel>(FXCollections.observableArrayList(node.getChildren() != null
				? node.getChildren().stream().map(Node::getModel).collect(Collectors.toList()).toArray(new NodeViewModel[0])
				: new NodeViewModel[0]));
		
		if(MainPaneController.redoFlag == 0){authorProperty.addListener((observable, oldValue, newValue) -> {
			OperationChangeAuthor change = new OperationChangeAuthor(this, oldValue, newValue);
			MainPaneController.history.add(change);
		});}
		if(MainPaneController.redoFlag == 0) {contentProperty.addListener((observable, oldValue, newValue) -> {
			OperationChangeContent change = new OperationChangeContent(oldValue, newValue, this);
			MainPaneController.history.add(change);
		});}

	}

	public StringProperty getAuthor() {
		return authorProperty;
	}

	public StringProperty getContent() {
		return contentProperty;
	}

	public ListProperty<NodeViewModel> getChildren() {
		return childrenProperty;
	}

	public Node toNode() {
		Node w = createDocument();
		childrenProperty.forEach(childModel -> w.addChild(childModel.toNode()));
		return w;
	}

	protected Node createDocument() {
		return new Node(contentProperty.get(), authorProperty.get());
	}

	@Override
	public String toString() {
		return this.getName();
	}

	public String getName() {
		return NAME;
	}

	public void presentOn(DetailsPaneController detailsPaneController) {
		detailsPaneController.present(this);
	}

}
