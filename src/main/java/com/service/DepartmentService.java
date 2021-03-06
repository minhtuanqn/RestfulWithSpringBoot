package com.service;

import com.convertor.PaginationConvertor;
import com.customexception.DuplicatedEntityException;
import com.customexception.NoSuchEntityException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.metamodel.DepartmentEntity_;
import com.metamodel.StaffEntity_;
import com.model.DepartmentModel;
import com.model.PaginationModel;
import com.model.ResourceModel;
import com.model.StaffModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


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
            throw new DuplicatedEntityException("Name of department have been existed");
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
        Optional<DepartmentEntity> deletedDepartmentOptional = departmentRepository.findDepartmentById(id);
        DepartmentEntity deletedDepartmentEntity = deletedDepartmentOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found department"));

        //Set deleted date for entity
        deletedDepartmentEntity.setDeleteAt(LocalDateTime.now());

        //update status of department
        DepartmentEntity responseEntity = departmentRepository.save(deletedDepartmentEntity);
        LOGGER.info("Deleted department with id: " + id);
        return new DepartmentModel(responseEntity);

    }

    /**
     * Find a department by id
     *
     * @param id
     * @return searched model
     */
    public DepartmentModel findDepartmentById(Integer id) {
        //Find department with id
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentById(id);
        DepartmentEntity departmentEntity = searchedDepartmentOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found department"));
        return new DepartmentModel(departmentEntity);
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
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentById(departmentId);
        DepartmentEntity searchedDepartmentEntity = searchedDepartmentOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found department"));

        //Check existed department with name of updated model
        if (departmentRepository.existsDepartmentEntitiesByNameAndIdNot(model.getName(), departmentId)) {
            throw new DuplicatedEntityException("Name of department have been existed");
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
    public ResourceModel<StaffModel> findAllStaffByDepartmentId(Integer id, PaginationModel pagination) {

        PaginationConvertor<StaffModel, StaffEntity> paginationConvertor = new PaginationConvertor<>();

        if (!departmentRepository.existsDepartmentEntitiesByIdAndDeleteAtNull(id)) {
            throw new NoSuchEntityException("Not found department");
        }

        String defaultSortBy = "firstName";
        Pageable pageable = paginationConvertor.covertToPageable(pagination, defaultSortBy, StaffEntity.class);

        //Find all staff belong to a department with id
        Page<StaffEntity> staffEntityPage = staffRepository.findAll(containsNullDeleteAtOfDep().and(equalsDepId(id)), pageable);

        //Convert model list to entity list
        List<StaffModel> staffModelList = new ArrayList<>();
        for (StaffEntity entity : staffEntityPage) {
            staffModelList.add(new StaffModel(entity));
        }
        //Prepare resource for return
        ResourceModel<StaffModel> resource = new ResourceModel<>();
        resource.setData(staffModelList);
        paginationConvertor.buildPagination(pagination, staffEntityPage, resource);
        return resource;
    }

    private Specification<DepartmentEntity> containName(String searchedName) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + searchedName + "%";
            return criteriaBuilder.like(root.get(DepartmentEntity_.NAME), pattern);
        };
    }

    private Specification<DepartmentEntity> containsNullDeleteAt() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get(DepartmentEntity_.DELETE_AT));
        };
    }

    private Specification<StaffEntity> equalsDepId(Integer depId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(StaffEntity_.DEP_ID), depId);
        };
    }

    private Specification<StaffEntity> containsNullDeleteAtOfDep() {
        return (root, query, criteriaBuilder) -> {
            Join<StaffEntity, DepartmentEntity> staffJoins = root.join(StaffEntity_.DEP_ID);
            Predicate equalPredicate = criteriaBuilder.isNull(staffJoins.get(DepartmentEntity_.DELETE_AT));
            query.distinct(true);
            return equalPredicate;
        };
    }

    /**
     * find department like name
     *
     * @param searchedValue
     * @param pagination
     * @return
     */
    public ResourceModel<DepartmentModel> findDepartmentLikeName(String searchedValue, PaginationModel pagination) {
        PaginationConvertor<DepartmentModel, DepartmentEntity> paginationConvertor = new PaginationConvertor<>();

        //Prepare pageable for query
        String defaultSortBy = "name";
        Pageable pageable = paginationConvertor.covertToPageable(pagination, defaultSortBy, DepartmentEntity.class);

        //Query from DB
        Page<DepartmentEntity> departmentsPage = departmentRepository.findAll(containName(searchedValue).and(containsNullDeleteAt()), pageable);

        //Convert to list of department model
        List<DepartmentModel> modelList = new ArrayList<>();
        for (DepartmentEntity entity : departmentsPage) {
            modelList.add(new DepartmentModel(entity));
        }

        //Prepare resource for return
        ResourceModel<DepartmentModel> resource = new ResourceModel<>();
        resource.setData(modelList);
        paginationConvertor.buildPagination(pagination, departmentsPage, resource);
        return resource;
    }


}
