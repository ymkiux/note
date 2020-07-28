package wooyun.note.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import wooyun.note.R;
import wooyun.note.db.DbManager;
import wooyun.note.model.Write;


public class WriteEtActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText titleEt;
    private EditText contentEt;
    private FloatingActionButton saveBtn;
    private int noteID = -1;
    private DbManager dbManager;
    private Toolbar titleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        dbManager = new DbManager(this);
        titleLayout = findViewById(R.id.title);
        titleEt = (EditText) findViewById(R.id.note_title);
        contentEt = (EditText) findViewById(R.id.note_content);
        saveBtn = (FloatingActionButton) findViewById(R.id.save);
        saveBtn.setOnClickListener(this);
        noteID = getIntent().getIntExtra("id", -1);
        if (noteID != -1) {
            showNoteData(noteID);
        }
    }


    private void showNoteData(int id) {
        Write note = dbManager.readData(id);
        titleEt.setText(note.getTitle());
        contentEt.setText(note.getContent());
        Spannable spannable = titleEt.getText();
        Selection.setSelection(spannable, titleEt.getText().length());
    }

    @Override
    public void onClick(View view) {
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        String time = getTime();
        if (!title.isEmpty()&&!content.isEmpty()) {
            if (noteID == -1) {
                dbManager.addToDB(title, content, time);
            } else {
                dbManager.updateNote(noteID, title, content, time);
            }
            Intent i = new Intent(WriteEtActivity.this, WriteActivity.class);
            startActivity(i);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            Toast.makeText(this, R.string.no_empty, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(WriteEtActivity.this, WriteActivity.class);
            startActivity(i);
        }
        this.finish();
    }


    private String getTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        String time = format.format(curDate);
        return time;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WriteEtActivity.this, WriteActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }
}
