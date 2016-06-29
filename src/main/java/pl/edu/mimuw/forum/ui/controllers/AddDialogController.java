package pl.edu.mimuw.forum.ui.controllers;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.edu.mimuw.forum.ui.models.CommentViewModel;
import pl.edu.mimuw.forum.ui.models.NodeViewModel;
import pl.edu.mimuw.forum.ui.models.SuggestionViewModel;
import pl.edu.mimuw.forum.ui.models.SurveyViewModel;
import pl.edu.mimuw.forum.ui.models.TaskViewModel;

public class AddDialogController implements Initializable{
	
	
	@FXML
	private Dialog<NodeViewModel> dialog;
	
	@FXML
	private TextField userField;

	@FXML
	private TextArea commentField;
	
	@FXML
	private ComboBox<String>comboBox;
	
	@FXML
	private Label label;
	

	
	private void updateFields(){
		if(comboBox.getValue().equals("Task")){
		//	label.setVisible(true);
		}
		else{
			//label.setVisible(false);
		}
	}

	
	public NodeViewModel resultConverter(ButtonType buttonType){
		if(buttonType == ButtonType.OK){
			return getModel();
		}
		else{
			return null;
		}
	}
	
	private NodeViewModel getModel(){
		String nodeType = comboBox.getValue();
		String comment = commentField.getText();
		String author = userField.getText();
		

		
		if(nodeType.equals("Comment")){
			return new CommentViewModel(comment, author);
		}
		else if(nodeType.equals("Suggestion")){
			return new SuggestionViewModel(comment, author, "");
		}
		else if(nodeType.equals("Survey")){
			return new SurveyViewModel(comment, author);
		}
		else if(nodeType.equals("Task")){
			return new TaskViewModel(comment, author, new Date());
		}
		else{
			throw new NoSuchElementException();
		}
		
		
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		dialog.setResultConverter(this::resultConverter);
        comboBox.setOnAction(e -> {
          updateFields();
		});
		
	}
}
