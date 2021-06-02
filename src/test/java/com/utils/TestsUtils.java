package com.utils;

import com.model.StaffModel;
import com.model.StaffResource;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TestsUtils {
    public static JSONObject createJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "firstName");
        jsonObject.put("lastName", "lastName");
        jsonObject.put("id", 1);
        return jsonObject;
    }

    public static LocalDateTime createMockTime() {
        return LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
    }

    public static StaffModel createStaffModel() {
        StaffModel staffModel = new StaffModel();
        staffModel.setFirstName("firstName");
        staffModel.setLastName("lastName");
        staffModel.setId(1);
        staffModel.setCreateAt(createMockTime());
        staffModel.setUpdatedAt(createMockTime());
        return staffModel;
    }

    public static StaffResource createStaffResource(int total, int page, int perPage, int totalPage, List<StaffModel> modelList) {
        StaffResource staffResource = new StaffResource();
        staffResource.setTotal(total);
        staffResource.setPage(page);
        staffResource.setPerPage(perPage);
        staffResource.setTotalPage(totalPage);
        staffResource.setData(modelList);
        return staffResource;
    }

    public static boolean compareTwoStaff(StaffModel expected, StaffModel actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getFirstName(), expected.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getCreateAt(), actual.getCreateAt());
        Assertions.assertEquals(expected.getUpdatedAt(),actual.getUpdatedAt());
        return true;
    }

    public static boolean compareTwoResource(StaffResource expected, StaffResource actual) {
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
