package com.appdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appdeveloperblog.app.service.AddressService;
import com.appdeveloperblog.app.service.UserService;
import com.appdeveloperblog.app.shared.dto.AddressDto;
import com.appdeveloperblog.app.shared.dto.UserDto;
import com.appdeveloperblog.app.ui.model.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ui.model.response.AddressRest;
import com.appdeveloperblog.app.ui.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ui.model.response.RequestOperationName;
import com.appdeveloperblog.app.ui.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ui.model.response.UserRest;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

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

//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);

		ModelMapper modelMapper = new ModelMapper();

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createUser = userService.createUser(userDto);
		// BeanUtils.copyProperties(createUser, returnValue);
		returnValue = modelMapper.map(createUser, UserRest.class);
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
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);
		

		for (UserDto userDto : users) {
			ModelMapper modelMapper = new ModelMapper();
			UserRest userModel = modelMapper.map(userDto, UserRest.class);
			returnValue.add(userModel);
		}

		return returnValue;
	}

	@GetMapping("/{id}/addresses")
	public CollectionModel<AddressRest> getUserAddress(@PathVariable("id") String id) {
		List<AddressRest> returnValue = new ArrayList<>();

		List<AddressDto> addressesDto = addressService.getAddresses(id);

		if (addressesDto != null && !addressesDto.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);

			for (AddressRest addressRest : returnValue) {
				Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
						.getUserAddressById(id, addressRest.getAddressId())).withSelfRel();
				addressRest.add(selfLink);
			}

		}

		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(id))
				.withRel("user");
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id))
				.withSelfRel();

		return CollectionModel.of(returnValue, selfLink, userLink);
	}

	@GetMapping("/{userId}/addresses/{addressId}")
	public EntityModel<AddressRest> getUserAddressById(@PathVariable("userId") String userId,
			@PathVariable("addressId") String addressId) {
		AddressDto addressDto = addressService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		AddressRest returnValue = modelMapper.map(addressDto, AddressRest.class);

		Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUser(userId))
				.withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId)).withRel("addresses");
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddressById(userId, addressId))
				.withSelfRel();

		return EntityModel.of(returnValue, userLink, userAddressesLink, selfLink);
	}
}
