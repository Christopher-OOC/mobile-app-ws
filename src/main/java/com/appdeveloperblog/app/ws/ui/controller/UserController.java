package com.appdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
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

import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.shared.dto.UserDto;
import com.appdeveloperblog.app.ui.model.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ui.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ui.model.response.RequestOperationName;
import com.appdeveloperblog.app.ui.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ui.model.response.UserRest;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public UserRest getUser(@PathVariable("id") String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);

		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);

		return returnValue;
	}

	@PutMapping("/{id}")
	public UserRest updateUser(@PathVariable("id") String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(createUser, returnValue);

		return returnValue;
	}

	@DeleteMapping("/{id}")
	public OperationStatusModel deleteUser(@PathVariable("id") String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		userService.deleteUser(id);
		
		return returnValue;
	}
	
	@GetMapping
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="limit", defaultValue="25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
}
