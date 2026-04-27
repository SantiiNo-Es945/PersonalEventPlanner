package com.example.personaleventplanner.data.local;

// These imports are from Room.
// Room is the local database system used to save data on the phone.
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity tells Room that this class should become a table in the database.
// tableName = "events" means the table will be called events.
@Entity(tableName = "events")
public class Event {

    // This is the unique id for each event.
    // autoGenerate = true means Room creates the id automatically.
    @PrimaryKey(autoGenerate = true)
    private int id;

    // These are the main pieces of information for one event.
    private String title;
    private String category;
    private String location;

    // dateTime is saved as a long number because it is easier
    // to compare dates, sort events, and check if a date is in the past.
    private long dateTime;

    // Constructor:
    // This is used when we create a new event.
    // We do not include id here because Room creates it automatically.
    public Event(String title, String category, String location, long dateTime) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTime = dateTime;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    // This is important when editing an event,
    // because we need to keep the same id to update the correct event.
    public void setId(int id) {
        this.id = id;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Getter for category
    public String getCategory() {
        return category;
    }

    // Getter for location
    public String getLocation() {
        return location;
    }

    // Getter for dateTime
    public long getDateTime() {
        return dateTime;
    }
}