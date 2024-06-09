package com.example.todoandroid.dataaccess;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todoandroid.domain.Attachment;
import com.example.todoandroid.domain.Task;

@Database(
        version = 2,
        entities = { Task.class, Attachment.class }
)
public abstract class TasksDatabase  extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract AttachmentDao attachmentDao();
}
