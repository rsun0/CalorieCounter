package com.mycompany.caloriecounter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A calorie counter list user interface
 * Note: the readme.txt file is in the res/values folder
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<String> list1 = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter1;
    private ArrayList<String> list2 = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter2;
    private int target;
    //The default target amount is 2250 calories
    private final int DEFAULT_TARGET = 2250;

    @Override
    /**
     * Initializes the ArrayAdapter and loads any previously saved lists
     * @param savedInstanceState The saved instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);
        ListView listView1 = (ListView) findViewById(R.id.list1);
        listView1.setAdapter(listAdapter1);
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View
                    paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                list1.remove(paramAnonymousInt);
                list2.remove(paramAnonymousInt);
                listAdapter1.notifyDataSetChanged();
                listAdapter2.notifyDataSetChanged();
                updateTotal();
                return true;
            }
        });

        listAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list2);
        ListView listView2 = (ListView) findViewById(R.id.list2);
        listView2.setAdapter(listAdapter2);
        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View
                    paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                list1.remove(paramAnonymousInt);
                list2.remove(paramAnonymousInt);
                listAdapter1.notifyDataSetChanged();
                listAdapter2.notifyDataSetChanged();
                updateTotal();
                return true;
            }
        });

        target = DEFAULT_TARGET;

        //if a previous list has been saved, add it to the array lists
        boolean existingSave = false;
        try {
            Scanner scan1 = new Scanner(openFileInput("caloriecountersave1.txt"));
            Scanner scan2 = new Scanner(openFileInput("caloriecountersave2.txt"));
            Scanner scan3 = new Scanner(openFileInput("caloriecountersave3.txt"));
            while (scan1.hasNextLine()) {
                String line = scan1.nextLine();
                if(!line.equals("")) {
                    list1.add(line);
                    existingSave = true;
                }
            }
            while (scan2.hasNextLine()) {
                String line = scan2.nextLine();
                if(!line.equals("")) {
                    list2.add(line);
                }
            }
            if(scan3.hasNextInt()) {
                target = scan3.nextInt();
            }
        }
        catch (FileNotFoundException e) {

        }

        //display toast
        if(existingSave) {
            Toast.makeText(getApplicationContext(), R.string.load, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.noload, Toast.LENGTH_SHORT).show();
        }

        listAdapter1.notifyDataSetChanged();
        listAdapter2.notifyDataSetChanged();
        updateTotal();
    }

    /**
     * Adds an item to the calorie count list
     * @param view Not used
     */
    public void addItem(View view) {
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        //check if calorie amount is a valid integer
        try {
            Integer.parseInt(editText2.getText().toString());
        }
        catch(NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.number_error, Toast.LENGTH_SHORT).show();
            return;
        }

        EditText editText1 = (EditText) findViewById(R.id.editText1);
        list1.add(editText1.getText().toString());
        editText1.setText("");

        list2.add(editText2.getText().toString());
        editText2.setText("");

        listAdapter1.notifyDataSetChanged();
        listAdapter2.notifyDataSetChanged();
        updateTotal();
    }

    /**
     * Saves the calorie count list to 2 files, clears the screen, and displays a toast
     * @param view Not used
     */
    public void saveList(View view) throws FileNotFoundException {
        //save to file
        PrintStream out1 = new PrintStream(openFileOutput("caloriecountersave1.txt", MODE_PRIVATE));
        for(int i = 0; i < list1.size(); i++) {
            out1.println(list1.get(i));
        }
        out1.close();

        PrintStream out2 = new PrintStream(openFileOutput("caloriecountersave2.txt", MODE_PRIVATE));
        for(int i = 0; i < list2.size(); i++) {
            out2.println(list2.get(i));
        }
        out2.close();

        PrintStream out3 = new PrintStream(openFileOutput("caloriecountersave3.txt", MODE_PRIVATE));
        out3.print(target);
        out3.close();

        //displays toast
        Toast.makeText(getApplicationContext(), R.string.toast_save, Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to the Web Activity that displays the calorie amount lookup website
     * @param view Not used
     */
    public void goToWebView(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        startActivity(intent);
    }

    /**
     * Clears the list and the save files
     * @param view Not used
     */
    public void clear(View view) throws FileNotFoundException {
        //clears the save files
        PrintStream out1 = new PrintStream(openFileOutput("caloriecountersave1.txt", MODE_PRIVATE));
        out1.print("");
        out1.close();

        PrintStream out2 = new PrintStream(openFileOutput("caloriecountersave2.txt", MODE_PRIVATE));
        out2.print("");
        out2.close();

        PrintStream out3 = new PrintStream(openFileOutput("caloriecountersave3.txt", MODE_PRIVATE));
        out3.print("");
        out3.close();

        //clears the screen
        list1.clear();
        list2.clear();
        target = DEFAULT_TARGET;

        listAdapter1.notifyDataSetChanged();
        listAdapter2.notifyDataSetChanged();
        updateTotal();

        //display toast
        Toast.makeText(getApplicationContext(), R.string.toast_clear, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets the daily target amount of calories
     * @param view Not used
     */
    public void setTarget(View view) {
        EditText editText3 = (EditText) findViewById(R.id.editText3);

        //check if calorie amount is a valid integer
        try {
            Integer.parseInt(editText3.getText().toString());
        }
        catch(NumberFormatException e) {
            Toast.makeText(getApplicationContext(), R.string.number_error, Toast.LENGTH_SHORT).show();
            return;
        }

        target = Integer.parseInt(editText3.getText().toString());
        editText3.setText("");

        updateTotal();

        //display toast
        Toast.makeText(getApplicationContext(), R.string.toast_set, Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the total calorie count
     */
    private void updateTotal() {
        //calculate total
        int total = 0;
        for(String amount : list2) {
            total += Integer.parseInt(amount);
        }

        //set text
        TextView textView = (TextView) findViewById(R.id.total);
        textView.setText(Integer.toString(total) + " " + getString(R.string.calories));

        //set color
        //green
        if(Math.abs(target-total) <= 250) {
            textView.setTextColor(Color.rgb(0, 128, 0));
        }
        //yellow
        else if(Math.abs(target-total) <= 500) {
            textView.setTextColor(Color.rgb(128, 128, 0));
        }
        //red
        else {
            textView.setTextColor(Color.rgb(128, 0, 0));
        }
    }
}