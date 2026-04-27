package com.example.personaleventplanner.ui.add;

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

import com.example.personaleventplanner.R;
import com.example.personaleventplanner.data.local.Event;
import com.example.personaleventplanner.viewmodel.EventViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    private EditText editTextTitle, editTextCategory, editTextLocation;
    private Button buttonPickDateTime, buttonSaveEvent;
    private TextView textViewSelectedDateTime;

    private EventViewModel eventViewModel;
    private Calendar selectedCalendar;

    public AddEventFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextCategory = view.findViewById(R.id.editTextCategory);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        buttonPickDateTime = view.findViewById(R.id.buttonPickDateTime);
        buttonSaveEvent = view.findViewById(R.id.buttonSaveEvent);
        textViewSelectedDateTime = view.findViewById(R.id.textViewSelectedDateTime);

        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        selectedCalendar = Calendar.getInstance();

        buttonPickDateTime.setOnClickListener(v -> showDatePicker());
        buttonSaveEvent.setOnClickListener(v -> saveEvent());

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
                    textViewSelectedDateTime.setText(sdf.format(selectedCalendar.getTime()));
                },
                currentCalendar.get(Calendar.HOUR_OF_DAY),
                currentCalendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void saveEvent() {
        String title = editTextTitle.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (textViewSelectedDateTime.getText().toString().equals("No date selected")) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        long selectedTimeMillis = selectedCalendar.getTimeInMillis();

        if (selectedTimeMillis < System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "Date cannot be in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, selectedTimeMillis);
        eventViewModel.insertEvent(event);

        Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show();

        editTextTitle.setText("");
        editTextCategory.setText("");
        editTextLocation.setText("");
        textViewSelectedDateTime.setText("No date selected");
    }
}