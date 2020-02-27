package telran.ProPets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.ProPets.dto.PageDto;
import telran.ProPets.dto.PostDto;
import telran.ProPets.dto.PostResponceDto;
import telran.ProPets.service.PostService;

@RestController
@RequestMapping("/{lang}/message/v1")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@PostMapping("/{login:.*}")
	public PostResponceDto post(@PathVariable String login, @RequestBody PostDto postDto) {
		return postService.post(login, postDto);
	}

	@GetMapping("/{id:.*}")
	public PostResponceDto getPostById(@PathVariable String id) {
		return postService.getPostById(id);
	}
	
	@PutMapping("/{id:.*}")
	public PostResponceDto updatePost(@PathVariable String id, @RequestBody PostResponceDto postDto) {
		return postService.updatePost(id, postDto);
	}
	
	@DeleteMapping("/{id:.*}")
	public PostResponceDto deletePost(@PathVariable String id) {
		return postService.deletePost(id);
	}
	
	@GetMapping("/view")
	public PageDto getPosts(@RequestParam Integer itemsOnPage, @RequestParam Integer currentPage) {
		return postService.getPosts(itemsOnPage, currentPage);
	}
}
