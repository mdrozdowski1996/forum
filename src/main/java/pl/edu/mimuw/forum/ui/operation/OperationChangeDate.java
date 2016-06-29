package pl.edu.mimuw.forum.ui.operation;

import pl.edu.mimuw.forum.ui.models.TaskViewModel;
import java.util.Date;

public class OperationChangeDate extends Operation{
	private TaskViewModel task;
	private Date before;
	private Date after;
	public OperationChangeDate(TaskViewModel task, Date before, Date after){
		this.task = task;
		this.before = before;
		this.after = after;
		this.reverse = new OperationChangeDate(this);
	}
	public OperationChangeDate(OperationChangeDate operation){
		this.task = operation.getTask();
		this.before = operation.getAfter();
		this.after = operation.getBefore();
		this.reverse = operation;
		
	}
	public TaskViewModel getTask(){
		return task;
	}
	public Date getAfter(){
		return after;
	}
	public Date getBefore(){
		return before;
	}
	
	@Override
	public void executed() {
		task.getDueDate().set(after);
	}

}
