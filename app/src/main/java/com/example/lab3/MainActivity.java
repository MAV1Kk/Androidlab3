package com.example.lab3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity
        implements BookFormFragment.BookChoiceListener,
        ChoiceResultFragment.ResultActionListener {

    private static final String FILE_NAME = "book_results.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.formFragmentContainer, new BookFormFragment())
                    .commit();
        }
    }

    @Override
    public void onBookChoiceConfirmed(String author, String year) {
        String resultText =
                "Результат взаємодії\n\n" +
                        "Обраний автор: " + author + "\n" +
                        "Рік видання: " + year + "\n\n" +
                        "Дані успішно передано з першого фрагмента до другого.";

        boolean saved = saveResultToFile(author, year);

        ChoiceResultFragment resultFragment =
                ChoiceResultFragment.create(resultText, saved);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.resultFragmentContainer, resultFragment)
                .commit();
    }

    private boolean saveResultToFile(String author, String year) {
        String record =
                "Автор: " + author + "\n" +
                        "Рік видання: " + year + "\n" +
                        "-----------------------------\n";

        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND)) {
            fos.write(record.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void onResultCancel() {
        Fragment resultFragment =
                getSupportFragmentManager().findFragmentById(R.id.resultFragmentContainer);

        if (resultFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(resultFragment)
                    .commit();
        }

        Fragment formFragment =
                getSupportFragmentManager().findFragmentById(R.id.formFragmentContainer);

        if (formFragment instanceof BookFormFragment) {
            ((BookFormFragment) formFragment).resetForm();
        }
    }
}