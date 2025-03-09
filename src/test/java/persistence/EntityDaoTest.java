//package edu.matc.persistence;

/**
 *


import edu.matc.entity.Book;
import edu.matc.entity.Author;
import edu.matc.testUtils.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Unit test for BookDao
 *
 * @author Paula Waite
 *
class BookDaoTest {

    BookDao dao;
    GenericDao genericDao;

    /**
     * Run set up tasks before each test:
     * 1. execute sql which deletes everything from the table and inserts records)
     * 2. Create any objects needed in the tests
     *
    @BeforeEach
    void setUp() {
        genericDao = new GenericDao();

        Database database = Database.getInstance();
        database.runSQL("cleandb.sql");

        dao = new BookDao();
    }

    /**
     * Verify successful retrieval of a Book
     *
    @Test
    void getByIdSuccess() {
        // Retrieve the book by ID
        Book retrievedBook = (Order)genericDao.getById(1);
        assertNotNull(retrievedBook, "Retrieved book should not be null");

        // Check the book title
        assertEquals("Head First Java, 2nd Edition", retrievedBook.getTitle());

        // Retrieve the Author object and ensure it's not null
        Author author = retrievedBook.getAuthor();
        assertNotNull(author, "Author should not be null");

        // Check the author details
        assertEquals("Kathy", author.getFirstName());  // Check first name of the author
        assertEquals("Sierra", author.getLastName());  // Check last name of the author

        // Check ISBN and publication year
        assertEquals("978-0596009205", retrievedBook.getIsbn());
        assertEquals(2005, retrievedBook.getPublicationYear());
    }

    /**
     * Verify successful insert of a Book
     *
    @Test
    void insertSuccess() {
        // Create an Author object
        Author author = new Author("My Test", "Author");

        // Save the author first (if necessary)
        AuthorDao authorDao = new AuthorDao();
        int authorId = authorDao.insert(author);  // Insert Author into the database

        // Create a Book with the Author object
        Book newBook = new Book("My Test Book", author, "test isbn", 2000);

        // Insert the book into the database
        int id = dao.insert(newBook);
        assertNotEquals(0, id);

        // Retrieve the inserted book and verify its details
        Book insertedBook = dao.getById(id);
        assertEquals("My Test Book", insertedBook.getTitle());

        Author insertedAuthor = insertedBook.getAuthor();
        assertNotNull(insertedAuthor);
        assertEquals(author.getFirstName(), insertedAuthor.getFirstName());
        assertEquals(author.getLastName(), insertedAuthor.getLastName());
    }



    /**
     * Verify successful update of a Book
     *
    @Test
    void updateSuccess() {
        // Retrieve book to update
        Book bookToUpdate = dao.getById(1);

        // Get the current author of the book and remove the association
        Author oldAuthor = bookToUpdate.getAuthor();
        if (oldAuthor != null) {
            oldAuthor.getBooks().remove(bookToUpdate); // Remove the book from the old author's collection
        }

        // Create new Author object for the update
        Author newAuthor = new Author("Sammy", "Indago");

        //New author set on book and save
        AuthorDao authorDao = new AuthorDao();
        authorDao.insert(newAuthor);

        // Set the new author for the book
        bookToUpdate.setAuthor(newAuthor);

        // Update the book in the database
        dao.update(bookToUpdate);

        // Get book after update and verify changes
        Book bookAfterUpdate = dao.getById(1);
        Author updatedAuthor = bookAfterUpdate.getAuthor();

        // Verify author details updated
        assertNotNull(updatedAuthor, "Author should not be null after update.");
        assertEquals(newAuthor.getFirstName(), updatedAuthor.getFirstName());
        assertEquals(newAuthor.getLastName(), updatedAuthor.getLastName());

        // Verify old author no longer has the book
        if (oldAuthor != null) {
            assertFalse(oldAuthor.getBooks().contains(bookToUpdate), "The old author should no longer have this book.");
        }
    }




    /**
     * Verify successful delete of Book
     *
    @Test
    void deleteSuccess() {
        dao.delete(dao.getById(2));
        assertNull(dao.getById(2));
    }

    /**
     * Verify successful retrieval of all Books
     *
    @Test
    void getAllSuccess() {
        List<Book> Books = dao.getAll();
        assertEquals(3, Books.size());
    }
}*/