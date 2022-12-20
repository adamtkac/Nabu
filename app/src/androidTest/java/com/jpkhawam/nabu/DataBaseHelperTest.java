package com.jpkhawam.nabu;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Not;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DataBaseHelperTest {
    private DataBaseHelper dbhelper;

    /**
     * Set up database before each test.
     */
    @Before
    public void setUpDb() {
        Context context = ApplicationProvider.getApplicationContext();
        dbhelper = new DataBaseHelper(context);
    }

    /**
     * Reset all data in test db after all tests.
     */
    @After
    public void resetDb() {
        ArrayList<Note> noteList = dbhelper.getAllNotes();
        for (Note note : noteList) {
            dbhelper.deleteNote(note);
        }

        dbhelper.emptyTrash();
        dbhelper.emptyArchive();
        dbhelper.resetData();
        dbhelper.close();
    }

    /**
     * Should create simple notes in local DB.
     * Assert that content of simple notes matches note returned from DB.
     */
    @Test
    public void addOneSimpleNote() {
        Note note = new Note();
        note.setTitle("Title");
        note.setContent("Simple note for testing!");
        long noteID = dbhelper.addNote(note);
        assertEquals("Simple note for testing!", dbhelper.getNote(noteID).getContent());
    }

    /**
     * Should create simple notes in local DB.
     * Assert that content of simple notes matches note returned from DB.
     */
    @Test
    public void addNote() {
        Note note = new Note(1, "Title", "Simple note for testing!",
                LocalDateTime.now(), LocalDateTime.now());
        long noteID = dbhelper.addNote(note);
        assertEquals("Simple note for testing!", dbhelper.getNote(noteID).getContent());
    }

    /**
     * Should create two notes in local DB.
     * Assert that DB returns two items.
     */
    @Test
    public void addSomeSimpleNotes() {
        Note note1 = new Note();
        note1.setTitle("Note 1 title");
        note1.setContent("This is content of note nr.1");
        Note note2 = new Note(2, "Note 2 title",
                "Simple note for testing! Nr.2", LocalDateTime.now(), LocalDateTime.now());
        dbhelper.addNote(note1);
        dbhelper.addNote(note2);
        assertEquals(2, dbhelper.getAllNotes().size());
    }

    /**
     * Should return note from local DB.
     * Assert that returned note content matches with setContent string.
     */
    @Test
    public void getNoteFromDB() {
        Note note1 = new Note();
        note1.setTitle("Note 1 title");
        note1.setContent("This is content of note nr.1");
        Note note2 = new Note(2, "Note 2 title",
                "Simple note for testing! Nr.2", LocalDateTime.now(), LocalDateTime.now());
        long note1ID = dbhelper.addNote(note1);
        long note2ID = dbhelper.addNote(note2);
        assertEquals("This is content of note nr.1", dbhelper.getNote(note1ID).getContent());
        assertEquals("Simple note for testing! Nr.2", dbhelper.getNote(note2ID).getContent());
    }

    /**
     * Should update note stored in local DB.
     * Assert that returned note content matches with setContent string.
     */
    @Test
    public void updateNoteFromDB() {
        Note note1 = new Note();
        note1.setTitle("Note 1 title");
        note1.setContent("This is content of note nr.1");
        Note note2 = new Note(2, "Note 2 title",
                "Simple note for testing! Nr.2", LocalDateTime.now(), LocalDateTime.now());
        long note1ID = dbhelper.addNote(note1);
        long note2ID = dbhelper.addNote(note2);
        Note dbNote = dbhelper.getNote(note2ID);
        dbNote.setTitle("Updated Title");
        dbNote.setContent("Updated content of note 2");
        dbhelper.updateNote(dbNote);
        assertEquals("This is content of note nr.1", dbhelper.getNote(note1ID).getContent());
        assertEquals("Updated Title", dbhelper.getNote(note2ID).getTitle());
        assertEquals("Updated content of note 2", dbhelper.getNote(note2ID).getContent());
        assertEquals(note2ID, dbhelper.getAllNotes().get(1).getNoteIdentifier());
    }

    /**
     * Should delete note from notes_table and move it to trash.
     * Assert that notes_table is empty and trash has one item.
     */
    @Test
    public void deleteNote() {
        Note note1 = new Note();
        note1.setTitle("Note 1 title");
        note1.setContent("Simple note for testing! Nr.1");
        long noteID = dbhelper.addNote(note1);
        dbhelper.deleteNote(noteID);
        assertTrue(dbhelper.getAllNotes().isEmpty());
        assertFalse(dbhelper.getAllNotesFromTrash().isEmpty());
    }


}