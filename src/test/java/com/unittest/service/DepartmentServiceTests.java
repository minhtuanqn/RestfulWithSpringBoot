//package com.unittest.service;
//
//import com.entity.DepartmentEntity;
//import com.entity.StaffEntity;
//import com.model.DepartmentModel;
//import com.model.StaffModel;
//import com.model.StaffResourceModel;
//import com.repository.DepartmentRepository;
//import com.repository.StaffRepository;
//import com.service.DepartmentService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//
//import java.sql.SQLIntegrityConstraintViolationException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static com.utils.TestsUtils.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
///**
// * DepartmentService test
// */
//public class DepartmentServiceTests {
//    private DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
//    private StaffRepository staffRepository = Mockito.mock(StaffRepository.class);
//
//    /**
//     * create department and save successfully
//     * @throws SQLIntegrityConstraintViolationException
//     */
//    @Test
//    public void when_saveDepartment_thenSaveSuccessfully() throws SQLIntegrityConstraintViolationException {
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        when(departmentRepository.save(any())).thenReturn(savedEntity);
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        DepartmentModel paramModel = createDepartmentModel();
//        paramModel.setCreateAt(createMockTime());
//        paramModel.setUpdateAt(null);
//        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).createDepartment(paramModel);
//        assertTrue(compareTwoDepartment(expectedModel, actualModel));
//    }
//
//    /**
//     * Create department but name of department has been existed
//     */
//    @Test
//    public void when_saveDepartmentWithExistName_thenThrowSQLIntegrityConstraintViolationException() {
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        when(departmentRepository.existsDepartmentEntitiesByName(anyString())).thenReturn(true);
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        DepartmentModel paramModel = createDepartmentModel();
//        paramModel.setCreateAt(createMockTime());
//        paramModel.setUpdateAt(null);
//
//        assertThrows(SQLIntegrityConstraintViolationException.class,
//                () -> new DepartmentService(departmentRepository, staffRepository).createDepartment(paramModel));
//    }
//
//    /**
//     * Delete a department and delete successfully
//     */
//    @Test
//    public void when_deleteExistDepartment_thenDeleteSuccessfully(){
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(true);
//        when(optional.get()).thenReturn(new DepartmentEntity(departmentModel));
//        doNothing().when(departmentRepository).deleteById(anyInt());
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).deleteDepartment(10);
//        assertTrue(compareTwoDepartment(expectedModel, actualModel));
//    }
//
//    /**
//     * Delete department but can not find department by id
//     */
//    @Test
//    public void when_DeleteNotExistDepartment_thenThrowNoSuchElementException(){
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(false);
//
//        assertThrows(NoSuchElementException.class, () ->
//                new DepartmentService(departmentRepository, staffRepository).deleteDepartment(1));
//    }
//
//    /**
//     * Find a department successfully
//     */
//    @Test
//    public void when_findExistDepartment_thenReturnModelSuccessfully(){
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(true);
//        when(optional.get()).thenReturn(new DepartmentEntity(departmentModel));
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository).findDepartmentById(10);
//        assertTrue(compareTwoDepartment(expectedModel, actualModel));
//    }
//
//    /**
//     * Find a department does not existed
//     */
//    @Test
//    public void when_FindNotExistDepartment_thenReturnNull(){
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(false);
//
//        assertThrows(NoSuchElementException.class, ()
//                -> new DepartmentService(departmentRepository, staffRepository).findDepartmentById(1));
//    }
//
//    /**
//     * Update a existed department and update successfully
//     * @throws SQLIntegrityConstraintViolationException
//     */
//    @Test
//    public void when_UpdateExistDepartment_thenUpdateSuccessfully()
//            throws SQLIntegrityConstraintViolationException {
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(true);
//        when(optional.get()).thenReturn(new DepartmentEntity(createDepartmentModel()));
//        when(departmentRepository.save(any())).thenReturn(savedEntity);
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        DepartmentModel actualModel = new DepartmentService(departmentRepository, staffRepository)
//                .updateDepartment(1, createDepartmentModel());
//        assertTrue(compareTwoDepartment(expectedModel, actualModel));
//    }
//
//
//    /**
//     * Update a department but name has been existed
//     */
//    @Test
//    public void when_UpdateExistNameOfDepartment_thenThrowSQLIntegrityConstraintViolationException() {
//
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        DepartmentEntity savedEntity = new DepartmentEntity(departmentModel);
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(true);
//        when(optional.get()).thenReturn(new DepartmentEntity(createDepartmentModel()));
//        when(departmentRepository.save(any())).thenReturn(savedEntity);
//        when(departmentRepository.existsDepartmentEntitiesByNameAndIdNot(anyString(), anyInt())).thenReturn(true);
//
//        DepartmentModel expectedModel = createDepartmentModel();
//
//        assertThrows(SQLIntegrityConstraintViolationException.class,
//                () -> new DepartmentService(departmentRepository, staffRepository)
//                        .updateDepartment(1, createDepartmentModel()));
//    }
//
//    /**
//     * Update not exist department but can not find department by id
//     */
//    @Test
//    public void when_UpdateNotExistDepartment_thenThrowNoSuchElementException() {
//        DepartmentModel departmentModel = createDepartmentModel();
//        departmentModel.setUpdateAt(null);
//
//        Optional<DepartmentEntity> optional = Mockito.mock(Optional.class);
//        when(departmentRepository.findById(any())).thenReturn(optional);
//        when(optional.isPresent()).thenReturn(false);
//
//        assertThrows(NoSuchElementException.class, () -> new DepartmentService(departmentRepository, staffRepository)
//                .updateDepartment(1, createDepartmentModel()));
//    }
//
//    /**
//     * Find not exist department
//     */
//    @Test
//    public void when_findStaffByNoExistDepartmentId_ThenThrowNoSuchElementException() {
//        when(!departmentRepository.existsDepartmentEntitiesById(anyInt())).thenReturn(false);
//
//        assertThrows(NoSuchElementException.class,
//                () -> new DepartmentService(departmentRepository, staffRepository)
//        .findAllStaffByDepartmentId(1,1,1, "firstName", "asc"));
//    }
//
//    /**
//     * Find staffs by department id then return result
//     */
//    @Test
//    public void when_findStaffsByDepartmentIdSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
//        List<StaffEntity> entityList = new ArrayList<>();
//        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
//        Page<StaffEntity> entityPage = new PageImpl<>(entityList);
//
//        when(departmentRepository.existsDepartmentEntitiesById(anyInt())).thenReturn(true);
//        when(staffRepository.findStaffEntitiesByDepartmentEntityIdEquals(anyInt(), any())).thenReturn(entityPage);
//
//        StaffResourceModel actualResource = new DepartmentService(departmentRepository, staffRepository).
//                findAllStaffByDepartmentId(1,0, 1,
//                        "firstName", "asc");
//
//        List<StaffModel> modelList = new ArrayList<>();
//        StaffModel expectModel = createStaffModel();
//        modelList.add(expectModel);
//        StaffResourceModel expectedResource = createStaffResource(1, 0,1, 1, modelList);
//        compareTwoResource(expectedResource, actualResource);
//    }
//
//
//
//
//}
