package kz.springcourse.demo.service;

import kz.springcourse.demo.model.Book;

import kz.springcourse.demo.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public List<Book> getBooksBySearch(String search){
        return bookRepository.findByNameStartingWith(search);
    }


    public Book findById(Integer id){
        return bookRepository.findById(id).orElse(null);
    }


    public void save(Book book){
        bookRepository.save(book);
    }


    public void update(Book book){
        Book bookToBeUpdated = bookRepository.findById(book.getId()).orElse(null);

        if(bookToBeUpdated != null){
            bookToBeUpdated.setYear(book.getYear());
            bookToBeUpdated.setName(book.getName());
            bookToBeUpdated.setAuthor(book.getAuthor());
            bookToBeUpdated.setOwner(book.getOwner());
        }
    }

    public void delete(Integer id){
        bookRepository.delete(bookRepository.findById(id).orElse(null));
    }

}
