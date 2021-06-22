//package com.unittest.controller;
//
//import com.controller.DepartmentController;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.model.DepartmentModel;
//import com.model.StaffModel;
//import com.model.StaffResourceModel;
//import com.resolver.PaginationResolver;
//import com.service.DepartmentService;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.utils.TestsUtils.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class DepartmentCotrollerTests {
//
//    MockMvc mockMvc;
//
//    DepartmentService departmentService = Mockito.mock(DepartmentService.class);
//
//    /**
//     * Create new department and save successfully
//     *
//     * @throws Exception
//     */
//    @Test
//    public void when_saveDepartmentByPostMethod_thenSuccessfullySave() throws Exception {
//        DepartmentModel expectModel = createDepartmentModel();
//        expectModel.setUpdateAt(null);
//        when(departmentService.createDepartment(any())).thenReturn(createDepartmentModel());
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
//        JSONObject paramJson = createDepJsonObject();
//        mockMvc.perform(post("/departments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(paramJson.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("name"));
//    }
//
//    /**
//     * Update existed department and save successfully
//     *
//     * @throws Exception
//     */
//    @Test
//    public void when_updateDepartmentByPutMethod_thenSuccessfullySave() throws Exception {
//        DepartmentModel expectModel = createDepartmentModel();
//        expectModel.setUpdateAt(null);
//        when(departmentService.updateDepartment(anyInt(), any())).thenReturn(createDepartmentModel());
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
//        JSONObject paramJson = createDepJsonObject();
//        mockMvc.perform(put("/departments/{departmentId}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(paramJson.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("name"));
//    }
//
//    /**
//     * Delete a existed department and delete successfully
//     *
//     * @throws Exception
//     */
//    @Test
//    public void when_deleteDepartmentByDeleteMethod_thenSuccessfullyDelete() throws Exception {
//        DepartmentModel expectModel = createDepartmentModel();
//        expectModel.setUpdateAt(null);
//        when(departmentService.deleteDepartment(anyInt())).thenReturn(createDepartmentModel());
//
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
//        JSONObject paramJson = createDepJsonObject();
//        mockMvc.perform(delete("/departments/{departmentId}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(paramJson.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("name"));
//    }
//
//    /**
//     * Find existed department and return department
//     *
//     * @throws Exception
//     */
//    @Test
//    public void when_findDepartmentByIdByGetMethod_thenReturnDepartment() throws Exception {
//        DepartmentModel expectModel = createDepartmentModel();
//        expectModel.setUpdateAt(null);
//        when(departmentService.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService)).build();
//        JSONObject paramJson = createDepJsonObject();
//        mockMvc.perform(get("/departments/{departmentId}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(paramJson.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("name"));
//    }
//
//    /**
//     * Find department by department id and return resource of staff contains list of staff
//     *
//     * @throws Exception
//     */
//    @Test
//    public void when_findStaffDepartmentId_thenReturnResource() throws Exception {
//        List<StaffModel> staffModelList = new ArrayList<>();
//        staffModelList.add(createStaffModel());
//        StaffResourceModel expectedResource = createStaffResource(2, 0, 1, 2, staffModelList);
//
//        when(departmentService.findAllStaffByDepartmentId(anyInt(), anyInt(), anyInt(), anyString(), anyString()))
//                .thenReturn(expectedResource);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(departmentService))
//                .setCustomArgumentResolvers(new PaginationResolver()).build();
//        mockMvc.perform(get("/departments/{departmentId}/staffs", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("page", "0")
//                .param("perPage", "1")
//                .param("sortBy", "firstName")
//                .param("sortType", "dsc")
//                .content(new JSONObject().toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.page").value(0))
//                .andExpect(jsonPath("$.perPage").value(1))
//                .andExpect(jsonPath("$.total").value(2))
//                .andExpect(jsonPath("$.totalPage").value(2))
//                .andDo(result -> {
//                    String actualJson = result.getResponse().getContentAsString();
//                    StaffResourceModel actualResource = new ObjectMapper().readValue(actualJson, StaffResourceModel.class);
//                    compareTwoResource(expectedResource, actualResource);
//                });
//    }
//
//}
