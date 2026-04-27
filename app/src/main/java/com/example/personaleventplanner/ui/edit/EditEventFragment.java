package com.example.personaleventplanner.ui.edit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.data.EventRepository;
import com.example.personaleventplanner.data.local.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditEventFragment extends Fragment {

    private EditText editTextEditTitle, editTextEditCategory, editTextEditLocation;
    private Button buttonEditPickDateTime, buttonUpdateEvent;
    private TextView textViewEditSelectedDateTime;

    private EventViewModel eventViewModel;
    private Calendar selectedCalendar;
    private int eventId = -1;

    public EditEventFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        editTextEditTitle = view.findViewById(R.id.editTextEditTitle);
        editTextEditCategory = view.findViewById(R.id.editTextEditCategory);
        editTextEditLocation = view.findViewById(R.id.editTextEditLocation);
        buttonEditPickDateTime = view.findViewById(R.id.buttonEditPickDateTime);
        buttonUpdateEvent = view.findViewById(R.id.buttonUpdateEvent);
        textViewEditSelectedDateTime = view.findViewById(R.id.textViewEditSelectedDateTime);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        selectedCalendar = Calendar.getInstance();

        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }

        if (eventId != -1) {
            eventViewModel.getEventById(eventId, new EventRepository.OnEventLoadedListener() {
                @Override
                public void onEventLoaded(Event event) {
                    if (event != null && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            editTextEditTitle.setText(event.getTitle());
                            editTextEditCategory.setText(event.getCategory());
                            editTextEditLocation.setText(event.getLocation());

                            selectedCalendar.setTimeInMillis(event.getDateTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            textViewEditSelectedDateTime.setText(sdf.format(selectedCalendar.getTime()));
                        });
                    }
                }
            });
        }

        buttonEditPickDateTime.setOnClickListener(v -> showDatePicker());
        buttonUpdateEvent.setOnClickListener(v -> updateEvent(view));

        return view;
    }

    private void showDatePicker() {
        Calendar currentCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (datePicker, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    showTimePicker();
                },
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar currentCalendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (timePicker, hourOfDay, minute) -> {
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE, minute);
                    selectedCalendar.set(Calendar.SECOND, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    textViewEditSelectedDateTime.setText(sdf.format(selectedCalendar.getTime()));
                },
                currentCalendar.get(Calendar.HOUR_OF_DAY),
                currentCalendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void updateEvent(View view) {
        String title = editTextEditTitle.getText().toString().trim();
        String category = editTextEditCategory.getText().toString().trim();
        String location = editTextEditLocation.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (textViewEditSelectedDateTime.getText().toString().equals("No date selected")) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        long selectedTimeMillis = selectedCalendar.getTimeInMillis();

        if (selectedTimeMillis < System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "Date cannot be in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        Event updatedEvent = new Event(title, category, location, selectedTimeMillis);
        updatedEvent.setId(eventId);

        eventViewModel.updateEvent(updatedEvent);

        Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(view).navigateUp();
    }
}