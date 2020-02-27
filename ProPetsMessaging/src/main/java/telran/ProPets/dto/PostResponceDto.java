package telran.ProPets.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;


@Getter
@Builder
public class PostResponceDto {
	String id;
	String userLogin;
	LocalDateTime datePost;	
	String text;
	@Singular
	List<String> images;

}
