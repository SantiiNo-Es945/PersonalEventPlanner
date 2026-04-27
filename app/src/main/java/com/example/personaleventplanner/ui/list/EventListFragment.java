package com.example.personaleventplanner.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.data.local.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;

import java.util.List;

public class EventListFragment extends Fragment {

    private RecyclerView recyclerViewEvents;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;

    public EventListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext()));

        eventAdapter = new EventAdapter();
        recyclerViewEvents.setAdapter(eventAdapter);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                eventAdapter.setEventList(events);
            }
        });

        eventAdapter.setOnDeleteClickListener(new EventAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Event event) {
                eventViewModel.deleteEvent(event);
                Toast.makeText(requireContext(), "Event deleted", Toast.LENGTH_SHORT).show();
            }
        });

        eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                Bundle bundle = new Bundle();
                bundle.putInt("eventId", event.getId());
                Navigation.findNavController(view).navigate(R.id.action_eventListFragment_to_editEventFragment, bundle);
            }
        });

        return view;
    }
}