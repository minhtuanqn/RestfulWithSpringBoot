package com.service;

import com.customexception.DuplicatedEntityByUniqueIdentityException;
import com.entity.DepartmentEntity;
import com.entity.StaffEntity;
import com.metamodel.DepartmentEntity_;
import com.metamodel.StaffEntity_;
import com.model.DepartmentModel;
import com.model.StaffModel;
import com.model.StaffResourceModel;
import com.repository.DepartmentRepository;
import com.repository.StaffRepository;
import com.resolver.anotation.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.utils.PaginationUtils.buildPagination;
import static com.utils.PaginationUtils.covertToPageable;

/**
 * Service layer
 */
@Service
public class StaffService {

    private final StaffRepository repository;

    private final DepartmentRepository departmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaffService.class);

    public StaffService(StaffRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Create a new staff
     *
     * @param model
     * @return response staff model
     */
    public StaffModel createStaff(StaffModel model) {
        Optional<DepartmentEntity> searchedDepartmentOptional = departmentRepository.findDepartmentEntityByIdAndDeleteAtNull(model.getDepId());
        DepartmentEntity searchedDepartmentEntity = searchedDepartmentOptional.orElseThrow();

        //Check exist entity by username
        if (repository.existsStaffEntitiesByUsername(model.getUsername())) {
            throw new DuplicatedEntityByUniqueIdentityException("Username of created staff is duplicated");
        }

        //Set id for saved staff is null and date time of creation
        StaffEntity entity = new StaffEntity(model, searchedDepartmentEntity);
        if (entity.getId() != null) {
            entity.setId(null);
        }
        entity.setCreateAt(LocalDateTime.now());

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

        Optional<DepartmentEntity> searchedDepOptional = departmentRepository.findDepartmentEntityByIdAndDeleteAtNull(staffModel.getDepId());
        DepartmentEntity searchedDepEntity = searchedDepOptional.orElseThrow();

        if (repository.existsStaffEntityByIdNotAndUsernameEquals(staffModel.getId(), staffModel.getUsername())) {
            throw new DuplicatedEntityByUniqueIdentityException("Username of staff is duplicated");
        }

        //Find exist staff by id in DB
        Optional<StaffEntity> existStaffOptional = repository.findStaffEntityByIdAndDeleteAtNull(staffModel.getId());
        StaffEntity existStaffEntity = existStaffOptional.orElseThrow();

        //Prepare for saved entity
        StaffEntity staffEntity = new StaffEntity(staffModel, searchedDepEntity);
        LocalDateTime createAt = existStaffEntity.getCreateAt();
        staffEntity.setUpdateAt(LocalDateTime.now());
        staffEntity.setCreateAt(createAt);

        //Save entity to DB
        StaffEntity savedEntity = repository.save(staffEntity);
        LOGGER.info("Updated staff with id " + staffModel.getId());
        return new StaffModel(savedEntity, new DepartmentModel(searchedDepEntity));
    }


    /**
     * Update information for existed staff
     *
     * @param staffId
     * @return model of deleted staff
     */
    public StaffModel deleteStaffById(Integer staffId) {

        //Find exist staff in DB
        Optional<StaffEntity> existEntityOptional = repository.findStaffEntityByIdAndDeleteAtNull(staffId);
        StaffEntity existEntity = existEntityOptional.orElseThrow();

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
        Optional<StaffEntity> searchedStaffOptional = repository.findStaffEntityByIdAndDeleteAtNull(id);
        StaffEntity searchedStaffEntity = searchedStaffOptional.orElseThrow();
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


    /**
     * Search staff list by first name or last name
     *
     * @param pagination
     * @param searchedName
     * @return information of staff resource
     */
    public StaffResourceModel findByLastnameOrFirstname(Pagination pagination, String searchedName) {
        //Create pageable for pagination
        List<StaffModel> modelList = new ArrayList<>();
        Pageable pageable = covertToPageable(pagination);

        //Find data in DB
        Page<StaffEntity> entityPage = repository.findAll(containFirstname(searchedName)
                .or(containLastname(searchedName)), pageable);
        List<StaffEntity> entityList = entityPage.toList();
        for (StaffEntity entity : entityList) {
            modelList.add(new StaffModel(entity, new DepartmentModel(entity.getDepartmentEntity())));
        }

        //Prepare data for resource
        StaffResourceModel resource = new StaffResourceModel();
        resource.setData(modelList);
        buildPagination(pagination, entityPage, resource);
        return resource;
    }


}
