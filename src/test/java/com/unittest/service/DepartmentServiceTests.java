package com.unittest.service;

import com.customexception.DuplicatedEntityException;
import com.customexception.NoSuchEntityException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.model.DepartmentModel;
import com.model.PaginationModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import com.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.utils.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * DepartmentService test
 */
public class DepartmentServiceTests {
    private DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
    private StaffRepository staffRepository = Mockito.mock(StaffRepository.class);

    /**
     * create department and save successfully
     *
     */
    @Test
    public void when_saveDepartment_thenSaveSuccessfully()  {
        when(departmentRepository.existsDepartmentEntitiesByName(anyString())).thenReturn(false);

        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
        when(departmentRepository.save(any())).thenReturn(savedEntity);

        DepartmentModel expectedModel = createDepartmentModel();
        DepartmentModel paramModel = createDepartmentModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdateAt(null);
        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).createDepartment(paramModel);
        assertTrue(compareTwoDepartment(expectedModel, actualModel));
    }

    /**
     * Create department but name of department has been existed
     */
    @Test
    public void when_saveDepartmentWithExistName_thenThrowDuplicateEntityException() {
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        when(departmentRepository.existsDepartmentEntitiesByName(anyString())).thenReturn(true);


        DepartmentModel paramModel = createDepartmentModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdateAt(null);

        assertThrows(DuplicatedEntityException.class,
                () -> new DepartmentService(departmentRepository, staffRepository).createDepartment(paramModel));
    }

    /**
     * Delete a department and delete successfully
     */
    @Test
    public void when_deleteExistDepartment_thenDeleteSuccessfully(){
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenReturn(savedEntity);
        when(departmentRepository.save(any())).thenReturn(savedEntity);

        DepartmentModel expectedModel = createDepartmentModel();

        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).deleteDepartment(10);
        assertTrue(compareTwoDepartment(expectedModel, actualModel));
    }

    /**
     * Delete department but can not find department by id
     */
    @Test
    public void when_DeleteNotExistDepartment_thenThrowNoSuchEntityException(){
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findById(any())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

        assertThrows(NoSuchEntityException.class, () ->
                new DepartmentService(departmentRepository, staffRepository).deleteDepartment(1));
    }

    /**
     * Find a department successfully
     */
    @Test
    public void when_findExistDepartment_thenReturnModelSuccessfully(){
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(anyInt())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenReturn(savedEntity);

        DepartmentModel expectedModel = createDepartmentModel();

        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).findDepartmentById(10);
        assertTrue(compareTwoDepartment(expectedModel, actualModel));
    }

    /**
     * Find a department does not existed
     */
    @Test
    public void when_FindNotExistDepartment_thenThrowNosuchEntity(){
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(any())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

        assertThrows(NoSuchEntityException.class, ()
                -> new DepartmentService(departmentRepository, staffRepository).findDepartmentById(1));
    }

    /**
     * Update a existed department and update successfully
     *
     */
    @Test
    public void when_UpdateExistDepartment_thenUpdateSuccessfully() {
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(any())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenReturn(new DepartmentEntity(createDepartmentModel()));
        when(departmentRepository.existsDepartmentEntitiesByNameAndIdNot(anyString(), anyInt())).thenReturn(false);
        when(departmentRepository.save(any())).thenReturn(savedEntity);

        DepartmentModel expectedModel = createDepartmentModel();

        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository)
                .updateDepartment(1, createDepartmentModel());
        assertTrue(compareTwoDepartment(expectedModel, actualModel));
    }


    /**
     * Update a department but name has been existed
     */
    @Test
    public void when_UpdateExistNameOfDepartment_thenThrowDuplicatedEntityException() {

        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        //Find existed department entity
        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findDepartmentById(any())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenReturn(savedEntity);
        when(departmentRepository.existsDepartmentEntitiesByNameAndIdNot(anyString(), anyInt())).thenReturn(true);

        assertThrows(DuplicatedEntityException.class,
                () -> new DepartmentService(departmentRepository, staffRepository)
                        .updateDepartment(1, createDepartmentModel()));
    }

    /**
     * Update not exist department but can not find department by id
     */
    @Test
    public void when_UpdateNotExistDepartment_thenThrowNoSuchEntityException() {
        DepartmentModel departmentModel = createDepartmentModel();
        departmentModel.setUpdateAt(null);

        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
        when(departmentRepository.findById(any())).thenReturn(optional);
        when(optional.orElseThrow(any())).thenThrow(NoSuchEntityException.class);

        assertThrows(NoSuchEntityException.class, () -> new DepartmentService(departmentRepository, staffRepository)
                .updateDepartment(1, createDepartmentModel()));
    }

    /**
     * Find not exist department
     */
    @Test
    public void when_findStaffByNoExistDepartmentId_ThenThrowNoSuchElementException() {
        when(departmentRepository.existsDepartmentEntitiesByIdAndDeleteAtNull(anyInt())).thenReturn(false);

        assertThrows(NoSuchEntityException.class,
                () -> new DepartmentService(departmentRepository, staffRepository)
        .findAllStaffByDepartmentId(1, new PaginationModel(0,2,"firstName", "asc")));
    }

    /**
     * Find staffs by department id then return result
     */
    @Test
    public void when_findStaffsByDepartmentIdSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
        List<StaffEntity> entityList = new ArrayList<>();
        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
        Page<StaffEntity> entityPage = new PageImpl<>(entityList);

        when(departmentRepository.existsDepartmentEntitiesByIdAndDeleteAtNull(anyInt())).thenReturn(true);
        when(staffRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        ResourceModel<StaffModel> actualResource = new DepartmentService(departmentRepository, staffRepository).
                findAllStaffByDepartmentId(1, new PaginationModel(0, 1, "firstName", "asc"));

        List<StaffModel> actualStaffList = actualResource.getData();
        for (StaffModel model: actualStaffList) {
            model.setDepartmentModel(createDepartmentModel());
            model.setDepId(1);
        }

        List<StaffModel> modelList = new ArrayList<>();
        StaffModel expectModel = createStaffModel();
        modelList.add(expectModel);
        ResourceModel<StaffModel> expectedResource = createStaffResource(1, 0,1, 1, modelList);
        compareTwoResourceStaff(expectedResource, actualResource);
    }

    /**
     * Find departments like name then return result
     */
    @Test
    public void when_findDepartmentLikeNameSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
        List<DepartmentEntity> entityList = new ArrayList<>();
        entityList.add(new DepartmentEntity(createDepartmentModel()));
        Page<DepartmentEntity> entityPage = new PageImpl<>(entityList);

        when(departmentRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        ResourceModel<DepartmentModel> actualResource = new DepartmentService(departmentRepository, staffRepository).
                findDepartmentLikeName("", new PaginationModel(0, 1, "name", "asc"));

        List<DepartmentModel> modelList = new ArrayList<>();
        DepartmentModel expectModel = createDepartmentModel();
        modelList.add(expectModel);
        ResourceModel<DepartmentModel> expectedResource = createDepartmentResource(1, 0,1, 1, modelList);
        compareTwoResourceDepartment(expectedResource, actualResource);
    }

}
