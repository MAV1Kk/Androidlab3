package com.example.lab3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChoiceResultFragment extends Fragment {

    public interface ResultActionListener {
        void onResultCancel();
    }

    private static final String RESULT_TEXT_KEY = "result_text_key";
    private static final String SAVE_STATUS_KEY = "save_status_key";

    private ResultActionListener resultActionListener;

    public static ChoiceResultFragment create(String resultText, boolean saved) {
        ChoiceResultFragment fragment = new ChoiceResultFragment();

        Bundle bundle = new Bundle();
        bundle.putString(RESULT_TEXT_KEY, resultText);
        bundle.putBoolean(SAVE_STATUS_KEY, saved);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ResultActionListener) {
            resultActionListener = (ResultActionListener) context;
        } else {
            throw new RuntimeException("Activity must implement ResultActionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choice_result, container, false);

        TextView resultTextView = view.findViewById(R.id.resultTextView);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        String resultText = "";
        boolean saved = false;

        if (getArguments() != null) {
            resultText = getArguments().getString(RESULT_TEXT_KEY, "");
            saved = getArguments().getBoolean(SAVE_STATUS_KEY, false);
        }

        resultTextView.setText(resultText);

        if (saved) {
            Toast.makeText(requireContext(),
                    "Результат успішно записано у файл",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(),
                    "Помилка запису результату",
                    Toast.LENGTH_SHORT).show();
        }

        cancelButton.setOnClickListener(v -> resultActionListener.onResultCancel());

        return view;
    }
}