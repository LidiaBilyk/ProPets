package telran.ProPets.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import telran.ProPets.model.Post;

public interface PostRepository extends JpaRepository<Post, String> {
	
	Page<Post> findAll(Pageable pageable);

}
