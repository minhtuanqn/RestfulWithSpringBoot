package com.service;

import com.entity.StaffEntity;
import com.metamodel.StaffEntity_;
import com.model.StaffModel;
import com.model.StaffResource;
import com.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer
 */
@Service
public class DefaultService {
    private final DefaultRepository repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultService.class);

    public DefaultService(DefaultRepository repository) {
        this.repository = repository;
    }


    /**
     * Creating a new staff
     *
     * @param model was saved
     */
    public StaffModel createStaff(StaffModel model) {
        StaffEntity entity = new StaffEntity(model);
        if (entity.getId() != null) {
            entity.setId(null);
        }
        entity.setCreateAt(LocalDateTime.now());
        StaffEntity savedEntity = repository.save(entity);
        model.setId(savedEntity.getId());
        model.setCreateAt(savedEntity.getCreateAt());
        LOGGER.info("Created staff with id: " + model.getId());
        return model;
    }

    /**
     * Updating a existed staff
     *
     * @param model
     * @return updated or did not
     */
    public StaffModel updateStaff(StaffModel model) {
        LOGGER.info("Update staff with id: " + model.getId());
        StaffEntity staffEntity = new StaffEntity(model);
        Optional<StaffEntity> existStaff = repository.findById(model.getId());
        if (existStaff.isPresent()) {
            LocalDateTime createAt = existStaff.get().getCreateAt();
            staffEntity.setUpdateAt(LocalDateTime.now());
            staffEntity.setCreateAt(createAt);
            StaffEntity savedEntity = repository.save(staffEntity);
            LOGGER.info("Updated staff with id " + model.getId());
            return new StaffModel(savedEntity);
        }
        LOGGER.info("Staff with id does not exist: " + model.getId());
        return null;
    }

    /**
     * Deleting a existed staff
     *
     * @param id
     */
    public StaffModel deleteStaffById(Integer id) {
        Optional<StaffEntity> existEntity = repository.findById(id);
        if (existEntity.isPresent()) {
            repository.deleteById(id);
            LOGGER.info("Deleted staff with id: " + id);
            return new StaffModel(existEntity.get());
        }
        LOGGER.info("Staff with id does not exist: " + id);
        return null;
    }

    /**
     * Finding a staff by id
     *
     * @param id
     * @return staff object
     */
    public StaffModel findById(Integer id) {
        Optional<StaffEntity> staffOptional = repository.findById(id);
        if (staffOptional.isPresent()) {
            StaffEntity entity = staffOptional.get();
            return new StaffModel(entity);
        }
        return null;
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
     * Search staff list
     *
     * @param page
     * @param perPage
     * @param sortBy
     * @param searchedName
     * @param sortType
     * @return information of staff resource
     */
    public StaffResource findByLastnameOrFirstname(Integer page, Integer perPage,
                                                   String sortBy, String searchedName, String sortType) {
        List<StaffModel> modelList = new ArrayList<>();
        Pageable pageable;
        if (sortType.equals("dsc")) {
            pageable = PageRequest.of(page, perPage, Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(page, perPage, Sort.by(sortBy).ascending());
        }

        Page<StaffEntity> entityPage = repository.findAll(containFirstname(searchedName)
                .or(containLastname(searchedName)), pageable);
        List<StaffEntity> entityList = entityPage.toList();
        for (StaffEntity entity : entityList) {
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        resource.setTotalPage(entityPage.getTotalPages());
        resource.setTotal((int) entityPage.getTotalElements());
        resource.setPage(page);
        resource.setPerPage(perPage);
        return resource;
    }


}
