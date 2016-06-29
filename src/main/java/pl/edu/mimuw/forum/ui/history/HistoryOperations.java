package pl.edu.mimuw.forum.ui.history;

import java.util.Stack;

import pl.edu.mimuw.forum.ui.controllers.MainPaneController;
import pl.edu.mimuw.forum.ui.operation.*;

public class HistoryOperations {
	private Stack<Operation> undo;
	private Stack<Operation> redo;
	
	public HistoryOperations(){
		this.undo = new Stack<Operation>();
		this.redo = new Stack<Operation>();
	}
	
	public Stack<Operation> getUndo() {
		return undo;
	}
	public void setUndo(Stack<Operation> undo) {
		this.undo = undo;
	}
	public Stack<Operation> getRedo() {
		return redo;
	}
	public void setRedo(Stack<Operation> redo) {
		this.redo = redo;
	}
	
	public void add(Operation operation){
			System.out.println(MainPaneController.redoFlag);
			if(MainPaneController.redoFlag == 0){
				undo.push(operation);
				redo.removeAllElements();
			}
	}
	
	public void undo(){
		System.out.println("redo  "+redo.size()+"  undo  "+undo.size());
		if(!undo.isEmpty()){
			Operation currentOperation = undo.pop();
			currentOperation.getReverse().executed();
			redo.push(currentOperation);
		}
	}
	
	public void redo(){
		System.out.println("redo  "+redo.size()+"  undo  "+undo.size());
		if(!redo.isEmpty()){
			Operation currentOperation = redo.pop();
			currentOperation.executed();
			undo.push(currentOperation);
		}
	}
	
	public boolean isEmptyRedo(){
		return this.redo.isEmpty();
	}
}
