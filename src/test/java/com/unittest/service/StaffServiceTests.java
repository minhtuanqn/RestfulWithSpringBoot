package com.unittest.service;

import com.customexception.DuplicatedEntityException;
import com.customexception.NoSuchEntityException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.model.PaginationModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import com.service.StaffService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static com.utils.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class StaffServiceTests {
    StaffRepository staffRepository = Mockito.mock(StaffRepository.class);
    DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    /**
     * Create a staff then save successfully
     */
    @Test
    public void when_saveStaff_thenSaveSuccessfully() {
        StaffModel staffModel = createStaffModel();
        staffModel.setUpdatedAt(null);

        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenReturn(new DepartmentEntity(createDepartmentModel()));

        when(staffRepository.existsStaffEntitiesByUsername(anyString())).thenReturn(false);
        when(staffRepository.save(any())).thenReturn(new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel())));

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        StaffModel actualModel = new StaffService(staffRepository, departmentRepository, passwordEncoder).createStaff(paramModel);
        staffModel.setDepartmentModel(createDepartmentModel());
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    /**
     * Assign a staff for a department by department does not exist
     */
    @Test
    public void when_createStaffWithDoesNotExistDepartment_thenThrowNoSuchEntityException() {
        when(departmentRepository.findDepartmentById(anyInt())).thenThrow(NoSuchEntityException.class);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        assertThrows(NoSuchEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).createStaff(paramModel));
    }

    /**
     * Create a staff but username has been existed
     */
    @Test
    public void when_createStaffWithExistUsername_thenThrowDuplicatedEntityException() {
        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optional);
        when(optional.orElseThrow()).thenReturn(new DepartmentEntity(createDepartmentModel()));
        when(staffRepository.existsStaffEntitiesByUsername(anyString())).thenReturn(true);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        assertThrows(DuplicatedEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).createStaff(paramModel));
    }

    /**
     * Update a existed staff then updated successfully
     */
    @Test
    public void when_updateStaff_thenUpdateSuccessfully() {
        StaffModel staffModel = createStaffModel();
        StaffEntity existEntity = new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel()));
        existEntity.setCreateAt(createMockTime());

        //Check exist or not department
        Optional<DepartmentEntity> departmentOptional = Mockito.mock(Optional.class);
        DepartmentEntity existedDepEntity = new DepartmentEntity(createDepartmentModel());
        existedDepEntity.setStaffList(createStaffEntityList());
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(departmentOptional);
        when(departmentOptional.orElseThrow(any())).thenReturn(existedDepEntity);

        //Check exist or not staff
        Optional<StaffEntity> staffOptional = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenReturn(staffOptional);
        when(staffOptional.orElseThrow(any())).thenReturn(new StaffEntity(createStaffModel(), existedDepEntity));

        //Check exist username of staff
        when(staffRepository.existsStaffEntityByIdNotAndUsernameEquals(anyInt(), anyString())).thenReturn(false);
        StaffEntity savedEntity = new StaffEntity(staffModel, existedDepEntity);
        savedEntity.setUpdateAt(LocalDateTime.now());
        when(staffRepository.save(any())).thenReturn(savedEntity);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdatedAt(null);
        StaffModel actualModel = new StaffService(staffRepository, departmentRepository, passwordEncoder).updateStaff(paramModel);
        staffModel.setDepartmentModel(createDepartmentModel());
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    /**
     * Update a staff but updated department does not exist
     */
    @Test
    public void when_updateStaffWithDoesNotExistDepartment_thenThrowNoSuchEntityException() {
        when(departmentRepository.findDepartmentById(anyInt())).thenThrow(NoSuchEntityException.class);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        assertThrows(NoSuchEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).updateStaff(paramModel));
    }

    /**
     * Update a staff but updated username has been existed
     */
    @Test
    public void when_updatedStaffWithExistUsername_thenThrowDuplicatedEntityException() {
        Optional<DepartmentEntity> optionalDepartmentEntity = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optionalDepartmentEntity);
        when(optionalDepartmentEntity.orElseThrow(any())).thenReturn(new DepartmentEntity(createDepartmentModel()));

        Optional<StaffEntity> optionalStaffEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenReturn(optionalStaffEntity);
        when(optionalStaffEntity.orElseThrow(any())).thenReturn(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));

        when(staffRepository.existsStaffEntityByIdNotAndUsernameEquals(any(), anyString())).thenReturn(true);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        assertThrows(DuplicatedEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).updateStaff(paramModel));
    }

    /**
     * Update not existed staff then throw exception
     */
    @Test
    public void when_updateDoesNotExistStaff_thenThrowNoSuchEntityException() {
        Optional<DepartmentEntity> optionalDepartmentEntity = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optionalDepartmentEntity);
        when(optionalDepartmentEntity.orElseThrow(any())).thenReturn(new DepartmentEntity(createDepartmentModel()));

        Optional<StaffEntity> optionalStaffEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenReturn(optionalStaffEntity);
        when(optionalStaffEntity.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdatedAt(null);

        assertThrows(NoSuchEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).updateStaff(paramModel));
    }

    /**
     * Find existed staff by id then return staff
     */
    @Test
    public void when_findStaffById_thenReturnStaff() {
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenReturn(existEntity);
        when(existEntity.orElseThrow(any())).thenReturn(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));

        StaffModel actualModel = new StaffService(staffRepository, departmentRepository, passwordEncoder).findById(1);
        staffModel.setDepartmentModel(createDepartmentModel());
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    /**
     * Find not existed staff then throw exception
     */
    @Test
    public void when_findDoesNotExistStaffById_thenThrowNoSuchEntityException() {
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenThrow(NoSuchEntityException.class);

        assertThrows(NoSuchEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).findById(1));
    }

    /**
     * Delete a staff and delete successfully
     */
    @Test
    public void when_deleteStaff_thenDeleteSuccessfully() {
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenReturn(existEntity);
        when(existEntity.orElseThrow(any())).thenReturn(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));

        when(staffRepository.save(any())).thenReturn(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));

        StaffModel actualStaff = new StaffService(staffRepository, departmentRepository, passwordEncoder).deleteStaffById(1);
        staffModel.setDepartmentModel(createDepartmentModel());
        assertTrue(compareTwoStaff(staffModel, actualStaff));

    }

    /**
     * Delete not existed staff then throw Exception
     */
    @Test
    public void when_deleteSDoesNotExistStaff_thenReturnNoSuchEntityException() {
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(staffRepository.findStaffById(anyInt())).thenThrow(NoSuchEntityException.class);
        when(existEntity.isPresent()).thenReturn(false);
        assertThrows(NoSuchEntityException.class,
                () -> new StaffService(staffRepository, departmentRepository, passwordEncoder).deleteStaffById(1));

    }

    /**
     * Find staff by search value then return pagination of staff model by Dsc
     */
    @Test
    public void when_findStaffsSortByDsc_thenReturnResourceOfListOfStaffsByDsc() {
        List<StaffEntity> entityList = new ArrayList<>();
        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
        when(entityPage.toList()).thenReturn(entityList);
        when(entityPage.getTotalElements()).thenReturn(2l);
        when(entityPage.getTotalPages()).thenReturn(2);

        when(staffRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        ResourceModel<StaffModel> actualResource = new StaffService(staffRepository, departmentRepository, passwordEncoder).
                findByLastnameOrFirstname(new PaginationModel(0, 1, "firstName", "dsc"), "a");

        List<StaffModel> modelList = new ArrayList<>();
        StaffModel expectedModel = createStaffModel();
        expectedModel.setDepartmentModel(createDepartmentModel());
        modelList.add(expectedModel);
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0, 1, 2, modelList);
        compareTwoResourceStaff(expectedResource, actualResource);
    }

    /**
     * Find staff by search value then return pagination of staff model by Asc
     */
    @Test
    public void when_findStaffsSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
        List<StaffEntity> entityList = new ArrayList<>();
        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
        when(entityPage.toList()).thenReturn(entityList);
        when(entityPage.getTotalElements()).thenReturn(2l);
        when(entityPage.getTotalPages()).thenReturn(2);
        when(staffRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        ResourceModel<StaffModel> actualResource = new StaffService(staffRepository, departmentRepository, passwordEncoder).
                findByLastnameOrFirstname(new PaginationModel(0, 1, "firstName", "asc"), "a");

        List<StaffModel> actualStaffList = actualResource.getData();
        for (StaffModel model: actualStaffList) {
            model.setDepartmentModel(createDepartmentModel());
            model.setDepId(1);
        }

        List<StaffModel> modelList = new ArrayList<>();
        StaffModel expectModel = createStaffModel();
        expectModel.setDepartmentModel(createDepartmentModel());
        modelList.add(expectModel);
        ResourceModel<StaffModel> expectedResource = createStaffResource(2, 0,1, 2, modelList);
        compareTwoResourceStaff(expectedResource, actualResource);
    }
}
