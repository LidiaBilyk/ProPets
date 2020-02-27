package telran.ProPets.service;

import telran.ProPets.dto.PageDto;
import telran.ProPets.dto.PostDto;
import telran.ProPets.dto.PostResponceDto;


public interface PostService {
	PostResponceDto post (String login, PostDto postDto);
	PostResponceDto getPostById(String id);
	PostResponceDto updatePost(String id, PostResponceDto postDto);
	PostResponceDto deletePost(String id);
	PageDto getPosts(Integer itemsOnPage, Integer currentPage);
	

}
