package telran.ProPets.security.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import telran.ProPets.configuration.AccountConfiguration;
import telran.ProPets.dao.UserAccountRepository;
import telran.ProPets.model.UserAccount;
import telran.ProPets.service.UserAccountCredentials;

@Service

public class AuthenticationFilter implements Filter {

	@Autowired
	AccountConfiguration configuration;
	@Autowired
	UserAccountRepository repository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		String auth = request.getHeader("Authorization");

		if (!checkPointCut(path, method)) {
			String sessionId = request.getSession().getId();
			if (sessionId != null && auth == null) {
				UserAccount userAccount = configuration.getUser(sessionId);
				if (userAccount != null) {
					chain.doFilter(new WrapperRequest(request, userAccount.getEmail()), response);
					return;
				}
			}

			UserAccountCredentials credentials = null;
			try {
				credentials = configuration.tokenDecode(auth);
			} catch (Exception e) {
				response.sendError(401, "Header Authorization is not valid");
				return;
			}
			UserAccount userAccount = repository.findById(credentials.getLogin()).orElse(null);
			if (userAccount == null) {
				response.sendError(401, "User not found");
				return;
			}
			if (!BCrypt.checkpw(credentials.getPassword(), userAccount.getPassword())) {
				response.sendError(403, "Password incorrect");
				return;
			}
			configuration.addUser(sessionId, userAccount);
			chain.doFilter(new WrapperRequest(request, credentials.getLogin()), response);
			return;
		}
		chain.doFilter(request, response);
	}

	private boolean checkPointCut(String path, String method) {
		boolean check = path.matches("/\\w*/account/v1") && "Post".equalsIgnoreCase(method);
		check = check || path.startsWith("/h2");
		return check;
	}

	private class WrapperRequest extends HttpServletRequestWrapper {

		String user;

		public WrapperRequest(HttpServletRequest request, String user) {
			super(request);
			this.user = user;
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() { // or return () -> user;

				@Override
				public String getName() {
					return user;
				}
			};
		}
	}
}
