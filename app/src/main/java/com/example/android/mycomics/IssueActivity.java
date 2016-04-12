package com.example.android.mycomics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.SeriesActivity;
import com.klinker.android.sliding.SlidingActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IssueActivity extends SlidingActivity {
    List<String> stringIssues;
    ArrayList<Double> doubleIssues = new ArrayList<>();
    List<Series> seriesArray = new ArrayList<>();
    Series currentSeries;
    int index;
    String currentCharacter = "";

    @Override
    public void init(Bundle bundle) {
        setTitle("Issues");
        setPrimaryColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));

        setContent(R.layout.activity_issue);
        Intent intent = getIntent();
        currentSeries = (Series) intent.getSerializableExtra("currentSeries");
        currentCharacter = (String) intent.getSerializableExtra("ComicCharacter");
        readSeries();
        index = -1;
        for (int i = 0; i < seriesArray.size(); i++) {
            if (currentSeries.getSeriesName().equals(seriesArray.get(i).getSeriesName())) {
                index = i;
            }
        }
        doubleIssues = seriesArray.get(index).getIssueArray();
        combineIssueRuns(doubleIssues);
        displayIssues();
        /*myListView = (ListView) findViewById(R.id.issueListView);
        doubleIssues = currentSeries.getIssueArray();
        combineIssueRuns(doubleIssues);
        myAdapter = new ArrayAdapter<>(this, R.layout.issues_layout, stringIssues);
        myListView.setAdapter(myAdapter);
        System.out.println(stringIssues);*/
    }

    private void combineIssueRuns(ArrayList<Double> list) {
        List<String> toReturn = new ArrayList<>();
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            int start = i;
            int size = 1;
            int end = start;
            if (i < list.size() - 2) {
                end = start + 1;
                while (list.get(end) == list.get(end - 1) + 1) {
                    size++;
                    end++;
                    if (end >= list.size()) {
                        break;
                    }
                }
            }
            if (size >= 3) {
                toReturn.add(list.get(start) + "-" + list.get(end - 1));
                i = end - 1;
            } else
                toReturn.add("" + list.get(i));
        }
        stringIssues = toReturn;
        System.out.println(stringIssues);
    }

    public void addIssues(View v) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText txtInput = new EditText(this);
        txtInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogBuilder.setTitle("Add Issues:");
        dialogBuilder.setView(txtInput);
        dialogBuilder.setPositiveButton("Done", null);
        dialogBuilder.setNeutralButton("Add", null);

        final AlertDialog dialogCharacterName = dialogBuilder.create();
        dialogCharacterName.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button add = dialogCharacterName.getButton(AlertDialog.BUTTON_NEUTRAL);
                Button done = dialogCharacterName.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //doubleIssues.add(Double.valueOf(txtInput.getText().toString()));
                        seriesArray.get(index).addIssue(Double.valueOf(txtInput.getText().toString()));
                        combineIssueRuns(doubleIssues);
                        txtInput.setText("");
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //For removing duplicates
                        Object[] st = doubleIssues.toArray();
                        for (Object s : st) {
                            if (doubleIssues.indexOf(s) != doubleIssues.lastIndexOf(s)) {
                                doubleIssues.remove(doubleIssues.lastIndexOf(s));
                            }
                        }
                        //end dup removal
                        Collections.sort(doubleIssues);
                        combineIssueRuns(doubleIssues);
                        saveIssues();
                        displayIssues();
                        dialogCharacterName.dismiss();
                    }
                });
            }
        });
        dialogCharacterName.show();
    }

    public void saveIssues() {
        String filePath = this.getFilesDir().getPath().toString() + "/" + currentCharacter + ".txt";
        File f = new File(filePath);
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(fos);
            out.writeObject(seriesArray);
            out.close();
        } catch (Exception e) {
            Toast.makeText(IssueActivity.this,
                    "SAVEERROR", Toast.LENGTH_LONG).show();
            Log.d("SAVEERROR", e.toString());
        }
    }

    protected void readSeries() {//Reads comicCharacters array from file
        try {
            String filePath = this.getFilesDir().getPath().toString() + "/" + currentCharacter + ".txt";
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);
            seriesArray = (List<Series>) in.readObject();
            in.close();
        } catch (Exception e) {
        }
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, SeriesActivity.class);
        intent.putExtra("currentCharName", currentCharacter);//So we know what ComicCharacter we are dealing with
        this.startActivity(intent);
    }

    public void displayIssues() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        for (int i = 0; i < stringIssues.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(stringIssues.get(i));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            View v = new View(this);
            v.setBackgroundResource(R.color.dark_background);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics())));
            linearLayout.addView(v);


        }
    }
}
