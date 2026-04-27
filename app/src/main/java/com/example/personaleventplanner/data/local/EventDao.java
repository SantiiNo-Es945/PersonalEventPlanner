package com.example.personaleventplanner.data.local;

// LiveData lets the UI automatically update when data changes
import androidx.lifecycle.LiveData;

// These are Room annotations for database actions
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// @Dao means this interface is used by Room to access the database
@Dao
public interface EventDao {

    // Insert a new event into the database
    @Insert
    void insertEvent(Event event);

    // Update an existing event
    @Update
    void updateEvent(Event event);

    // Delete an event
    @Delete
    void deleteEvent(Event event);

    // Get all events sorted by date (earliest first)
    // LiveData means the UI will update automatically when data changes
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    LiveData<List<Event>> getAllEvents();

    // Get a single event by its id
    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    Event getEventById(int eventId);
}