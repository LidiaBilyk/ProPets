package telran.ProPets.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ProPets.dao.PostRepository;
import telran.ProPets.dto.PageDto;
import telran.ProPets.dto.PostDto;
import telran.ProPets.exceptions.NotFoundException;
import telran.ProPets.model.Post;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	PostRepository postRepository;

	@Override
	public PostDto postLost(String login, PostDto postDto) {
		Post post = createNewPost(login, postDto);
		post.setFound(false);
		postRepository.save(post);
		return postToPostDto(post);
	}
	
	private Post createNewPost(String login, PostDto postDto) {
		return Post.builder()		
		.datePost(LocalDateTime.now())
		.userLogin(login)
		.id(LocalDateTime.now().toString())
		.type(postDto.getType())
		.location(postDto.getLocation())
		.radius(postDto.getRadius())
		.tags(postDto.getTags())
		.photos(postDto.getPhotos())		
		.build();
	}

	@Override
	public PostDto postFound(String login, PostDto postDto) {
		Post post = createNewPost(login, postDto);		
		post.setFound(true);
		postRepository.save(post);
		return postToPostDto(post);
	}

	private PostDto postToPostDto(Post post) {		
		List<String> postList = post.getPhotos();
		List<String> tagList = post.getTags();
		return PostDto.builder()
				.id(post.getId())
				.userLogin(post.getUserLogin())
				.datePost(post.getDatePost())
				.type(post.getType())
				.location(post.getLocation())
				.radius(post.getRadius())
				.tags(tagList)
				.photos(postList)
				.build();
	}

	@Override
	public PostDto getPostById(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());		
		return postToPostDto(post);
	}

	@Override
	public PostDto updatePost(String id, PostDto postDto) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());
		if (postDto.getType() != null) {
			post.setType(postDto.getType());
		}
		if (postDto.getLocation() != null) {
			post.setLocation(postDto.getLocation());
		}
		if (postDto.getRadius() != 0) {
			post.setRadius(postDto.getRadius());
		}
		if (postDto.getTags() != null) {
			post.setTags(postDto.getTags());
		}
		if (postDto.getPhotos() != null) {
			post.setPhotos(postDto.getPhotos());
		}
		postRepository.save(post);
		return postToPostDto(post);
	}

	@Transactional
	@Override
	public PostDto deletePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException());
		postRepository.deleteById(id);
		return postToPostDto(post);
	}

	@Override
	public PageDto getLosts(Integer itemsOnPage, Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
		Page<Post> page = postRepository.findByFoundFalse(pageable);
		return pageToPageDto(page);		
	}
	
	private PageDto pageToPageDto(Page<Post> page) {		
		return PageDto.builder()
				.itemsOnPage(page.getNumberOfElements())
				.currentPage(page.getNumber())
				.itemsTotal(page.getTotalElements())
				.posts(page.getContent().stream().map(p -> postToPostDto(p)).collect(Collectors.toList()))
				.build();
	}

	@Override
	public PageDto getFounds(Integer itemsOnPage, Integer currentPage) {
		Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
		Page<Post> page = postRepository.findByFoundTrue(pageable);
		return pageToPageDto(page);		
	}

	@Override
	public PageDto getMatchingLosts(Integer itemsOnPage, Integer currentPage, PostDto postDto) {
		Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
//		String type = postDto.getType();
//		String country = postDto.getLocation().getCountry();
//		String city = postDto.getLocation().getCity();
//		List<String> tags = postDto.getTags();		
//		Page<Post> page = postRepository.findLostsByFilter(pageable, type, country, city);
		Post post = createPostForSearch(postDto);
		post.setFound(false);		
		Example<Post> example = Example.of(post, createExampleMatcher());
		Page<Post> page = postRepository.findAll(example, pageable);		
		return pageToPageDto(page);

	}

	private ExampleMatcher createExampleMatcher() {
		ExampleMatcher matcher = ExampleMatcher.matchingAll()
				.withIgnorePaths("radius", "location.building", "location.latitude", "location.longitude")								
				.withIgnoreCase();
		return matcher;
	}

	@Override
	public PageDto getMatchingFounds(Integer itemsOnPage, Integer currentPage, PostDto postDto) {
		Pageable pageable = PageRequest.of(currentPage, itemsOnPage);
		Post post = createPostForSearch(postDto);
		post.setFound(true);
		Example<Post> example = Example.of(post, createExampleMatcher());
		Page<Post> page = postRepository.findAll(example, pageable);
		return pageToPageDto(page);
	}


	private Post createPostForSearch(PostDto postDto) {		
		return Post.builder()
				.type(postDto.getType())
				.location(postDto.getLocation())
				.radius(postDto.getRadius())
				.tags(postDto.getTags())
				.build();
	}

}
