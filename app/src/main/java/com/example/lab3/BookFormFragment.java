package com.example.lab3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BookFormFragment extends Fragment {

    public interface BookChoiceListener {
        void onBookChoiceConfirmed(String author, String year);
    }

    private BookChoiceListener choiceListener;

    private Spinner authorSpinner;
    private RadioGroup yearRadioGroup;
    private Button okButton;
    private Button openButton;

    private final String[] authors = {
            "Оберіть письменника",
            "Михайло Коцюбинський",
            "Ольга Кобилянська",
            "Панас Мирний",
            "Григорій Сковорода",
            "Ліна Костенко"
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof BookChoiceListener) {
            choiceListener = (BookChoiceListener) context;
        } else {
            throw new RuntimeException("Activity must implement BookChoiceListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_form, container, false);

        authorSpinner = view.findViewById(R.id.authorSpinner);
        yearRadioGroup = view.findViewById(R.id.yearRadioGroup);
        okButton = view.findViewById(R.id.okButton);
        openButton = view.findViewById(R.id.openButton);

        setupAuthorSpinner();

        okButton.setOnClickListener(v -> processUserChoice(view));

        openButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StorageActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void setupAuthorSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.spinner_selected_item,
                authors
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        authorSpinner.setAdapter(adapter);
    }

    private void processUserChoice(View rootView) {
        int authorIndex = authorSpinner.getSelectedItemPosition();
        int checkedYearId = yearRadioGroup.getCheckedRadioButtonId();

        if (authorIndex == 0 && checkedYearId == -1) {
            Toast.makeText(requireContext(),
                    "Оберіть автора та рік видання",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (authorIndex == 0) {
            Toast.makeText(requireContext(),
                    "Оберіть автора книги",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkedYearId == -1) {
            Toast.makeText(requireContext(),
                    "Оберіть рік видання",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedAuthor = authors[authorIndex];

        RadioButton checkedButton = rootView.findViewById(checkedYearId);
        String selectedYear = checkedButton.getText().toString();

        choiceListener.onBookChoiceConfirmed(selectedAuthor, selectedYear);
    }

    public void resetForm() {
        if (authorSpinner != null) {
            authorSpinner.setSelection(0);
        }

        if (yearRadioGroup != null) {
            yearRadioGroup.clearCheck();
        }
    }
}