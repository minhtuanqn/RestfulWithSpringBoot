package com.intergrationtest;

import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intergrationtest.config.TestConfig;
import com.model.DepartmentModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.utils.TestsUtils.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class SpringBootControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DataSource dataSource;

    /**
     * When save staff by then save successfully
     * @throws Exception
     */
    @Test
    public void when_saveStaffByPostMethod_thenSuccessfullySave() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        paramJson.put("id", 3);
        paramJson.put("username","savedUsername");
        mockMvc.perform(post("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("savedUsername"))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"));
    }

    /**
     * When save staff with invalid information by then return bad request
     * @throws Exception
     */
    @Test
    public void when_saveInvalidStaffByPostMethod_thenReturnBadRequest() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        paramJson.put("firstName", "4");
        mockMvc.perform(post("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());

    }

    /**
     * When updated staff then update successfully
     * @throws Exception
     */
    @Test
    public void when_updateExistedStaffByPostMethod_thenSuccessfullyUpdate() throws Exception {

        DepartmentEntity savedDepartment = departmentRepository.save(new DepartmentEntity(createDepartmentModel()));
        StaffModel paramModel = createStaffModel();
        paramModel.setId(4);
        StaffEntity savedStaffEntity = staffRepository.save(new StaffEntity(paramModel, savedDepartment));

        JSONObject paramJson = createStaffJsonObj();
        paramJson.put("id", 4);
        paramJson.put("firstName", "updatedFirstname");
        paramJson.put("lastName", "updatedLastname");
        paramJson.put("username", "updatedUsername");

        mockMvc.perform(put("/staffs/{id}", savedStaffEntity.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.username").value("updatedUsername"))
                .andExpect(jsonPath("$.firstName").value("updatedFirstname"))
                .andExpect(jsonPath("$.lastName").value("updatedLastname"));
    }

    /**
     * when update not existed staff then return bad request
     * @throws Exception
     */
    @Test
    public void when_updateNotExistStaffByPostMethod_thenReturnBadRequest() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(put("/staffs/{id}", 1000)
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When udpate staff with invalid id then return bad request
     * @throws Exception
     */
    @Test
    public void when_updateStaffWithInvalidIDByPostMethod_thenReturnBadRequest() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(put("/staffs/{id}", -3)
                .with(httpBasic("root", "123"))
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

        DepartmentModel departmentModel3 = createDepartmentModel();
        departmentModel3.setId(3);
        departmentModel3.setName("departmentModel3");
        DepartmentEntity savedDepartmentEntity = departmentRepository.save(new DepartmentEntity(departmentModel3));
        StaffModel paramModel = createStaffModel();
        paramModel.setId(5);
        StaffEntity staffEntity = staffRepository.save(new StaffEntity(paramModel, savedDepartmentEntity));

        mockMvc.perform(delete("/staffs/{id}", staffEntity.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(staffEntity.getId()))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"));
        ;
    }

    /**
     * When delete not existed staff then return bad request
     * @throws Exception
     */
    @Test
    public void when_deleteNotExistStaffByDeleteMethod_thenReturnBadRequest() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(delete("/staffs/{id}", 100)
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When delete staff with invalid id then return bad request
     * @throws Exception
     */
    @Test
    public void when_deleteStaffWithInvalidID_thenReturnBadRequest() throws Exception {

        JSONObject paramJson = createStaffJsonObj();
        mockMvc.perform(delete("/staffs/{id}", "a")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff with id then return staff
     * @throws Exception
     */
    @Test
    public void when_findStaffById_theReturnStaff() throws Exception {

        StaffModel savedStaff = createStaffModel();
        savedStaff.setUsername("testFindById");
        savedStaff.setId(6);

        DepartmentModel depModel = createDepartmentModel();
        depModel.setName("testFindById");
        depModel.setId(4);

        DepartmentEntity savedDepartment = departmentRepository.save(new DepartmentEntity(depModel));
        StaffEntity staffEntity = staffRepository.save(new StaffEntity(savedStaff, savedDepartment));
        mockMvc.perform(get("/staffs/{id}", staffEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("root", "123"))
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(staffEntity.getId()))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"));
        ;
    }

    /**
     * When find staff with invalid id then return bad request
     * @throws Exception
     */
    @Test
    public void when_findStaffByInvalidId_theReturnBadRequest() throws Exception {

        mockMvc.perform(get("/staffs/{id}", -6)
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff with not exist staff then return bad request
     * @throws Exception
     */
    @Test
    public void when_findStaffWithNotExistID_thenReturnBadRequest() throws Exception {

        mockMvc.perform(get("/staffs/{id}", "100")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    /**
     * When find staff like firstname or lastname then return resource of staff contains list of staff
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithDscFirstname_thenReturnResource() throws Exception {

        DepartmentModel paramDep = createDepartmentModel();
        paramDep.setId(20);
        paramDep.setName("jjjjjj");
        DepartmentEntity savedDepartment = departmentRepository.save(new DepartmentEntity(createDepartmentModel()));

        StaffEntity staffEntity1 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity1.setId(12);
        staffEntity1.setUsername("vvvvv");
        staffEntity1.setFirstName("vvvvvv");
        staffEntity1.setLastName("vvvvvv");
        StaffEntity actualStaff1 = staffRepository.save(staffEntity1);

        StaffEntity staffEntity2 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity2.setId(13);
        staffEntity2.setUsername("yyyyy");
        staffEntity2.setFirstName("yyyyyv");
        staffEntity2.setLastName("yyyyyv");
        StaffEntity actualStaff2 = staffRepository.save(staffEntity2);


        List<StaffModel> staffModelList = new ArrayList<>();
        staffModelList.add(new StaffModel(actualStaff2));
        staffModelList.add(new StaffModel(actualStaff1));
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 2,1, staffModelList);
        mockMvc.perform(get("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "2")
                .param("searchedValue", "v")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    ResourceModel<LinkedHashMap<String, Object>> actualResource = new ObjectMapper().readValue(actualJson, ResourceModel.class);
                    compareTwoResourceStaff(expectedResource, convertToStaffResource(actualResource));
                });
    }

    /**
     * When find staff with firstname or last name by asc then return resource of staff
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithAscFirstname_thenReturnResource() throws Exception {

        DepartmentEntity savedDepartment = departmentRepository.save(new DepartmentEntity(createDepartmentModel()));

        StaffEntity staffEntity1 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity1.setUsername("kkkk");
        staffEntity1.setFirstName("kkkkw");
        staffEntity1.setLastName("kkkkw");
        staffEntity1.setId(14);
        StaffEntity actualStaff1 = staffRepository.save(staffEntity1);

        StaffEntity staffEntity2 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity2.setUsername("wwwww");
        staffEntity2.setUsername("wwwww");
        staffEntity2.setFirstName("wwwww");
        staffEntity2.setId(15);
        StaffEntity actualStaff2 = staffRepository.save(staffEntity2);

        List<StaffModel> staffModelList = new ArrayList<>();
        staffModelList.add(new StaffModel(actualStaff1));
        staffModelList.add(new StaffModel(actualStaff2));
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 2,1, staffModelList);
        mockMvc.perform(get("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "2")
                .param("searchedValue", "w")
                .param("sortBy", "firstName")
                .param("sortType", "asc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    ResourceModel<LinkedHashMap<String, Object>> actualResource = new ObjectMapper().readValue(actualJson, ResourceModel.class);
                    compareTwoResourceStaff(expectedResource, convertToStaffResource(actualResource));
                });
    }

    /**
     * When find staff by first name or last name with invalid param then return bad request
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidNotNegativeParam_thenReturnBadRequest()
            throws Exception {

        mockMvc.perform(get("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "-1")
                .param("perPage", "0")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff by first name or last name with invalid param then return bad request
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidGreaterThanZeroParam_thenReturnBadRequest()
            throws Exception {

        mockMvc.perform(get("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "0")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff with first name or last name with invalid sort by then return bad request
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidSortBy_thenReturnBadRequest()
            throws Exception {

        mockMvc.perform(get("/staffs")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "3")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "d")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    /**
     * When find staff but invalid authentication then return Forbidden
     * @throws Exception
     */
    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidAuthentication_thenReturnForbidden()
            throws Exception {

        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpass"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "3")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "d")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isForbidden());
    }

    /**
     * When create new department and create successfully
     * @throws Exception
     */
    @Test
    public void when_createNewDepartment_thenCreateSuccessfully()
            throws Exception {

        JSONObject departmentJson = createDepJsonObject();
        departmentJson.remove("name");
        departmentJson.put("name", "testSaveName");
        departmentJson.put("id", 4);
        mockMvc.perform(post("/departments")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testSaveName"));
    }

    /**
     * When update existed department then update successfully
     * @throws Exception
     */
    @Test
    public void when_updateExistDepartment_thenCreateSuccessfully()
            throws Exception {
        DepartmentModel paramModel = createDepartmentModel();
        paramModel.setId(5);
        DepartmentEntity updatedDepartment = new DepartmentEntity(paramModel);
        updatedDepartment.setName("testUpdateDepartment");
        DepartmentEntity savedDepartment = departmentRepository.save(updatedDepartment);

        JSONObject departmentJson = createDepJsonObject();
        departmentJson.remove("name");
        departmentJson.put("name", "testUpdatedName");
        mockMvc.perform(put("/departments/{departmentId}", savedDepartment.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(departmentJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testUpdatedName"));
    }

    /**
     * When delete existed department then delete successfully
     * @throws Exception
     */
    @Test
    public void when_deleteExistDepartment_thenDeleteSuccessfully()
            throws Exception {
        DepartmentModel paramModel = createDepartmentModel();
        paramModel.setId(6);
        DepartmentEntity updatedDepartment = new DepartmentEntity(paramModel);
        updatedDepartment.setName("testDeleteDepartment");
        DepartmentEntity savedDepartment = departmentRepository.save(updatedDepartment);

        mockMvc.perform(delete("/departments/{departmentId}", savedDepartment.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testDeleteDepartment"));
    }

    /**
     * When find existed department then return department
     * @throws Exception
     */
    @Test
    public void when_findExistDepartment_thenReturnDepartment()
            throws Exception {
        DepartmentModel paramModel = createDepartmentModel();
        paramModel.setId(7);
        DepartmentEntity searchedDepartment = new DepartmentEntity(paramModel);
        searchedDepartment.setName("testFindExistDepartment");
        DepartmentEntity savedDepartment = departmentRepository.save(searchedDepartment);

        mockMvc.perform(get("/departments/{departmentId}", savedDepartment.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testFindExistDepartment"));
    }

    /**
     * When find staffs by department id then return resource of staffs that contains list of staffs
     * @throws Exception
     */
    @Test
    public void when_findStaffsByDepartmentIdWithAscFirstname_thenReturnResource() throws Exception {

        DepartmentEntity findFirstName = new DepartmentEntity(createDepartmentModel());
        findFirstName.setId(10);
        findFirstName.setName("testFindDepartment");
        DepartmentEntity savedDepartment = departmentRepository.save(findFirstName);

        StaffEntity staffEntity1 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity1.setFirstName("zzza");
        staffEntity1.setId(11);
        staffEntity1.setUsername("username1");
        StaffEntity actualStaff1 = staffRepository.save(staffEntity1);

        StaffEntity staffEntity2 = new StaffEntity(createStaffModel(), savedDepartment);
        staffEntity2.setFirstName("aaaa");
        staffEntity2.setUsername("username2");
        staffEntity2.setId(11);
        StaffEntity actualStaff2 = staffRepository.save(staffEntity2);

        List<StaffModel> staffModelList = new ArrayList<>();
        StaffModel expectModel = createStaffModel();
        expectModel.setFirstName("aaaa");
        expectModel.setId(actualStaff2.getId());
        expectModel.setPassword(null);
        expectModel.setUsername("username2");
        staffModelList.add(expectModel);
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 1,2, staffModelList);
        mockMvc.perform(get("/departments/{departmentId}/staffs", savedDepartment.getId())
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("sortBy", "firstName")
                .param("sortType", "asc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    ResourceModel<LinkedHashMap<String, Object>> actualResource = new ObjectMapper().readValue(actualJson, ResourceModel.class);
                    compareTwoResourceStaff(expectedResource, convertToStaffResource(actualResource));
                });
    }

    /**
     * When find departments by department name then return resource of staffs that contains list of department
     * @throws Exception
     */
    @Test
    public void when_findDepartmentsLikeNameWithAscName_thenReturnResource() throws Exception {

        DepartmentEntity department1 = new DepartmentEntity(createDepartmentModel());
        department1.setId(100);
        department1.setName("yyyyyx");

        DepartmentEntity department2 = new DepartmentEntity(createDepartmentModel());
        department2.setId(101);
        department2.setName("xxxxxx");

        DepartmentEntity actualSDep1 = departmentRepository.save(department1);
        DepartmentEntity actualSDep2 = departmentRepository.save(department2);

        List<DepartmentModel> expectList = new ArrayList<>();
        expectList.add(new DepartmentModel(actualSDep2));
        expectList.add(new DepartmentModel(actualSDep1));
        ResourceModel<DepartmentModel> expectedResource = createDepartmentResource(2, 0, 2,1, expectList);
        mockMvc.perform(get("/departments")
                .with(httpBasic("root", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "2")
                .param("sortBy", "name")
                .param("sortType", "asc")
                .param("searchedName", "x")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    ResourceModel<LinkedHashMap<String, Object>> actualResource = new ObjectMapper().readValue(actualJson, ResourceModel.class);
                    compareTwoResourceDepartment(expectedResource, convertToDepartmentResource(actualResource));
                });
    }
}
