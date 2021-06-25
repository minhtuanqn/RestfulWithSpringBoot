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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestsUtils {
    public static JSONObject createJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "firstName");
        jsonObject.put("lastName", "lastName");
        jsonObject.put("username", "username");
        jsonObject.put("password", "password");
        jsonObject.put("id", 1);
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
        compareTwoDepartment(expected.getDepartmentModel(), actual.getDepartmentModel());
        return true;
    }

    public static boolean compareTwoResource(ResourceModel<StaffModel> expected, ResourceModel<StaffModel> actual) {
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


}
