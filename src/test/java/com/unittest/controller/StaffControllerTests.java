package com.unittest.controller;

import com.controller.StaffController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.resolver.RequestPaginationResolver;
import com.service.StaffService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.utils.TestsUtils.*;

public class StaffControllerTests {

    MockMvc mockMvc;

    StaffService staffService = Mockito.mock(StaffService.class);

    /**
     * Create a new staff and save staff to a department and save successfully
     *
     * @throws Exception
     */
    @Test
    public void when_saveStaffByPostMethod_thenSuccessfullySave() throws Exception {
        StaffModel expectModel = createStaffModel();
        expectModel.setUpdatedAt(null);
        expectModel.setDepartmentModel(createDepartmentModel());
        when(staffService.createStaff(any())).thenReturn(expectModel);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(post("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.username").value("username"));
    }

    /**
     * When save new staff with error information then return bad request
     * @throws Exception
     */
    @Test
    public void when_saveInvalidStaffByPostMethod_thenReturnBadRequest() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        paramJson.put("depId", "1");
        paramJson.put("firstName", "4");
        mockMvc.perform(post("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When update a existed staff then save successfully
     * @throws Exception
     */
    @Test
    public void when_updateExistedStaffByPostMethod_thenSuccessfullyUpdate() throws Exception {
        when(staffService.updateStaff(any())).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(put("/staffs/{staffId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    /**
     * When update staff with invalid id then return bad request
     * @throws Exception
     */
    @Test
    public void when_updateStaffWithInvalidIDByPostMethod_thenReturnBadRequest() throws Exception {
        when(staffService.updateStaff(any())).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(put("/staffs/{staffId}", "A")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When delete existed staff then delete successfully
     * @throws Exception
     */
    @Test
    public void when_deleteExistedStaff_thenSuccessfullyDelete() throws Exception {
        when(staffService.deleteStaffById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(delete("/staffs/{staffId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
    }

    /**
     * When delete staff with invalid id then return bad request
     * @throws Exception
     */
    @Test
    public void when_deleteStaffWithInvalidID_thenReturnBadRequest() throws Exception {
        when(staffService.deleteStaffById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(delete("/staffs/{staffId}", "a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff by id then return information of existed staff
     * @throws Exception
     */
    @Test
    public void when_findStaffById_theReturnStaff() throws Exception {
        when(staffService.findById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService)).build();
        mockMvc.perform(get("/staffs/{staffId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
    }


    /**
     * When find staff like first name and last name then return return resource of staff contains list of staff
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastName_thenReturnResource() throws Exception {
        List<StaffModel> staffModelList = new ArrayList<>();
        staffModelList.add(createStaffModel());
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 1, 2, staffModelList);

        when(staffService.findByLastnameOrFirstname(any(), anyString())).thenReturn(expectedResource);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new StaffController(staffService))
                .setCustomArgumentResolvers(new RequestPaginationResolver()).build();
        mockMvc.perform(get("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.perPage").value(1))
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.totalPage").value(2))
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    ResourceModel<LinkedHashMap<String, Object>> actualResource = new ObjectMapper().readValue(actualJson, ResourceModel.class);
                    compareTwoResourceStaff(expectedResource, convertToStaffResource(actualResource));
                });
    }


}
