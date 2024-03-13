//package com.cognicx.AppointmentRemainder.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.cognicx.AppointmentRemainder.Dto.UserDto;
//import com.cognicx.AppointmentRemainder.Dto.UserInventoryMapDto;
//import com.cognicx.AppointmentRemainder.Request.UserCenterRequest;
//import com.cognicx.AppointmentRemainder.Request.UserClientRequest;
//import com.cognicx.AppointmentRemainder.Request.UserProcessRequest;
//import com.cognicx.AppointmentRemainder.Request.UserRegionRequest;
//import com.cognicx.AppointmentRemainder.dao.UserDAO;
//import com.cognicx.AppointmentRemainder.response.UsersResponse;
//import com.cognicx.AppointmentRemainder.service.UserService;
//
//
//@Service
//public class UserServiceImpl  implements UserService{
//
//	@Autowired
//	UserDAO userDAO;
//
//
//	// Utility function
//	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
//		Map<Object, Boolean> map = new ConcurrentHashMap<>();
//		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//	}
//
//
//	@Override
//	public List<UsersResponse> getApprovedUsersList() throws Exception {
//		List<UserDto> userList = userDAO.getApprovedUsersList("");
//		List<UsersResponse> userResList = new ArrayList<>();
//		for (UserDto userDto : userList) {
//			List<UserInventoryMapDto> userInventoryMapList = userDto.getUserInventoryMapDtoList();
//
//			if (userInventoryMapList != null && !userInventoryMapList.isEmpty()) {
//				List<UserInventoryMapDto> regionList = userInventoryMapList.stream()
//						.filter(distinctByKey(region -> region.getInventoryRegionId())).collect(Collectors.toList());
//
//				List<UserRegionRequest> inventoryMapResponseList = new ArrayList<>();
//				regionList.stream().forEach(regionObject -> {
//					UserRegionRequest inventoryMapResponse = new UserRegionRequest();
//
//					List<UserInventoryMapDto> centerList = userInventoryMapList.stream()
//							.filter(center -> center.getInventoryRegionId().equals(regionObject.getInventoryRegionId()))
//							.filter(distinctByKey(center -> center.getInventoryCenterId()))
//							.collect(Collectors.toList());
//					List<UserCenterRequest> userCenters = new ArrayList<>();
//					centerList.stream().forEach(centerObject -> {
//						UserCenterRequest centerRequest = new UserCenterRequest();
//						List<UserInventoryMapDto> clientList = userInventoryMapList.stream().filter(
//								client -> client.getInventoryRegionId().equals(regionObject.getInventoryRegionId())
//										&& client.getInventoryCenterId().equals(centerObject.getInventoryCenterId()))
//								.filter(distinctByKey(client -> client.getInventoryClientId()))
//								.collect(Collectors.toList());
//						List<UserClientRequest> userClients = new ArrayList<>();
//						clientList.forEach(clientObject -> {
//
//							UserClientRequest clientRequest = new UserClientRequest();
//							List<UserInventoryMapDto> processList = userInventoryMapList.stream()
//									.filter(process -> process.getInventoryRegionId()
//											.equals(regionObject.getInventoryRegionId())
//											&& process.getInventoryCenterId()
//													.equals(centerObject.getInventoryCenterId())
//											&& process.getInventoryClientId()
//													.equals(clientObject.getInventoryClientId()))
//									.filter(distinctByKey(process -> process.getInventoryProcessId()))
//									.collect(Collectors.toList());
//							List<UserProcessRequest> userProcesses = new ArrayList<>();
//							processList.forEach(processObject -> {
//								UserProcessRequest processRequest = new UserProcessRequest();
//								processRequest.setInventoryProcessId(processObject.getInventoryProcessId());
//								processRequest.setInventoryProcessName(processObject.getInventoryProcessName());
//								processRequest.setInventoryCategoryId(processObject.getInventoryCategoryId());
//								processRequest.setInventoryCategoryName(processObject.getInventoryCategoryName());
//								userProcesses.add(processRequest);
//							});
//							clientRequest.setInventoryClientId(clientObject.getInventoryClientId());
//							clientRequest.setInventoryClientName(clientObject.getInventoryClientName());
//							clientRequest.setUserProcesses(userProcesses);
//							userClients.add(clientRequest);
//						});
//						centerRequest.setUserClients(userClients);
//						centerRequest.setInventoryCenterId(centerObject.getInventoryCenterId());
//						centerRequest.setInventoryCenterName(centerObject.getInventoryCenterName());
//						userCenters.add(centerRequest);
//					});
//					inventoryMapResponse.setUserCenters(userCenters);
//					inventoryMapResponse.setInventoryRegionId(regionObject.getInventoryRegionId());
//					inventoryMapResponse.setInventoryRegionName(regionObject.getInventoryRegionName());
//					inventoryMapResponseList.add(inventoryMapResponse);
//				});
//				userDto.setUserInventoryMaps(inventoryMapResponseList);
//			}
//			userDto.setSurveyTypes(userDto.getSurveyTypes());
//			userResList.add(new UsersResponse(userDto));
//		}
//		return userResList;
//	}
//}
