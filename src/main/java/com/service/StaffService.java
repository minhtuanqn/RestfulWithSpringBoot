package com.service;

import com.convertor.PaginationConvertor;
import com.customexception.DuplicatedEntityException;
import com.customexception.NoSuchEntityException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer
 */
@Service
public class StaffService {

    private final StaffRepository repository;

    private final DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffService.class);

    public StaffService(StaffRepository repository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create a new staff
     *
     * @param model
     * @return response staff model
     */
    public StaffModel createStaff(StaffModel model) {
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentById(model.getDepId());
        DepartmentEntity searchedDepartmentEntity = searchedDepartmentOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found department"));

        //Check exist entity by username
        if (repository.existsStaffEntitiesByUsername(model.getUsername())) {
            throw new DuplicatedEntityException("Username of created staff is duplicated");
        }

        //Set id for saved staff is null and date time of creation
        StaffEntity entity = new StaffEntity(model, searchedDepartmentEntity);
        if (entity.getId() != null) {
            entity.setId(null);
        }
        entity.setCreateAt(LocalDateTime.now());
        entity.setPassword(passwordEncoder.encode(model.getPassword()));

        //Save entity to DB
        StaffEntity savedEntity = repository.save(entity);

        //Prepare response model
        model.setId(savedEntity.getId());
        model.setCreateAt(savedEntity.getCreateAt());
        model.setDepartmentModel(new DepartmentModel(searchedDepartmentEntity));
        LOGGER.info("Created staff with id: " + model.getId());
        return model;

    }


    /**
     * Update a new staff
     *
     * @param staffModel
     * @return model of updated staff
     */
    public StaffModel updateStaff(StaffModel staffModel) {

        //Find existed department by id
        Optional<DepartmentEntity> searchedDepOptional = departmentRepository.findDepartmentById(staffModel.getDepId());
        DepartmentEntity searchedDepEntity = searchedDepOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found updated department"));

        //Find exist staff by id in DB
        Optional<StaffEntity> existStaffOptional = repository.findStaffById(staffModel.getId());
        StaffEntity existStaffEntity = existStaffOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found staff"));

        //Check exist username
        if (repository.existsStaffEntityByIdNotAndUsernameEquals(staffModel.getId(), staffModel.getUsername())) {
            throw new DuplicatedEntityException("Username of staff is duplicated");
        }

        //Prepare for saved entity
        StaffEntity staffEntity = new StaffEntity(staffModel, searchedDepEntity);
        LocalDateTime createAt = existStaffEntity.getCreateAt();
        staffEntity.setUpdateAt(LocalDateTime.now());
        staffEntity.setCreateAt(createAt);
        staffEntity.setPassword(passwordEncoder.encode(staffModel.getPassword()));

        //Save entity to DB
        Set<StaffEntity> staffEntityList = searchedDepEntity.getStaffList();
        staffEntityList.add(staffEntity);
        searchedDepEntity.setStaffList(staffEntityList);
        departmentRepository.save(searchedDepEntity);
        repository.save(staffEntity);
        LOGGER.info("Updated staff with id " + staffModel.getId());
        return new StaffModel(staffEntity, new DepartmentModel(searchedDepEntity));
    }


    /**
     * Update information for existed staff
     *
     * @param staffId
     * @return model of deleted staff
     */
    public StaffModel deleteStaffById(Integer staffId) {

        //Find exist staff in DB
        Optional<StaffEntity> existEntityOptional = repository.findStaffById(staffId);
        StaffEntity existEntity = existEntityOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found staff"));

        //Prepare data for saving
        existEntity.setDeleteAt(LocalDateTime.now());

        //Delete staff in DB
        repository.save(existEntity);
        LOGGER.info("Deleted staff with id: " + staffId);
        return new StaffModel(existEntity, new DepartmentModel(existEntity.getDepartmentEntity()));
    }

    /**
     * Find staff by id
     *
     * @param id
     * @return searched staff
     */
    public StaffModel findById(Integer id) {
        //Find exist staff in DB
        Optional<StaffEntity> searchedStaffOptional = repository.findStaffById(id);
        StaffEntity searchedStaffEntity = searchedStaffOptional
                .orElseThrow(() -> new NoSuchEntityException("Not found staff"));

        return new StaffModel(searchedStaffEntity, new DepartmentModel(searchedStaffEntity.getDepartmentEntity()));
    }


    private Specification<StaffEntity> containFirstname(String searchedName) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + searchedName + "%";
            return criteriaBuilder.like(root.get(StaffEntity_.FIRSTNAME), pattern);
        };
    }

    private Specification<StaffEntity> containLastname(String searchedName) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + searchedName + "%";
            return criteriaBuilder.like(root.get(StaffEntity_.LASTNAME), pattern);
        };
    }

    private Specification<StaffEntity> containsNullDeleteAt() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNull(root.get(StaffEntity_.DELETE_AT));
        };
    }


    /**
     * Search staff list by first name or last name
     *
     * @param pagination
     * @param searchedName
     * @return information of staff resource
     */
    public ResourceModel<StaffModel> findByLastnameOrFirstname(PaginationModel pagination, String searchedName) {

        PaginationConvertor<StaffModel, StaffEntity> paginationConvertor = new PaginationConvertor<>();

        //Create pageable for pagination
        String defaultSortBy = "firstName";
        Pageable pageable = paginationConvertor.covertToPageable(pagination, defaultSortBy, StaffEntity.class);

        //Find data in DB
        Page<StaffEntity> entityPage = repository.findAll(containFirstname(searchedName).and(containsNullDeleteAt())
                .or(containLastname(searchedName).and(containsNullDeleteAt())), pageable);

        List<StaffModel> modelList = new ArrayList<>();
        List<StaffEntity> entityList = entityPage.toList();
        for (StaffEntity entity : entityList) {
            modelList.add(new StaffModel(entity, new DepartmentModel(entity.getDepartmentEntity())));
        }

        //Prepare data for resource
        ResourceModel<StaffModel> resource = new ResourceModel<>();
        resource.setData(modelList);
        paginationConvertor.buildPagination(pagination, entityPage, resource);
        return resource;
    }


}
