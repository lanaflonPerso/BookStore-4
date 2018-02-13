package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.Language;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(Arquillian.class)
public class BookRepositoryTest {

    // Attributes
    private static Long bookId;

    // Injection Points
    @Inject
    private BookRepository bookRepository;

    // Deployment
    @Deployment
    public static Archive<?> createDeploymentPackage() {

        return ShrinkWrap.create(JavaArchive.class)
            .addClass(Book.class)
            .addClass(Language.class)
            .addClass(BookRepository.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    // Test methods
    @Test
    @InSequence(1)
    public void shouldBeDeployed() {
        assertNotNull(bookRepository);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoBook() {
        // Count all
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        // Find all
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test
    @InSequence(3)
    public void shouldCreateABook() {
        // Creates a book
        Book book = new Book("title", "description", 12F, "isbn", new Date(),123, "imageURL", Language.ENGLISH);
        book = bookRepository.create(book);
        // Checks the created book
        assertNotNull(book);
        assertNotNull(book.getId());
        bookId = book.getId();
    }

    @Test
    @InSequence(4)
    public void shouldFindTheCreatedBook() {
        // Finds the book
        Book bookFound = bookRepository.find(bookId);
        // Checks the found book
        assertNotNull(bookFound.getId());
        assertEquals("title", bookFound.getTitle());
    }

    @Test
    @InSequence(5)
    public void shouldGetOneBook() {
        // Count all
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        // Find all
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @InSequence(6)
    public void shouldDeleteTheCreatedBook() {
        // Deletes the book
        bookRepository.delete(bookId);
        // Checks the deleted book
        Book bookDeleted = bookRepository.find(bookId);
        assertNull(bookDeleted);
    }

    @Test
    @InSequence(7)
    public void shouldGetNoMoreBook() {
        // Count all
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        // Find all
        assertEquals(0, bookRepository.findAll().size());
    }

    @Test(expected = Exception.class)
    @InSequence(8)
    public void shouldFailCreatingANullBook() {
        bookRepository.create(null);
    }

    @Test(expected = Exception.class)
    @InSequence(9)
    public void shouldFailCreatingABookWithNullTitle() {
        Book book = new Book(null, "description", 12F, "isbn", new Date(),123, "imageURL", Language.ENGLISH);
        bookRepository.create(book);
    }


    @Test(expected = Exception.class)
    @InSequence(10)
    public void shouldFailCreatingABookWithLowUnitCostTitle() {
        Book book = new Book("title", "description", 0F, "isbn", new Date(),123, "imageURL", Language.ENGLISH);
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    @InSequence(11)
    public void shouldFailCreatingABookWithNullISBN() {
        Book book = new Book("title", "description", 12F, null, new Date(),123, "imageURL", Language.ENGLISH);
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    @InSequence(12)
    public void shouldFailInvokingFindByIdWithNull() {
        bookRepository.find(null);
    }

    @Test
    @InSequence(13)
    public void shouldNotFindUnknownId() {
        assertNull(bookRepository.find(99999L));
    }

    @Test(expected = Exception.class)
    @InSequence(14)
    public void shouldFailInvokingDeleteByIdWithNull() {
        bookRepository.delete(null);
    }
    @Test(expected = Exception.class)
    @InSequence(15)
    public void shouldNotDeleteUnknownId() {
        bookRepository.delete(99999L);
    }
}
