package com.utils;

import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.model.DepartmentModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class TestsUtils {
    public static JSONObject createStaffJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "firstName");
        jsonObject.put("lastName", "lastName");
        jsonObject.put("username", "username");
        jsonObject.put("password", "password");
        jsonObject.put("id", 1);
        jsonObject.put("depId", 1);
        return jsonObject;
    }

    public static JSONObject createDepJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "name");
        return jsonObject;
    }

    public static Set<StaffEntity> createStaffEntityList() {
        Set<StaffEntity> staffEntityList = new HashSet<>();
        staffEntityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
        staffEntityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
        return staffEntityList;
    }

    public static LocalDateTime createMockTime() {
        return LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
    }

    public static DepartmentModel createDepartmentModel() {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(1);
        departmentModel.setName("name");
        departmentModel.setCreateAt(createMockTime());
        departmentModel.setUpdateAt(createMockTime());
        return departmentModel;
    }

    public static StaffModel createStaffModel() {
        StaffModel staffModel = new StaffModel();
        staffModel.setFirstName("firstName");
        staffModel.setLastName("lastName");
        staffModel.setUsername("username");
        staffModel.setPassword("password");
        staffModel.setId(1);
        staffModel.setDepId(1);
        staffModel.setCreateAt(createMockTime());
        staffModel.setUpdatedAt(createMockTime());
        staffModel.setDepartmentModel(createDepartmentModel());
        return staffModel;
    }

    public static ResourceModel<StaffModel> createStaffResource(int total, int page, int perPage, int totalPage, List<StaffModel> modelList) {
        ResourceModel<StaffModel> staffResource = new ResourceModel<>();
        staffResource.setTotal(total);
        staffResource.setPage(page);
        staffResource.setPerPage(perPage);
        staffResource.setTotalPage(totalPage);
        staffResource.setData(modelList);
        return staffResource;
    }

    public static ResourceModel<DepartmentModel> createDepartmentResource(int total, int page, int perPage, int totalPage, List<DepartmentModel> modelList) {
        ResourceModel<DepartmentModel> resourceModel = new ResourceModel<>();
        resourceModel.setTotal(total);
        resourceModel.setPage(page);
        resourceModel.setPerPage(perPage);
        resourceModel.setTotalPage(totalPage);
        resourceModel.setData(modelList);
        return resourceModel;
    }

    public static boolean compareTwoDepartment(DepartmentModel expect, DepartmentModel actual) {
        if(expect == null) {
            Assertions.assertEquals(expect, actual);
        }
        else {
            Assertions.assertEquals(expect.getId(), actual.getId());
            Assertions.assertEquals(expect.getName(), actual.getName());
        }
        return true;
    }

    public static boolean compareTwoStaff(StaffModel expected, StaffModel actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getFirstName(), expected.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
//        compareTwoDepartment(expected.getDepartmentModel(), actual.getDepartmentModel());
        return true;
    }

    public static StaffModel convertToStaff(LinkedHashMap<String, Object> map) {
        Integer id = (Integer) map.get("id");
        String firstName = (String) map.get("firstName");
        String lastName = (String) map.get("lastName");
        String userName = (String) map.get("username");
        StaffModel staffModel = new StaffModel();
        staffModel.setId(id);
        staffModel.setUsername(userName);
        staffModel.setFirstName(firstName);
        staffModel.setLastName(lastName);
        return staffModel;
    }

    public static DepartmentModel convertToDepartment(LinkedHashMap<String, Object> map) {
        Integer id = (Integer) map.get("id");
        String name = (String) map.get("name");
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(id);
        departmentModel.setName(name);
        return departmentModel;
    }

    public static boolean compareTwoResourceStaff(ResourceModel<StaffModel> expected,
                                             ResourceModel<StaffModel> actual) {
        Assertions.assertEquals(expected.getPage(), actual.getPage());
        Assertions.assertEquals(expected.getPerPage(), actual.getPerPage());
        Assertions.assertEquals(expected.getTotal(), actual.getTotal());
        Assertions.assertEquals(expected.getTotalPage(), actual.getTotalPage());
        List<StaffModel> expectedList = expected.getData();
        List<StaffModel> actualList = actual.getData();
        int expectedListSize = expectedList.size();
        for(int count = 0; count < expectedListSize; count++) {
            compareTwoStaff(expectedList.get(count), actualList.get(count));
        }
        return true;
    }

    public static boolean compareTwoResourceDepartment(ResourceModel<DepartmentModel> expected,
                                             ResourceModel<DepartmentModel> actual) {
        Assertions.assertEquals(expected.getPage(), actual.getPage());
        Assertions.assertEquals(expected.getPerPage(), actual.getPerPage());
        Assertions.assertEquals(expected.getTotal(), actual.getTotal());
        Assertions.assertEquals(expected.getTotalPage(), actual.getTotalPage());
        List<DepartmentModel> expectedList = expected.getData();
        List<DepartmentModel> actualList = actual.getData();
        int expectedListSize = expectedList.size();
        for(int count = 0; count < expectedListSize; count++) {
            compareTwoDepartment(expectedList.get(count), actualList.get(count));
        }
        return true;
    }

    public static ResourceModel<StaffModel> convertToStaffResource(ResourceModel<LinkedHashMap<String, Object>> resourceModel) {
        ResourceModel<StaffModel> staffModelResourceModel = new ResourceModel<>();
        List<StaffModel> staffModelList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> actualMap = resourceModel.getData();
        for(int count = 0; count < actualMap.size(); count++) {
            staffModelList.add(convertToStaff(actualMap.get(count)));
        }
        staffModelResourceModel.setData(staffModelList);
        staffModelResourceModel.setPage(resourceModel.getPage());
        staffModelResourceModel.setPerPage(resourceModel.getPerPage());
        staffModelResourceModel.setTotal(resourceModel.getTotal());
        staffModelResourceModel.setTotalPage(resourceModel.getTotalPage());
        return staffModelResourceModel;
    }

    public static ResourceModel<DepartmentModel> convertToDepartmentResource(ResourceModel<LinkedHashMap<String, Object>> resourceModel) {
        ResourceModel<DepartmentModel> depModelResourceModel = new ResourceModel<>();
        List<DepartmentModel> departmentModelList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> actualMap = resourceModel.getData();
        for(int count = 0; count < actualMap.size(); count++) {
            departmentModelList.add(convertToDepartment(actualMap.get(count)));
        }
        depModelResourceModel.setData(departmentModelList);
        depModelResourceModel.setPage(resourceModel.getPage());
        depModelResourceModel.setPerPage(resourceModel.getPerPage());
        depModelResourceModel.setTotal(resourceModel.getTotal());
        depModelResourceModel.setTotalPage(resourceModel.getTotalPage());
        return depModelResourceModel;
    }



}
