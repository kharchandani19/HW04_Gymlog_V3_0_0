package com.example.hw04_gymlog_v300.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.hw04_gymlog_v300.MainActivity;
import com.example.hw04_gymlog_v300.database.entities.GymLog;
import com.example.hw04_gymlog_v300.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class GymLogRepository {

    private final GymLogDAO mGymLogDAO;
    private final UserDAO mUserDAO;
    private static GymLogRepository repository;

    private GymLogRepository(Application application) {
        AppDataBase db = AppDataBase.getDatabase(application);
        mGymLogDAO = db.gymLogDAO();
        mUserDAO = db.userDAO();
    }

    public static GymLogRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }

        Future<GymLogRepository> future = AppDataBase.databaseWriteExecutor.submit(
                new Callable<GymLogRepository>() {
                    @Override
                    public GymLogRepository call() throws Exception {
                        return new GymLogRepository(application);
                    }
                }
        );

        try {
            repository = future.get();
            return repository;
        } catch (ExecutionException | InterruptedException e) {
            Log.d(MainActivity.TAG, "Problem getting GymLog repository.");
            e.printStackTrace();
        }
        return null;
    }


    public void insert(GymLog gymLog) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mGymLogDAO.insert(gymLog);
        });
    }
    public LiveData<List<GymLog>> getAllLogsByUserIdLiveData(int userId) {
        return mGymLogDAO.getRecordsByUserIdLiveData(userId);
    }

    public void insert(User... users) {
        AppDataBase.databaseWriteExecutor.execute(() -> {
            mUserDAO.insert(users);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return mUserDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserById(int userId) {
        return mUserDAO.getUserById(userId);
    }

    public ArrayList<GymLog> getAllLogs() {
        Future<List<GymLog>> future = AppDataBase.databaseWriteExecutor.submit(
                new Callable<List<GymLog>>() {
                    @Override
                    public List<GymLog> call() throws Exception {
                        return mGymLogDAO.getAllRecords();
                    }
                }
        );

        try {
            return new ArrayList<>(future.get());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(MainActivity.TAG, "Problem when getting all gym logs", e);
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<GymLog> getAllLogsByUserId(int loggedInUserId) {
        Future<List<GymLog>> future = AppDataBase.databaseWriteExecutor.submit(
                new Callable<List<GymLog>>() {
                    @Override
                    public List<GymLog> call() throws Exception {
                        return mGymLogDAO.getRecordsByUserId(loggedInUserId);
                    }
                }
        );

        try {
            return new ArrayList<>(future.get());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(MainActivity.TAG, "Problem when getting all gym logs by user", e);
            e.printStackTrace();
        }
        return null;
    }
}