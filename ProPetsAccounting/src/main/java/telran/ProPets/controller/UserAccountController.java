package telran.ProPets.controller;

import java.security.Principal;
import java.util.List;

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

import telran.ProPets.dto.RolesDto;
import telran.ProPets.dto.UserProfileDto;
import telran.ProPets.dto.UserRegisterDto;
import telran.ProPets.dto.UserRegisterResponceDto;
import telran.ProPets.service.UserAccountService;

@RestController
@RequestMapping("{lang}/account/v1")
public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping
	public UserRegisterResponceDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.registerUser(userRegisterDto);
	}

	@PostMapping("/login")
	public UserProfileDto userLogin(Principal principal) {
		return userAccountService.userLogin(principal.getName());
	}
	
	@GetMapping("/{login:.*}/info")
	public UserProfileDto getUserById(@PathVariable String login) {		
		return userAccountService.getUserById(login);
	}
	
	@PutMapping
	public UserProfileDto updateUser(Principal principal, @RequestBody UserProfileDto userProfileDto) {
		return userAccountService.updateUser(principal.getName(), userProfileDto);
	}
	
	@DeleteMapping
	public UserProfileDto deleteUser(Principal principal) {
		return userAccountService.deleteUser(principal.getName());
	}
	
	@PutMapping("/{login:.*}/roles")
	public List<String> addRole(@PathVariable String login, @RequestBody RolesDto rolesDto) {		
		return userAccountService.addRole(login, rolesDto);
	}
	
//	@DeleteMapping("/role")
//	public List<String> removeRole(@RequestParam String userLogin, @RequestParam String role) {
//		return userAccountService.removeRole(userLogin, role);
//	}
	
	@PutMapping("{login:.*}/block/{block}")
	public boolean blockUser(@PathVariable String login, @PathVariable boolean block) {
		return userAccountService.blockUser(login, block);
	}
}
