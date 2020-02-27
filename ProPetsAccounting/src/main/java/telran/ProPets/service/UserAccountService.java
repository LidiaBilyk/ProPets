package telran.ProPets.service;

import java.util.List;

import telran.ProPets.dto.RolesDto;
import telran.ProPets.dto.UserProfileDto;
import telran.ProPets.dto.UserRegisterDto;
import telran.ProPets.dto.UserRegisterResponceDto;

public interface UserAccountService {
	UserRegisterResponceDto registerUser(UserRegisterDto userRegisterDto);
	UserProfileDto userLogin(String login);
	UserProfileDto getUserById(String login);
	UserProfileDto updateUser(String login, UserProfileDto userProfileDto);
	void userLogout(String login);
	UserProfileDto deleteUser(String login);
	List<String> addRole(String userLogin, RolesDto rolesDto);
//	List<String> removeRole(String userLogin, String role);
	boolean blockUser(String login, boolean block);

}
