package com.example.hw04_gymlog_v300.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hw04_gymlog_v300.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM " + AppDataBase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " ORDER BY username ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE username = :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE id = :userId")
    LiveData<User> getUserById(int userId);
}