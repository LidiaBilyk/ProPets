package telran.ProPets.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ProPets.dao.PostRepository;
import telran.ProPets.dto.PageDto;
import telran.ProPets.dto.PostDto;
import telran.ProPets.dto.PostResponseDto;
import telran.ProPets.exceptions.NotFoundException;
import telran.ProPets.model.Post;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	PostRepository postRepository;

	@Override
	public PostResponseDto post(String login, PostDto postDto) {
		Post post = Post.builder()
				.userLogin(login)
				.datePost(LocalDateTime.now())
				.text(postDto.getText())
				.images(postDto.getImages())
				.build();
		post.setId(LocalDateTime.now().toString());
		postRepository.save(post);

		return postToPostResponceDto(post);
	}

	private PostResponseDto postToPostResponceDto(Post post) {		
		return PostResponseDto.builder()
				.id(post.getId())
				.userLogin(post.getUserLogin())
				.text(post.getText())
				.datePost(post.getDatePost())
				.images(post.getImages())
				.build();
	}

	@Override
	public PostResponseDto getPostById(String id) {
		System.out.println("get post");
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());		
		return postToPostResponceDto(post);
	}

	@Override
	public PostResponseDto updatePost(String id, PostResponseDto postResponceDto) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());
		if (postResponceDto.getImages() != null) {
			post.setImages(postResponceDto.getImages());
		}
		if (post.getText() != null) {
			post.setText(postResponceDto.getText());
		}
		postRepository.save(post);
		return postToPostResponceDto(post);
	}

	@Transactional
	@Override		
	public PostResponseDto deletePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());
		PostResponseDto postResponceDto = postToPostResponceDto(post);
		postRepository.deleteById(id);
		return postResponceDto;
	}

	@Override
	public PageDto getPosts(Integer itemsOnPage, Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, itemsOnPage, Sort.by("id").descending());
		Page<Post> page = postRepository.findAll(pageable);
		return pageToPageDto(page);
	}

	private PageDto pageToPageDto(Page<Post> page) {		
		return PageDto.builder()
				.itemsOnPage(page.getNumberOfElements())
				.currentPage(page.getNumber())
				.itemsTotal(page.getTotalElements())
				.posts(page.getContent().stream().map(p -> postToPostResponceDto(p)).collect(Collectors.toList()))
				.build();				
	}

}
