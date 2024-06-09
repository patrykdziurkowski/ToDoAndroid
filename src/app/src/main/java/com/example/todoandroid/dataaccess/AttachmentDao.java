package com.example.todoandroid.dataaccess;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoandroid.domain.Attachment;

import java.util.List;
import java.util.UUID;

@Dao
public interface AttachmentDao {
    @Query("SELECT * FROM attachment")
    List<Attachment> getAttachments();

    @Query("SELECT * FROM attachment WHERE taskId = :id")
    List<Attachment> getAttachmentsByTaskId(UUID id);

    @Query("SELECT * FROM attachment WHERE id = :id")
    Attachment getAttachmentById(UUID id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Attachment attachment);

    @Delete
    void delete(Attachment attachment);
}
