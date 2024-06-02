package com.example.todoandroid;

import java.util.Comparator;

import javax.inject.Inject;

public class TaskSortingStrategyFactory {
    @Inject
    public TaskSortingStrategyFactory() {}

    public Comparator<Task> getSortingStrategy(SortingMode sortingMode) {
        if (sortingMode == SortingMode.Deadline) {
            return new TaskSortByDeadline();
        } else if (sortingMode == SortingMode.DateAdded) {
            return new TaskSortByDateAdded();
        } else if (sortingMode == SortingMode.Importance) {
            return new TaskSortByImportance();
        } else {
            throw new IllegalArgumentException("Unable to find the strategy for given task sorting mode: " + sortingMode);
        }
    }

    public static class TaskSortByDeadline implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getDeadline() == null) return Constants.FIRST_ARGUMENT_GOES_LAST;
            if (t2.getDeadline() == null) return Constants.FIRST_ARGUMENT_GOES_FIRST;
            return t1.getDeadline().compareTo(t2.getDeadline());
        }
    }
    public static class TaskSortByDateAdded implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getDateAdded().compareTo(t2.getDateAdded());
        }
    }
    public static class TaskSortByImportance implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getPriority() == Task.TaskPriority.IMPORTANT
                    && t2.getPriority() == Task.TaskPriority.NORMAL) {
                return Constants.FIRST_ARGUMENT_GOES_FIRST;
            } else if (t1.getPriority() == Task.TaskPriority.NORMAL
                    && t2.getPriority() == Task.TaskPriority.IMPORTANT) {
                return Constants.FIRST_ARGUMENT_GOES_LAST;
            } else {
                return Constants.ARGUMENTS_ARE_EQUAL;
            }
        }
    }

    public enum SortingMode {
        DateAdded,
        Deadline,
        Importance
    }
}
