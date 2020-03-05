package telran.ProPets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.ProPets.dto.PageDto;
import telran.ProPets.dto.PostDto;
import telran.ProPets.dto.PostResponseDto;
import telran.ProPets.service.PostService;

@RestController
@RequestMapping("/{lang}/message/v1")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@PostMapping("/{login:.*}")
	public PostResponseDto post(@PathVariable String login, @RequestBody PostDto postDto, @RequestHeader(value = "X-token") String token) {
		return postService.post(login, postDto);
	}

	@GetMapping("/{id:.*}")
	public PostResponseDto getPostById(@PathVariable String id, @RequestHeader(value = "X-token") String token) {
		return postService.getPostById(id);
	}
	
	@PutMapping("/{id:.*}")
	public PostResponseDto updatePost(@PathVariable String id, @RequestBody PostResponseDto postDto, @RequestHeader(value = "X-token") String token) {
		return postService.updatePost(id, postDto);
	}
	
	@DeleteMapping("/{id:.*}")
	public PostResponseDto deletePost(@PathVariable String id, @RequestHeader(value = "X-token") String token) {
		return postService.deletePost(id);
	}
	
	@GetMapping("/view")
	public PageDto getPosts(@RequestParam Integer itemsOnPage, @RequestParam Integer currentPage, @RequestHeader(value = "X-token") String token) {
		return postService.getPosts(itemsOnPage, currentPage);
	}
}
