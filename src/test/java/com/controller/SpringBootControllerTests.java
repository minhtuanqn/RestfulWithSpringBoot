package com.controller;

import com.config.TestConfig;
import com.entity.StaffEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.StaffModel;
import com.model.StaffResource;
import com.repository.DefaultRepository;
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
import java.util.List;

import static com.utils.TestsUtils.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private DefaultRepository defaultRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    public void when_saveStaffByPostMethod_thenSuccessfullySave() throws Exception {
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(post("/staffs")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
//                .andExpect(jsonPath("$.createAt").value(jsonPath("$.createAt")))
                .andExpect(jsonPath("$.updatedAt").doesNotExist());
    }

    @Test
    public void when_saveInvalidStaffByPostMethod_thenReturnBadRequest() throws Exception {
        JSONObject paramJson = createJsonObj();
        paramJson.put("firstName", "4");
        mockMvc.perform(post("/staffs")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void when_updateExistedStaffByPostMethod_thenSuccessfullyUpdate() throws Exception {
        JSONObject paramJson = createJsonObj();
        paramJson.put("firstName", "updatedFirstname");
        paramJson.put("lastName", "updatedLastname");

        StaffModel savedStaff = createStaffModel();
        defaultRepository.save(new StaffEntity(savedStaff));

        mockMvc.perform(put("/staffs/{id}", 1)
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("updatedFirstname"))
                .andExpect(jsonPath("$.lastName").value("updatedLastname"))
//                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()));
        ;
    }

    @Test
    public void when_updateNotExistStaffByPostMethod_thenReturnNotFound() throws Exception {
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(put("/staffs/{id}", 1)
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_updateStaffWithInvalidIDByPostMethod_thenReturnBadRequest() throws Exception {
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(put("/staffs/{id}", -3)
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_deleteExistedStaff_thenSuccessfullyDelete() throws Exception {
        StaffModel savedStaff = createStaffModel();
        StaffEntity staffEntity = defaultRepository.save(new StaffEntity(savedStaff));

        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", staffEntity.getId())
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
        ;
    }

    @Test
    public void when_deleteNotExistStaffByDeleteMethod_thenReturnNotFound() throws Exception {
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", 100)
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_deleteStaffWithInvalidID_thenReturnBadRequest() throws Exception {
        JSONObject paramJson = createJsonObj();
        mockMvc.perform(delete("/staffs/{id}", "a")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffById_theReturnStaff() throws Exception {
        StaffEntity staffEntity = defaultRepository.save(new StaffEntity(createStaffModel()));
        mockMvc.perform(get("/staffs/{id}", staffEntity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic("username", "userpassword"))
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(staffEntity.getId()))
                .andExpect(jsonPath("$.firstName").value("firstName"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.createAt").value(createMockTime().toString()))
                .andExpect(jsonPath("$.updatedAt").value(createMockTime().toString()));
        ;
    }

    @Test
    public void when_findStaffByInvalidId_theReturnBadRequest() throws Exception {
        mockMvc.perform(get("/staffs/{id}", -6)
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void when_findStaffWithNotExistID_thenReturnNotfound() throws Exception {
        mockMvc.perform(get("/staffs/{id}", "100")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void when_findStaffByFirstNameOrLastNameWithDscFirstname_thenReturnResource() throws Exception {

        defaultRepository.deleteAll();

        StaffEntity staffEntity1 = new StaffEntity(createStaffModel());
        staffEntity1.setId(null);
        StaffEntity actualStaff1 = defaultRepository.save(staffEntity1);

        StaffEntity staffEntity2 = new StaffEntity(createStaffModel());
        staffEntity2.setId(null);
        StaffEntity actualStaff2 = defaultRepository.save(staffEntity2);

        List<StaffModel> staffModelList = new ArrayList<>();
        StaffModel expectModel = createStaffModel();
        expectModel.setId(actualStaff1.getId());
        staffModelList.add(expectModel);
        StaffResource expectedResource = createStaffResource(2, 0, 1,2, staffModelList);
        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    StaffResource actualResource = new ObjectMapper().readValue(actualJson, StaffResource.class);
                    compareTwoResource(expectedResource, actualResource);
                });
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithAscFirstname_thenReturnResource() throws Exception {

        defaultRepository.deleteAll();

        StaffEntity staffEntity1 = new StaffEntity(createStaffModel());
        staffEntity1.setFirstName("zzza");
        staffEntity1.setId(null);
        StaffEntity actualStaff1 = defaultRepository.save(staffEntity1);

        StaffEntity staffEntity2 = new StaffEntity(createStaffModel());
        staffEntity2.setFirstName("aaaa");
        staffEntity2.setId(null);
        StaffEntity actualStaff2 = defaultRepository.save(staffEntity2);

        List<StaffModel> staffModelList = new ArrayList<>();
        StaffModel expectModel = createStaffModel();
        expectModel.setFirstName("aaaa");
        expectModel.setId(actualStaff2.getId());
        staffModelList.add(expectModel);
        StaffResource expectedResource = createStaffResource(2, 0, 1,2, staffModelList);
        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "firstName")
                .param("sortType", "asc")
                .content(new JSONObject().toString()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String actualJson = result.getResponse().getContentAsString();
                    StaffResource actualResource = new ObjectMapper().readValue(actualJson, StaffResource.class);
                    compareTwoResource(expectedResource, actualResource);
                });
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidNotNegativeParam_thenReturnBadRequest()
            throws Exception {
        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpassword"))
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
    public void when_findStaffByFirstNameOrLastNameWithInvalidGreaterThanZeroParam_thenReturnBadRequest()
            throws Exception {
        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpassword"))
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
    public void when_findStaffByFirstNameOrLastNameWithInvalidSortBy_thenReturnBadRequest()
            throws Exception {
        mockMvc.perform(get("/staffs")
                .with(httpBasic("username", "userpassword"))
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "3")
                .param("perPage", "1")
                .param("searchedValue", "a")
                .param("sortBy", "d")
                .param("sortType", "dsc")
                .content(new JSONObject().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void when_findStaffByFirstNameOrLastNameWithInvalidAuthentication_thenReturnBadRequest()
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
}
