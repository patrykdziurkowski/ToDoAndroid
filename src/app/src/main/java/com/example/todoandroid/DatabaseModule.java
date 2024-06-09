package com.example.todoandroid;


import android.content.Context;

import androidx.room.Room;

import com.example.todoandroid.dataaccess.TasksDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    private static TasksDatabase INSTANCE;
    private static final Object sLock = new Object();

    @Singleton
    @Provides
    public static TasksDatabase getInstance(@ApplicationContext Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TasksDatabase.class, "Tasks.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }
}
