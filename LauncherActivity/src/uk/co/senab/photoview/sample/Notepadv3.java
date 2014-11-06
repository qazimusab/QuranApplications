package uk.co.senab.photoview.sample;


//import com.google.analytics.tracking.android.EasyTracker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.qaziconsultancy.thirteenlinequran.R;

public class Notepadv3 extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ALL = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private NotesDbAdapter mDbHelper;
    Boolean runAd;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }
    
    
    @Override
	public void onStart() {
	    super.onStart();
	   
	   EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  @Override
	public void onStop() {
	    super.onStop();
	    
	   EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }

    private void fillData() {
        Cursor notesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(notesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{NotesDbAdapter.KEY_TITLE};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = 
            new SimpleCursorAdapter(this, R.layout.notes_row, notesCursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       // menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(0, DELETE_ALL, 0, R.string.delete_all);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            /*case INSERT_ID:
                createNote();
                return true;*/
            case DELETE_ALL:
            	mDbHelper.deleteAllBookmark();
            	fillData();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(Notepadv3.this,ViewPagerActivity.class);
        Cursor note = mDbHelper.fetchNote(id);
        startManagingCursor(note);
        i.putExtra("PAGE", note.getString(
                note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        Log.e("page", note.getString(
                note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    @Override
	protected void onSaveInstanceState(Bundle outState)
	{
	  super.onSaveInstanceState(outState);	 

	  //outState.putBoolean("submitOn", submitOn);
	  //outState.putBoolean("autofillOn", autofillOn);
	}
	//Restore instance state
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		
	}
}
