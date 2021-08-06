package apiexample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Controller {
	
	@Autowired
	private bookRepository books;
	
	@GetMapping("/hello")
	public String hello() {
		return "hello there";
	}
	
	@GetMapping("/books")  // format: http://localhost:8081/books?title= "inputwords"
	public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String title) {
				
		try {
			if(title == null) { //if no parameters passed, output all books
				return new ResponseEntity<>(books.findAll(),HttpStatus.OK);
			}
			else  {
				if(books.findAll() == null) { // if there is no name matched with title input, return no centent
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				else { // if parameter passed and found match books, output the matched books
					return new ResponseEntity<>(books.findByTitleContaining(title),HttpStatus.OK);
				}
				
			}
			
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		}

	@PostMapping("/add/{title}/{published}/{pages}") // == to all parameters required
	public ResponseEntity<Book> addBook(@PathVariable("title") String title, @PathVariable("published") boolean published
										, @PathVariable("pages") int pages){
		Book b = new Book(title, published, pages);
		try {
			books.save(b);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch(Exception e){
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
	}
	
	@PutMapping("/updatebook/{bookid}")
	public ResponseEntity<Book> updateBook(@PathVariable("bookid") int bookid, @RequestBody Book book){
		Optional<Book> oldBook = books.findByBookId(bookid);
		if(oldBook.isPresent()) {
			Book originalBook = oldBook.get();
			originalBook.setTitle(book.getTitle());
			originalBook.setPublished(book.isPublished());
			originalBook.setPages(book.getPages());
			books.save(originalBook);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping("/deletebook")
	public ResponseEntity<Book> deleteBooks(){
		try {
			books.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
		}
	}
	

}
