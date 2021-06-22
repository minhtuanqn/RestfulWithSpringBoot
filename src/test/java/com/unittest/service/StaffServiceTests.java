//package com.unittest.service;
//
//import com.entity.DepartmentEntity;
//import com.entity.StaffEntity;
//import com.model.StaffModel;
//import com.model.StaffResourceModel;
//import com.repository.StaffRepository;
//import com.service.DepartmentService;
//import com.service.StaffService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.sql.SQLIntegrityConstraintViolationException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static com.utils.TestsUtils.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//public class StaffServiceTests {
//    StaffRepository staffRepository = Mockito.mock(StaffRepository.class);
//    DepartmentService departmentSevice = Mockito.mock(DepartmentService.class);
//
//    /**
//     * Create a staff then save successfully
//     * @throws SQLIntegrityConstraintViolationException
//     */
//    @Test
//    public void when_saveStaff_thenSaveSuccessfully() throws SQLIntegrityConstraintViolationException {
//        StaffModel staffModel = createStaffModel();
//        staffModel.setUpdatedAt(null);
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.save(any())).thenReturn(new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel())));
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(null);
//        paramModel.setUpdatedAt(null);
//        StaffModel actualModel = new StaffService(staffRepository, departmentSevice).createStaff(paramModel, 1);
//        staffModel.setDepartmentModel(createDepartmentModel());
//        assertTrue(compareTwoStaff(staffModel, actualModel));
//    }
//
//    /**
//     * Assign a staff for a department by department does not exist
//     */
//    @Test
//    public void when_createStaffWithDoesNotExistDepartment_thenThrowNoSuchElementException() {
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(null);
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(null);
//        paramModel.setUpdatedAt(null);
//        assertThrows(NoSuchElementException.class,
//                () -> new StaffService(staffRepository, departmentSevice).createStaff(paramModel, 1));
//    }
//
//    /**
//     * Create a staff but username has been existed
//     */
//    @Test
//    public void when_createStaffWithExistUsername_thenThrowSQLIntegrityConstraintViolationException() {
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.existsStaffEntitiesByUsername(anyString())).thenReturn(true);
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(null);
//        paramModel.setUpdatedAt(null);
//        assertThrows(SQLIntegrityConstraintViolationException.class,
//                () -> new StaffService(staffRepository, departmentSevice).createStaff(paramModel, 1));
//    }
//
//    /**
//     * Update a existed staff then updated successfully
//     * @throws SQLIntegrityConstraintViolationException
//     */
//    @Test
//    public void when_updateStaff_thenUpdateSuccessfully() throws SQLIntegrityConstraintViolationException {
//        StaffModel staffModel = createStaffModel();
//        StaffEntity existEntity = new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel()));
//        existEntity.setCreateAt(createMockTime());
//
//        Optional<StaffEntity> existOptional = Mockito.mock(Optional.class);
//        when(staffRepository.findById(anyInt())).thenReturn(existOptional);
//        when(existOptional.isPresent()).thenReturn(true);
//        when(existOptional.get()).thenReturn(existEntity);
//
//        StaffEntity savedEntity = new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel()));
//        savedEntity.setUpdateAt(LocalDateTime.now());
//        when(staffRepository.save(any())).thenReturn(savedEntity);
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(createMockTime());
//        paramModel.setUpdatedAt(null);
//        StaffModel actualModel = new StaffService(staffRepository, departmentSevice).updateStaff(paramModel, 1);
//        staffModel.setDepartmentModel(createDepartmentModel());
//        assertTrue(compareTwoStaff(staffModel, actualModel));
//    }
//
//    /**
//     * Update a staff but updated department does not exist
//     */
//    @Test
//    public void when_updateStaffWithDoesNotExistDepartment_thenThrowNoSuchElementException() {
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(null);
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(null);
//        paramModel.setUpdatedAt(null);
//        assertThrows(NoSuchElementException.class,
//                () -> new StaffService(staffRepository, departmentSevice).updateStaff(paramModel, 1));
//    }
//
//    /**
//     * Update a staff but updated username has been existed
//     */
//    @Test
//    public void when_updatedStaffWithExistUsername_thenThrowSQLIntegrityConstraintViolationException() {
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.existsStaffEntityByIdNotAndUsernameEquals(any(), anyString())).thenReturn(true);
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(null);
//        paramModel.setUpdatedAt(null);
//        assertThrows(SQLIntegrityConstraintViolationException.class,
//                () -> new StaffService(staffRepository, departmentSevice).updateStaff(paramModel, 1));
//    }
//
//    /**
//     * Update not existed staff then throw exception
//     */
//    @Test
//    public void when_updateDoesNotExistStaff_thenThrowNoSuchElementException() {
//        StaffModel staffModel = createStaffModel();
//
//        Optional<StaffEntity> existOptional = Mockito.mock(Optional.class);
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.existsStaffEntityByIdNotAndUsernameEquals(anyInt(), anyString())).thenReturn(false);
//        when(staffRepository.findById(anyInt())).thenReturn(existOptional);
//        when(existOptional.isPresent()).thenReturn(false);
//
//        StaffModel paramModel = createStaffModel();
//        paramModel.setCreateAt(createMockTime());
//        paramModel.setUpdatedAt(null);
//
//        assertThrows(NoSuchElementException.class,
//                () -> new StaffService(staffRepository, departmentSevice).updateStaff(paramModel, 1));
//    }
//
//    /**
//     * Find existed staff by id then return staff
//     */
//    @Test
//    public void when_findStaffById_thenReturnStaff(){
//        StaffModel staffModel = createStaffModel();
//        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.findById(anyInt())).thenReturn(existEntity);
//        when(existEntity.isPresent()).thenReturn(true);
//        when(existEntity.get()).thenReturn(new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel())));
//
//        StaffModel actualModel = new StaffService(staffRepository, departmentSevice).findById(1);
//        staffModel.setDepartmentModel(createDepartmentModel());
//        assertTrue(compareTwoStaff(staffModel, actualModel));
//    }
//
//    /**
//     * Find not existed staff then throw exception
//     */
//    @Test
//    public void when_findDoesNotExistStaffById_thenThrowNoSuchElementException(){
//        StaffModel staffModel = createStaffModel();
//        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
//        when(departmentSevice.findDepartmentById(anyInt())).thenReturn(createDepartmentModel());
//        when(staffRepository.findById(anyInt())).thenReturn(existEntity);
//        when(existEntity.isPresent()).thenReturn(false);
//
//        assertThrows(NoSuchElementException.class,
//                () -> new StaffService(staffRepository, departmentSevice).findById(1));
//    }
//
//    /**
//     * Delete a staff and delete successfully
//     */
//    @Test
//    public void when_deleteStaff_thenDeleteSuccessfully() {
//        StaffModel staffModel = createStaffModel();
//        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
//        when(staffRepository.findById(anyInt())).thenReturn(existEntity);
//        when(existEntity.isPresent()).thenReturn(true);
//        when(existEntity.get()).thenReturn(new StaffEntity(staffModel, new DepartmentEntity(createDepartmentModel())));
//        doNothing().when(staffRepository).deleteById(anyInt());
//        StaffModel actualStaff = new StaffService(staffRepository, departmentSevice).deleteStaffById(1);
//        staffModel.setDepartmentModel(createDepartmentModel());
//        assertTrue(compareTwoStaff(staffModel, actualStaff));
//
//    }
//
//    /**
//     * Delete not existed staff then throw Exception
//     */
//    @Test
//    public void when_deleteSDoesNotExistStaff_thenReturnNoSuchElementException() {
//        StaffModel staffModel = createStaffModel();
//        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
//        when(staffRepository.findById(anyInt())).thenReturn(existEntity);
//        when(existEntity.isPresent()).thenReturn(false);
//        assertThrows(NoSuchElementException.class,
//                () -> new StaffService(staffRepository, departmentSevice).deleteStaffById(1));
//
//    }
//
//    /**
//     * Find staff by search value then return pagination of staff model by Dsc
//     */
//    @Test
//    public void when_findStaffsSortByDsc_thenReturnResourceOfListOfStaffsByDsc() {
//        List<StaffEntity> entityList = new ArrayList<>();
//        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
//        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
//        when(entityPage.toList()).thenReturn(entityList);
//        when(entityPage.getTotalElements()).thenReturn(2l);
//        when(entityPage.getTotalPages()).thenReturn(2);
//        when(staffRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);
//
//        StaffResourceModel actualResource = new StaffService(staffRepository, departmentSevice).
//                findByLastnameOrFirstname(0, 1, "firstName", "a", "dsc");
//
//        List<StaffModel> modelList = new ArrayList<>();
//        StaffModel expectedModel = createStaffModel();
//        expectedModel.setDepartmentModel(createDepartmentModel());
//        modelList.add(expectedModel);
//        StaffResourceModel expectedResource = createStaffResource(2, 0,1, 2, modelList);
//        compareTwoResource(expectedResource, actualResource);
//    }
//
//    /**
//     * Find staff by search value then return pagination of staff model by Asc
//     */
//    @Test
//    public void when_findStaffsSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
//        List<StaffEntity> entityList = new ArrayList<>();
//        entityList.add(new StaffEntity(createStaffModel(), new DepartmentEntity(createDepartmentModel())));
//        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
//        when(entityPage.toList()).thenReturn(entityList);
//        when(entityPage.getTotalElements()).thenReturn(2l);
//        when(entityPage.getTotalPages()).thenReturn(2);
//        when(staffRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);
//
//        StaffResourceModel actualResource = new StaffService(staffRepository, departmentSevice).
//                findByLastnameOrFirstname(0, 1,
//                        "firstName", "a", "asc");
//
//        List<StaffModel> modelList = new ArrayList<>();
//        StaffModel expectModel = createStaffModel();
//        expectModel.setDepartmentModel(createDepartmentModel());
//        modelList.add(expectModel);
//        StaffResourceModel expectedResource = createStaffResource(2, 0,1, 2, modelList);
//        compareTwoResource(expectedResource, actualResource);
//    }
//}
