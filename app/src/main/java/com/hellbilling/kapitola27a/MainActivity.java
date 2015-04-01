package com.hellbilling.kapitola27a;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ListActivity {
    private static final String[] items={"lorem", "ipsum", "dolor",
            "sit", "amet", "consectetuer", "adipiscing", "elit",
            "morbi", "vel", "ligula", "vitae", "arcu", "aliquet",
            "mollis", "etiam", "vel", "erat", "placerat", "ante",
            "porttitor", "sodales", "pellentesque", "augue", "purus"};
    private ArrayList<String> words=null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        initAdapter();
        registerForContextMenu(getListView());
        // toto povoli stlacatelnu ikonu aktivity v lavom hornom rohu
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // volanie customizovaneho layoutu pre TextEdit add z menu_main.xml
        EditText add=(EditText)menu
                .findItem(R.id.add)
                .getActionView()
                .findViewById(R.id.title);

        // ak na textedite add stlacime enter tak volame toto
        add.setOnEditorActionListener(onSearch);

        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(this).inflate(R.menu.context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                add();
                return(true);

            case R.id.reset:
                initAdapter();
                return(true);

            case R.id.about:
                Toast.makeText(this, "stlacene about", Toast.LENGTH_LONG).show();
                //aby toto fungovalo musel som dat do onCreate() toto: getActionBar().setDisplayHomeAsUpEnabled(true);
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this); // toto by malo navigovat snad domov, treba pospekulovat
                Toast.makeText(this, "stlaceny home", Toast.LENGTH_LONG).show();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        ArrayAdapter<String> adapter=(ArrayAdapter<String>)getListAdapter();

        switch (item.getItemId()) {
            case R.id.cap:
                String word=words.get(info.position);

                word=word.toUpperCase();

                adapter.remove(words.get(info.position));
                adapter.insert(word, info.position);

                return(true);

            case R.id.remove:
                adapter.remove(words.get(info.position));

                return(true);
        }

        return(super.onContextItemSelected(item));
    }

    private void initAdapter() {
        words=new ArrayList<String>();

        for (String s : items) {
            words.add(s);
        }

        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, words));
    }

    private void add() {
        // toto je ked z onOptionsItemSelected vyberieme add
        Toast.makeText(this, "volame add()", Toast.LENGTH_LONG).show();
        final View addView=getLayoutInflater().inflate(R.layout.add, null);

        new AlertDialog.Builder(this)
                .setTitle("Add a Word")
                .setView(addView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                addWord((TextView)addView.findViewById(R.id.title));
                            }
                        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addWord(TextView title) {
        ArrayAdapter<String> adapter=(ArrayAdapter<String>)getListAdapter();

        String text2add = title.getText().toString();
        adapter.add(text2add);
        Toast.makeText(this, text2add + " added into the list", Toast.LENGTH_LONG).show();
        // Clear the text
        title.setText("");
    }

    // toto sa zavola ked stlacime enter
    // onSearch je teraz listener a vola sa v onCreateOptionsMenu
    private TextView.OnEditorActionListener onSearch=
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    if (event==null || event.getAction()==KeyEvent.ACTION_UP) {
                        addWord(v);

                        InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    return(true);
                }
            };
}
