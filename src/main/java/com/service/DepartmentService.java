package com.service;

import com.customexception.DuplicatedEntityByUniqueIdentityException;
import com.customexception.NoSuchEntityByIdException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.model.DepartmentModel;
import com.model.PaginationModel;
import com.model.StaffModel;
import com.model.StaffResourceModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static com.utils.PaginationUtils.buildPagination;
import static com.utils.PaginationUtils.covertToPageable;


@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final StaffRepository staffRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentService.class);

    public DepartmentService(DepartmentRepository departmentRepository, StaffRepository staffRepository) {
        this.departmentRepository = departmentRepository;
        this.staffRepository = staffRepository;
    }

    /**
     * create a Department
     *
     * @param model
     * @return created Model
     */
    public DepartmentModel createDepartment(DepartmentModel model) {
        //Check existed name of department
        if (departmentRepository.existsDepartmentEntitiesByName(model.getName())) {
            throw new DuplicatedEntityByUniqueIdentityException("Name of department have been existed");
        }

        //Set id for model is null
        if (model.getId() != null) {
            model.setId(null);
        }
        //Prepare entity for saving to DB
        DepartmentEntity entity = new DepartmentEntity(model);
        entity.setCreateAt(LocalDateTime.now());

        //Save entity to DB
        DepartmentEntity savedEntity = departmentRepository.save(entity);
        model = new DepartmentModel(savedEntity);
        LOGGER.info("Created a department with id: " + model.getId());
        return model;
    }

    /**
     * delete a department
     *
     * @param id
     * @return deleted model
     */
    @Transactional
    public DepartmentModel deleteDepartment(Integer id) {
        //Find department with id
        Optional<DepartmentEntity> deletedDepartmentOptional = departmentRepository.findDepartmentEntityByIdAndDeleteAtNull(id);
        DepartmentEntity deletedDepartmentEntity = deletedDepartmentOptional.orElseThrow();

        //Set deleted date for entity
        deletedDepartmentEntity.setDeleteAt(LocalDateTime.now());

        //update status of department
        departmentRepository.save(deletedDepartmentEntity);
        LOGGER.info("Deleted department with id: " + id);
        return new DepartmentModel(deletedDepartmentEntity);

    }

    /**
     * Find a department by id
     *
     * @param id
     * @return searched model
     */
    public DepartmentModel findDepartmentById(Integer id) {
        //Find department with id
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentEntityByIdAndDeleteAtNull(id);
        return new DepartmentModel(searchedDepartmentOptional.orElseThrow());
    }


    /**
     * Update a information of department
     *
     * @param departmentId
     * @param model
     * @return model of updated department
     */
    public DepartmentModel updateDepartment(Integer departmentId, DepartmentModel model) {
        //Find department with id
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentEntityByIdAndDeleteAtNull(departmentId);
        DepartmentEntity searchedDepartmentEntity = searchedDepartmentOptional.orElseThrow();

        //Check existed department with name of updated model
        if (departmentRepository.existsDepartmentEntitiesByNameAndIdNot(model.getName(), departmentId)) {
            throw new DuplicatedEntityByUniqueIdentityException("Name of department have been existed");
        }

        //Prepare entity for saving to DB
        model.setId(departmentId);
        model.setCreateAt(searchedDepartmentEntity.getCreateAt());
        model.setUpdateAt(LocalDateTime.now());

        //Save entity to DB
        DepartmentEntity savedEntity = departmentRepository.save(new DepartmentEntity(model));
        LOGGER.info("Updated information of department with id: " + departmentId);
        return new DepartmentModel(savedEntity);

    }

    /**
     * find staffs by department id
     *
     * @param id
     * @param pagination
     * @return pagination model of staff model
     */
    public StaffResourceModel findAllStaffByDepartmentId(Integer id, PaginationModel pagination) {

        if (!departmentRepository.existsDepartmentEntitiesByIdAndDeleteAtNull(id)) {
            throw new NoSuchEntityByIdException("ID of department does not exist");
        }

        String defaultSortBy = "firstName";
        Pageable pageable = covertToPageable(pagination, defaultSortBy);

        //Find all staff belong to a department with id
        Page<StaffEntity> staffEntityPage = staffRepository.findStaffEntitiesByDepartmentEntityIdEquals(id, pageable);

        //Convert model list to entity list
        List<StaffModel> staffModelList = new ArrayList<>();
        for (StaffEntity entity : staffEntityPage) {
            staffModelList.add(new StaffModel(entity));
        }
        //Prepare resource for return
        StaffResourceModel resource = new StaffResourceModel();
        resource.setData(staffModelList);
        buildPagination(pagination, staffEntityPage, resource);
        return resource;
    }


}
