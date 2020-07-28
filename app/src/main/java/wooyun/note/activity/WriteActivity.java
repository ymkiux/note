package wooyun.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import wooyun.note.R;
import wooyun.note.adapter.WriteAdapter;
import wooyun.note.db.DbManager;
import wooyun.note.model.Write;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton addBtn;
    private DbManager dm;
    private List<Write> noteDataList = new ArrayList<>();
    private WriteAdapter adapter;
    private ListView listView;
    private TextView emptyListTextView;
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_write);
        init();
    }

    private void init() {
        dm = new DbManager(this);
        dm.readFromDB(noteDataList);
        listView = (ListView) findViewById(R.id.list);
        addBtn = (FloatingActionButton) findViewById(R.id.add);
        emptyListTextView = (TextView) findViewById(R.id.empty);
        addBtn.setOnClickListener(this);
        adapter = new WriteAdapter(this, noteDataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new NoteClickListener());
        listView.setOnItemLongClickListener(new NoteLongClickListener());
        updateView();
    }

    private void updateView() {
        if (noteDataList.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyListTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, WriteEtActivity.class);
        switch (view.getId()) {
            case R.id.add:
                startActivity(i);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
        }
    }

    private class NoteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            WriteAdapter.ViewHolder viewHolder = (WriteAdapter.ViewHolder) view.getTag();
            String noteId = viewHolder.tvId.getText().toString().trim();
            Intent intent = new Intent(WriteActivity.this, WriteEtActivity.class);
            intent.putExtra("id", Integer.parseInt(noteId));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }


    private class NoteLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
            final Write note = ((WriteAdapter) adapterView.getAdapter()).getItem(i);
            if (note == null) {
                return true;
            }
            final int id = note.getId();
            DbManager.getInstance(WriteActivity.this).deleteNote(id);
            adapter.removeItem(i);
            updateView();
            return true;
        }
    }


    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            touchTime = currentTime;
        } else {
            finish();
        }
    }
}
