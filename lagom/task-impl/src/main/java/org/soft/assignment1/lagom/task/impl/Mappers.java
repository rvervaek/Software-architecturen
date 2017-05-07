package org.soft.assignment1.lagom.task.impl;

import java.util.UUID;

import org.soft.assignment1.lagom.task.api.Task;
import org.soft.assignment1.lagom.task.api.Task.TaskColor;
import org.soft.assignment1.lagom.task.impl.PTask.PTaskColor;

public final class Mappers {

	public static Task toApi(PTask task) {
        return new Task(
        		task.getId(), 
        		task.getTitle(), 
        		task.getDetails(), 
        		toApi(task.getColor()),
        		task.getStatus().toTaskStatus(), 
        		task.getBoardId());
    }

	public static PTask fromApi(Task task) {
		return new PTask(
				task.getId(), 
				task.getTitle(), 
				task.getDetails(), 
				fromApi(task.getColor()), 
				task.getBoardId());
	}
	
	public static PTask fromApi(UUID id, Task task) {
		return new PTask(
				id, 
				task.getTitle(), 
				task.getDetails(), 
				fromApi(task.getColor()), 
				task.getBoardId());
	}
	
	public static TaskColor toApi(PTaskColor color) {
		if (color == null) {
			return null;
		}
		return new TaskColor(
				color.getRed(), 
				color.getGreen(),
				color.getBlue());
	}
	
	public static PTaskColor fromApi(TaskColor color) {
		if (color == null) {
			return null;
		}
		return new PTaskColor(
				color.getRed(),
				color.getRed(),
				color.getBlue());
	}
}
