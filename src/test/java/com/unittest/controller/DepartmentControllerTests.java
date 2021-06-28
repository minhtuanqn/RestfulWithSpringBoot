package com.unittest.controller;

import com.controller.DepartmentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.DepartmentModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.resolver.RequestPaginationResolver;
import com.service.DepartmentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.utils.TestsUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DepartmentControllerTests {

    MockMvc mockMvc;

    DepartmentService departmentService = Mockito.mock(DepartmentService.class);

    /**
     * Create new department and save successfully
     *
     * @throws Exception
     */
    @Test
    public void when_saveDepartmentByPostMethod_thenSuccessfullySave() throws Exception {
        DepartmentModel expectModel = createDepartmentModel();
        expectModel.setUpdateAt(null);
        when(departmentService.createDepartment(any())).thenReturn(createDepartmentModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
        JSONObject paramJson = createDepJsonObject();
        mockMvc.perform(post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    /**
     * Update existed department and save successfully
     *
     * @throws Exception
     */
    @Test
    public void when_updateDepartmentByPutMethod_thenSuccessfullySave() throws Exception {
        DepartmentModel expectModel = createDepartmentModel();
        expectModel.setUpdateAt(null);
        when(departmentService.updateDepartment(anyInt(), any())).thenReturn(createDepartmentModel());
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
        JSONObject paramJson = createDepJsonObject();
        mockMvc.perform(put("/departments/{departmentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    /**
     * Delete a existed department and delete successfully
     *
     * @throws Exception
     */
    @Test
    public void when_deleteDepartmentByDeleteMethod_thenSuccessfullyDelete() throws Exception {
        DepartmentModel expectModel = createDepartmentModel();
        expectModel.setUpdateAt(null);
        when(departmentService.deleteDepartment(anyInt())).thenReturn(createDepartmentModel());

        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
        JSONObject paramJson = createDepJsonObject();
        mockMvc.perform(delete("/departments/{departmentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    /**
     * Find existed department and return department
     *
     * @throws Exception
     */
    @Test
    public void when_findDepartmentByIdByGetMethod_thenReturnDepartment() throws Exception {
        DepartmentModel expectModel = createDepartmentModel();
        expectModel.setUpdateAt(null);
        when(departmentService.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());

        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
        JSONObject paramJson = createDepJsonObject();
        mockMvc.perform(get("/departments/{departmentId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name"));
    }

    /**
     * Find staffs by department id and return resource of staff contains list of staff
     *
     * @throws Exception
     */
    @Test
    public void when_findStaffByDepartmentId_thenReturnResource() throws Exception {
        List<StaffModel> staffModelList = new ArrayList<>();
        staffModelList.add(createStaffModel());
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 1, 2, staffModelList);

        when(departmentService.findAllStaffByDepartmentId(anyInt(), any()))
                .thenReturn(expectedResource);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService))
                .setCustomArgumentResolvers(new RequestPaginationResolver()).build();
        mockMvc.perform(get("/departments/{departmentId}/staffs", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("sortBy", "name")
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

    /**
     * Find departments like name and return resource of department contains list of departments
     *
     * @throws Exception
     */
    @Test
    public void when_findDepartmentLikeName_thenReturnResource() throws Exception {
        List<DepartmentModel> departmentModelList = new ArrayList<>();
        departmentModelList.add(createDepartmentModel());
        ResourceModel<DepartmentModel> expectedResource = createDepartmentResource(2, 0, 1, 2, departmentModelList);

        when(departmentService.findDepartmentLikeName(anyString(), any()))
                .thenReturn(expectedResource);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService))
                .setCustomArgumentResolvers(new RequestPaginationResolver()).build();
        mockMvc.perform(get("/departments", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("sortBy", "name")
                .param("searchedName", "a")
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
                    compareTwoResourceDepartment(expectedResource, convertToDepartmentResource(actualResource));
                });
    }

}
