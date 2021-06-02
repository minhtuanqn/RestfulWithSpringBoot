package com.service;

import com.entity.StaffEntity;
import com.model.StaffModel;
import com.model.StaffResource;
import com.repository.DefaultRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static com.utils.TestsUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class DefaultServiceTests {
    DefaultRepository defaultRepository = Mockito.mock(DefaultRepository.class);

    @Test
    public void when_saveStaff_thenSaveSuccessfully(){
        StaffModel staffModel = createStaffModel();
        staffModel.setUpdatedAt(null);
        when(defaultRepository.save(any())).thenReturn(new StaffEntity(staffModel));

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(null);
        paramModel.setUpdatedAt(null);
        StaffModel actualModel = new DefaultService(defaultRepository).createStaff(paramModel);
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    @Test
    public void when_updateStaff_thenUpdateSuccessfully(){
        StaffModel staffModel = createStaffModel();
        StaffEntity existEntity = new StaffEntity(staffModel);
        existEntity.setCreateAt(createMockTime());

        Optional<StaffEntity> existOptional = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existOptional);
        when(existOptional.isPresent()).thenReturn(true);
        when(existOptional.get()).thenReturn(existEntity);

        StaffEntity savedEntity = new StaffEntity(staffModel);
        savedEntity.setUpdateAt(LocalDateTime.now());
        when(defaultRepository.save(any())).thenReturn(savedEntity);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdatedAt(null);
        StaffModel actualModel = new DefaultService(defaultRepository).updateStaff(paramModel);
        staffModel.setUpdatedAt(actualModel.getUpdatedAt());
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    @Test
    public void when_updateDoesNotExistStaff_thenReturnNull(){
        StaffModel staffModel = createStaffModel();
        StaffEntity existEntity = new StaffEntity(staffModel);
        existEntity.setCreateAt(createMockTime());

        Optional<StaffEntity> existOptional = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existOptional);
        when(existOptional.isPresent()).thenReturn(false);

        StaffModel paramModel = createStaffModel();
        paramModel.setCreateAt(createMockTime());
        paramModel.setUpdatedAt(null);
        StaffModel actualModel = new DefaultService(defaultRepository).updateStaff(paramModel);
        assertNull(actualModel);
    }

    @Test
    public void when_findStaffById_thenReturnStaff(){
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existEntity);
        when(existEntity.isPresent()).thenReturn(true);
        when(existEntity.get()).thenReturn(new StaffEntity(staffModel));

        StaffModel actualModel = new DefaultService(defaultRepository).findById(1);
        assertTrue(compareTwoStaff(staffModel, actualModel));
    }

    @Test
    public void when_findDoesNotExistStaffById_thenReturnNull(){
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existEntity);
        when(existEntity.isPresent()).thenReturn(false);

        StaffModel actualModel = new DefaultService(defaultRepository).findById(1);
        assertNull(actualModel);
    }

    @Test
    public void when_deleteStaff_thenDeleteSuccessfully() {
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existEntity);
        when(existEntity.isPresent()).thenReturn(true);
        when(existEntity.get()).thenReturn(new StaffEntity(staffModel));
        doNothing().when(defaultRepository).deleteById(anyInt());
        StaffModel actualStaff = new DefaultService(defaultRepository).deleteStaffById(1);
        assertTrue(compareTwoStaff(staffModel, actualStaff));

    }

    @Test
    public void when_deleteSDoesNotExistStaff_thenReturnNull() {
        StaffModel staffModel = createStaffModel();
        Optional<StaffEntity> existEntity = Mockito.mock(Optional.class);
        when(defaultRepository.findById(anyInt())).thenReturn(existEntity);
        when(existEntity.isPresent()).thenReturn(false);
        StaffModel actualStaff = new DefaultService(defaultRepository).deleteStaffById(1);
        Assertions.assertNull(actualStaff);

    }

    @Test
    public void when_findStaffsSortByDsc_thenReturnResourceOfListOfStaffsByDsc() {
        List<StaffEntity> entityList = new ArrayList<>();
        entityList.add(new StaffEntity(createStaffModel()));
        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
        when(entityPage.toList()).thenReturn(entityList);
        when(entityPage.getTotalElements()).thenReturn(2l);
        when(entityPage.getTotalPages()).thenReturn(2);
        when(defaultRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        StaffResource actualResource = new DefaultService(defaultRepository).
                findByLastnameOrFirstname(0, 1, "firstName", "a", "dsc");

        List<StaffModel> modelList = new ArrayList<>();
        modelList.add(createStaffModel());
        StaffResource expectedResource = createStaffResource(2, 0,1, 2, modelList);
        compareTwoResource(expectedResource, actualResource);
    }

    @Test
    public void when_findStaffsSortByAsc_thenReturnResourceOfListOfStaffsByAsc() {
        List<StaffEntity> entityList = new ArrayList<>();
        entityList.add(new StaffEntity(createStaffModel()));
        Page<StaffEntity> entityPage = Mockito.mock(Page.class);
        when(entityPage.toList()).thenReturn(entityList);
        when(entityPage.getTotalElements()).thenReturn(2l);
        when(entityPage.getTotalPages()).thenReturn(2);
        when(defaultRepository.findAll(any(), (Pageable) any())).thenReturn(entityPage);

        StaffResource actualResource = new DefaultService(defaultRepository).
                findByLastnameOrFirstname(0, 1,
                        "firstName", "a", "asc");

        List<StaffModel> modelList = new ArrayList<>();
        modelList.add(createStaffModel());
        StaffResource expectedResource = createStaffResource(2, 0,1, 2, modelList);
        compareTwoResource(expectedResource, actualResource);
    }
}
