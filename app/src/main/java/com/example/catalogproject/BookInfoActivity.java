package com.example.catalogproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.catalogproject.Logic.Book;
import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.sync.SyncDeleteResult;

/**
 * Activity for displaying info about a book from the parent BookListActivity.
 * Includes button to remove book from collection entirely.
 */
public class BookInfoActivity extends AppCompatActivity {
    /**
     * The RemoteMongoCollection created in MainActivity for use making remove requests.
     */
    private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();

    /**
     * The book currently being displayed.
     */
    private Book book;

    /**
     * Called by system when activity is being initialized.
     * @param savedInstanceState (unused) the information from the previous instance of BookInfoActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Bundle bundle = getIntent().getExtras();
        book = (Book) bundle.getSerializable("book");

        TextView titleAuthor = findViewById(R.id.titleAuthorView);
        titleAuthor.setText(book.toString());

        TextView genre = findViewById(R.id.genreView);
        genre.setText(book.getGenreAsString());

        TextView description = findViewById(R.id.descriptionView);
        description.setText(book.getDescription());

        Button removeButton = findViewById(R.id.removeFromDatabaseButton);
        removeButton.setOnClickListener(v -> removeBook());

    }

    /**
     * Permanently removes a book from the collection.
     * This is irreversible.
     */
    private void removeBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmation);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.remove, (unused1, unused2) -> {
            // First create remove query
            BasicDBObject query = new BasicDBObject("title", book.getTitle());
            query.put("author", book.getAuthor());
            query.put("genre", book.getGenreAsString().toLowerCase());
            query.put("description", book.getDescription());
            final Task<SyncDeleteResult> deleteTask = mongoCollection.sync().deleteOne(query);
            deleteTask.addOnCompleteListener(task -> {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                if (task.isSuccessful()) {
                    builder2.setMessage("Book removed!");
                    builder2.setOnDismissListener(v -> finish());
                } else {
                    builder2.setMessage("Could not remove book");
                }
                builder2.create().show();
            });
        });
        builder.create().show();
    }
}
