package telran.ProPets.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ProPets.dao.UserAccountRepository;
import telran.ProPets.dto.RolesDto;
import telran.ProPets.dto.UserProfileDto;
import telran.ProPets.dto.UserRegisterDto;
import telran.ProPets.dto.UserRegisterResponceDto;
import telran.ProPets.exceptions.ConflictException;
import telran.ProPets.exceptions.ForbiddenException;
import telran.ProPets.exceptions.NotFoundException;
import telran.ProPets.model.UserAccount;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	UserAccountRepository userAccountRepository;

	@Override
	public UserRegisterResponceDto registerUser(UserRegisterDto userRegisterDto) {
		if (userAccountRepository.existsById(userRegisterDto.getEmail())) {
			throw new ConflictException();
		}
		String hashPassword = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
				.email(userRegisterDto.getEmail())
				.password(hashPassword)
				.name(userRegisterDto.getName())				
				.role("User")
				.avatar("https://www.gravatar.com/avatar/0?d=mp")
				.build();		
		return userAccountToUserRegisterResponceDto(userAccountRepository.save(userAccount));
	}

	private UserRegisterResponceDto userAccountToUserRegisterResponceDto(UserAccount userAccount) {		
		return UserRegisterResponceDto.builder()
				.email(userAccount.getEmail())
				.name(userAccount.getName())
				.avatar(userAccount.getAvatar())
				.roles(userAccount.getRoles())
				.build();
	}

	private UserProfileDto userAccountToUserProfileDto(UserAccount userAccount) {		
		return UserProfileDto.builder()
				.email(userAccount.getEmail())				
				.name(userAccount.getName())
				.phone(userAccount.getPhone())
				.avatar(userAccount.getAvatar())
				.roles(userAccount.getRoles())
				.build();
	}

	@Override
	public UserProfileDto userLogin(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).get();
		if (userAccount.isBlock()) {
			throw new ForbiddenException();
		}
		return userAccountToUserProfileDto(userAccount);
	}
	
	@Override
	public UserProfileDto getUserById(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).get();
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDto updateUser(String login, UserProfileDto userProfileDto) {
		UserAccount userAccount = userAccountRepository.findById(login).get();
		
		if (userProfileDto.getName() != null) {
			userAccount.setName(userProfileDto.getName());
		}		
		if (userProfileDto.getPhone() != null) {
			userAccount.setPhone(userProfileDto.getPhone());
		}
		userAccountRepository.save(userAccount);
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public void userLogout(String login) {
		// TODO Auto-generated method stub

	}

	@Transactional
	@Override
	public UserProfileDto deleteUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).get();
		userAccountRepository.deleteById(login);
		return userAccountToUserProfileDto(userAccount);
	}

	@Override
	public List<String> addRole(String userLogin, RolesDto rolesDto) {		
		UserAccount userAccount = userAccountRepository.findById(userLogin).orElseThrow(NotFoundException::new);		
		userAccount.setRoles(rolesDto.getRoles());		
		userAccountRepository.save(userAccount);
		return userAccount.getRoles();
	}

//	@Override
//	public List<String> removeRole(String userLogin, String role) {
//		UserAccount userAccount = userAccountRepository.findById(userLogin).orElseThrow(NotFoundException::new);
//		userAccount.removeRole(role);
//		userAccountRepository.save(userAccount);
//		return userAccount.getRoles();
//	}

	@Override
	public boolean blockUser(String userLogin, boolean block) {
		UserAccount userAccount = userAccountRepository.findById(userLogin).orElseThrow(NotFoundException::new);
		if (block) {
			userAccount.setBlock(true);
		}  else {
			userAccount.setBlock(false);
		}		
		userAccountRepository.save(userAccount);
		return userAccount.isBlock();
	}

}
