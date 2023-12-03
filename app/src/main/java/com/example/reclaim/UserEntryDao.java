package com.example.reclaim;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserEntryDao {
    @Insert
    void insert(UserEntry userEntry);
    @Update
    void update(UserEntry userDetails);

    @Query("SELECT * FROM user_details_entries WHERE userid = :userId LIMIT 1")
    UserEntry getUserDetails(int userId);



}

