package com.example.personaleventplanner.data;

import androidx.lifecycle.LiveData;

import com.example.personaleventplanner.data.local.Event;
import com.example.personaleventplanner.data.local.EventDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {

    private final EventDao eventDao;
    private final LiveData<List<Event>> allEvents;
    private final ExecutorService executorService;

    public EventRepository(EventDao eventDao) {
        this.eventDao = eventDao;
        this.allEvents = eventDao.getAllEvents();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insertEvent(Event event) {
        executorService.execute(() -> eventDao.insertEvent(event));
    }

    public void updateEvent(Event event) {
        executorService.execute(() -> eventDao.updateEvent(event));
    }

    public void deleteEvent(Event event) {
        executorService.execute(() -> eventDao.deleteEvent(event));
    }

    public void getEventById(int eventId, OnEventLoadedListener listener) {
        executorService.execute(() -> {
            Event event = eventDao.getEventById(eventId);
            listener.onEventLoaded(event);
        });
    }

    public interface OnEventLoadedListener {
        void onEventLoaded(Event event);
    }
}