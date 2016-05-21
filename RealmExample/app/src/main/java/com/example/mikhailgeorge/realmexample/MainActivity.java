package com.example.mikhailgeorge.realmexample;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mikhailgeorge.realmexample.model.ToDoItem;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

import com.bumptech.glide.Glide;
public class MainActivity extends AppCompatActivity {

    private static final int[] COLORS = new int[] {
            Color.argb(255, 28, 160, 170),
            Color.argb(255, 99, 161, 247),
            Color.argb(255, 13, 79, 139),
            Color.argb(255, 89, 113, 173),
            Color.argb(255, 200, 213, 219),
            Color.argb(255, 99, 214, 74),
            Color.argb(255, 205, 92, 92),
            Color.argb(255, 105, 5, 98)
    };

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAndShowInputDialog();
            }
        });

        resetRealm();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        realm = Realm.getInstance(realmConfig);
        RealmResults<ToDoItem> ToDoItems = realm
                .where(ToDoItem.class)
                .findAllSorted("id", Sort.ASCENDING);
        ToDoRealmAdapter toDoRealmAdapter =
                new ToDoRealmAdapter(this, ToDoItems, true, true);
        RealmRecyclerView realmRecyclerView =
                (RealmRecyclerView) findViewById(R.id.realm_recycler_view);
        realmRecyclerView.setAdapter(toDoRealmAdapter);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    private void buildAndShowInputDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create A Task");

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.to_do_dialog_view, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);
        final EditText inputURL = (EditText) dialogView.findViewById(R.id.urlInput);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //addToDoItem(input.getText().toString());
                addToDoItem(input.getText().toString(), inputURL.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.show();
        input.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                                (event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            dialog.dismiss();
                            addToDoItem(input.getText().toString(), inputURL.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });

        inputURL.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                                (event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            dialog.dismiss();
                            addToDoItem(input.getText().toString(), inputURL.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });

    }


    private void addToDoItem(String ToDoItemText, String url) {
        if ((ToDoItemText == null || ToDoItemText.length() == 0) || ((url == null || url.length() == 0))) {
            Toast
                    .makeText(this, "Please fill both fields", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        realm.beginTransaction();
        ToDoItem ToDoItem = realm.createObject(ToDoItem.class);
        ToDoItem.setId(System.currentTimeMillis());
        ToDoItem.setDescription(ToDoItemText);
        ToDoItem.setURL(url);
        realm.commitTransaction();
    }

//    private void removeToDoItem(int position) {
//        if (position <  0) {
//            Toast
//                    .makeText(this, "Empty ToDos don't get stuff done!", Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//
//        realm.beginTransaction();
//        ToDoItem ToDoItem = realm.createObject(ToDoItem.class);
//        ToDoItem.setId(System.currentTimeMillis());
//       // ToDoItem.setDescription(ToDoItemText);
//        realm.commitTransaction();
//    }

    public class ToDoRealmAdapter
            extends RealmBasedRecyclerViewAdapter<ToDoItem, ToDoRealmAdapter.ViewHolder> {

        private Context mContext;

        public class ViewHolder extends RealmViewHolder {

            public TextView todoTextView;
            public ImageView imageView;
            public ViewHolder(FrameLayout container) {
                super(container);
                this.todoTextView = (TextView) container.findViewById(R.id.todo_text_view);
                this.imageView = (ImageView) container.findViewById(R.id.imageView);
            }
        }

        public ToDoRealmAdapter(
                Context context,
                RealmResults<ToDoItem> realmResults,
                boolean automaticUpdate,
                boolean animateResults) {

            super(context, realmResults, automaticUpdate, animateResults);
            mContext = context;
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.to_do_item_view, viewGroup, false);
            ViewHolder vh = new ViewHolder((FrameLayout) v);
            return vh;
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final ToDoItem ToDoItem = realmResults.get(position);

           // System.out.println(ToDoItem.getURL());
            //put glide code here
            Glide.with(viewHolder.imageView.getContext())
                   // .load(ToDoItem.getURL())
                    .load("http://goo.gl/gEgYUd")
                    //@android:drawable/sym_def_app_icon
                   // .placeholder(R.drawable.sym_def_app_icon)
                    .into(viewHolder.imageView);

            viewHolder.todoTextView.setText(ToDoItem.getDescription());
            viewHolder.itemView.setBackgroundColor(
                    COLORS[(int) (ToDoItem.getId() % COLORS.length)]
            );
        }
    }

    private void resetRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.deleteRealm(realmConfig);
    }
}
