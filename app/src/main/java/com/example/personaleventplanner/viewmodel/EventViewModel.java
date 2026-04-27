package com.example.personaleventplanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.personaleventplanner.data.local.EventRepository;
import com.example.personaleventplanner.data.local.Event;
import com.example.personaleventplanner.data.local.EventDatabase;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private final EventRepository repository;
    private final LiveData<List<Event>> allEvents;

    public EventViewModel(@NonNull Application application) {
        super(application);
        EventDatabase database = EventDatabase.getDatabase(application);
        repository = new EventRepository(database.eventDao());
        allEvents = repository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }

    public void updateEvent(Event event) {
        repository.updateEvent(event);
    }

    public void deleteEvent(Event event) {
        repository.deleteEvent(event);
    }

    public void getEventById(int eventId, EventRepository.OnEventLoadedListener listener) {
        repository.getEventById(eventId, listener);
    }
}