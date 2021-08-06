package apiexample;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface bookRepository extends JpaRepository<Book, Long>{

	List<Book> findByTitleContaining(String name);

	Optional<Book> findByBookId(int bookid);


	

}
