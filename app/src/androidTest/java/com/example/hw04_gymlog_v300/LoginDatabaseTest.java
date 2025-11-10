package com.example.hw04_gymlog_v300;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hw04_gymlog_v300.database.AppDataBase;
import com.example.hw04_gymlog_v300.database.UserDAO;
import com.example.hw04_gymlog_v300.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class LoginDatabaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserDAO mUserDao;
    private AppDataBase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDataBase.class)
                .allowMainThreadQueries() // Allows us to run queries on the test thread
                .build();
        mUserDao = mDb.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void insertAndGetUserByUsername() throws Exception {
        // 1. Create and insert a user
        User user = new User("admin1", "admin1");
        mUserDao.insert(user);

        // 2. Query for the user
        User userFromDb = com.example.hw04_gymlog_v300.LiveDataTestUtil.getOrAwaitValue(mUserDao.getUserByUsername("admin1"));

        // 3. Assert the user was found and is correct
        assertNotNull(userFromDb);
        assertEquals(user.getUsername(), userFromDb.getUsername());
        assertEquals(user.getPassword(), userFromDb.getPassword());
    }
}