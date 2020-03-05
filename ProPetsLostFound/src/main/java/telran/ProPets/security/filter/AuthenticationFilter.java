package telran.ProPets.security.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;



@Service
public class AuthenticationFilter implements Filter{

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException, RestClientException {			
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		if (!path.startsWith("/h2")) {
			String auth = request.getHeader("X-Token");
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Token", auth);
			RestTemplate restTemplate = new RestTemplate();			
			try {
				RequestEntity<String> restRequest = new RequestEntity<>(headers, HttpMethod.GET, new URI("http://localhost:8080/en/account/v1/token/validation"));				
				ResponseEntity<String> restResponse = restTemplate.exchange(restRequest, String.class);				
			} catch (URISyntaxException e) {
				response.sendError(400, "Bad request");
			}catch (HttpClientErrorException e) {				
				if (e.getStatusCode() != HttpStatus.OK) {			
					response.sendError(401, "Header Authorization is not valid");
					return;
				}
			}			
		}
		chain.doFilter(request, response);
	}

}
