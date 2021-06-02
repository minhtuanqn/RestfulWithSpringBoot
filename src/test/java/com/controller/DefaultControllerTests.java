package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.StaffModel;
import com.model.StaffResource;
import com.service.DefaultService;
import com.utils.ValidatorUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.utils.TestsUtils.*;

public class DefaultControllerTests {

    MockMvc mockMvc;

    DefaultService defaultService = Mockito.mock(DefaultService.class);

    @Test
    public void when_saveStaffByPostMethod_thenSuccessfullySave() throws Exception {
        StaffModel expectModel = createStaffModel();
        expectModel.setUpdatedAt(null);
        when(defaultService.createStaff(any())).thenReturn(expectModel);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(post("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andDo(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                });
    }

    @Test
    public void when_saveInvalidStaffByPostMethod_thenReturnBadRequest() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        paramJson.put("firstName", "4");
        mockMvc.perform(post("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void when_updateExistedStaffByPostMethod_thenSuccessfullyUpdate() throws Exception {
        when(defaultService.updateStaff(any())).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(put("/staffs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()));
    }

    @Test
    public void when_updateNotExistStaffByPostMethod_thenReturnNotFound() throws Exception {
        when(defaultService.updateStaff(any())).thenReturn(null);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(put("/staffs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_updateStaffWithInvalidIDByPostMethod_thenReturnBadRequest() throws Exception {
        when(defaultService.updateStaff(any())).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(put("/staffs/{id}", -3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_deleteExistedStaff_thenSuccessfullyDelete() throws Exception {
        when(defaultService.deleteStaffById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
    }

    @Test
    public void when_deleteNotExistStaffByDeleteMethod_thenReturnNotFound() throws Exception {
        when(defaultService.deleteStaffById(any())).thenReturn(null);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_deleteStaffWithInvalidID_thenReturnBadRequest() throws Exception {
        when(defaultService.deleteStaffById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", "a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffById_theReturnStaff() throws Exception {
        when(defaultService.findById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
    }

    @Test
    public void when_findStaffByInvalidId_theReturnBadRequest() throws Exception {
        when(defaultService.findById(1)).thenReturn(createStaffModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs/{id}", -6)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffWithNotExistID_thenReturnNotfound() throws Exception {
        when(defaultService.findById(1)).thenReturn(null);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void when_findStaffByFirstNameOrLastName_thenReturnResource() throws Exception {
        List<StaffModel> staffModelList = new ArrayList<>();
        staffModelList.add(createStaffModel());
        StaffResource expectedResource = createStaffResource(2, 0, 1,2, staffModelList);
        when(defaultService.findByLastnameOrFirstname(anyInt(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(expectedResource);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
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
                    StaffResource actualResource = new ObjectMapper().readValue(actualJson, StaffResource.class);
                    compareTwoResource(expectedResource, actualResource);
                });
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidNotNegativeParam_thenReturnBadRequest()
            throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "-1")
                .param("perPage", "0")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvaliGreaterThanZeroParam_thenReturnBadRequest()
            throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "0")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidSortBy_thenReturnBadREquest()
            throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DefaultController(defaultService)).build();
        mockMvc.perform(get("/staffs")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "3")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "d")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isNotFound());
    }
}
