package com.example.todoandroid.dataaccess;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoandroid.domain.Task;

import java.util.List;
import java.util.UUID;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getTasks();

    @Query("SELECT * FROM task WHERE id = :id")
    Task getTaskById(UUID id);

    @Update
    void update(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Delete
    void delete(Task task);
}
