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
import java.util.Iterator;
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

    private boolean checkExistStaff(Integer id) {
        LOGGER.info("Check existed staff with id: " + id);
        return repository.existsById(id);
    }


    /**
     * Creating a new staff
     * @param model was saved
     */
    public void createStaff(StaffModel model) {
        StaffEntity entity = new StaffEntity(model);
        if(entity.getId() != null) {
            entity.setId(null);
        }
        entity.setCreateAt(LocalDateTime.now());
        StaffEntity savedEntity = repository.save(entity);
        model.setId(savedEntity.getId());
        model.setCreateAt(savedEntity.getCreateAt());
        LOGGER.info("Created staff with id: " + model.getId());
    }

    /**
     * Updating a existed staff
     * @param model
     * @return updated or did not
     */
    public boolean updateStaff(StaffModel model) {
        LOGGER.info("Update staff with id: " + model.getId());
        StaffEntity staffEntity = new StaffEntity(model);
        boolean isExist = checkExistStaff(model.getId());
        if (isExist) {
            LocalDateTime createAt = repository.findById(model.getId()).get().getCreateAt();
            staffEntity.setUpdateAt(LocalDateTime.now());
            staffEntity.setCreateAt(createAt);
            repository.save(staffEntity);
            model.setCreateAt(createAt);
            model.setUpdatedAt(staffEntity.getUpdateAt());
            LOGGER.info("Updated staff with id " + model.getId());
            return true;
        }
        LOGGER.info("Staff with id does not exist: " + model.getId());
        return false;
    }

    /**
     * Deleting a existed staff
     * @param id
     */
    public StaffModel deleteStaffById(Integer id) {
        boolean isExist = checkExistStaff(id);
        if(isExist) {
            StaffEntity existEntity = repository.findById(id).get();
            repository.deleteById(id);
            LOGGER.info("Deleted staff with id: " + id);
            return new StaffModel(existEntity);
        }
        LOGGER.info("Staff with id does not exist: " + id);
        return null;
    }

    /**
     * Finding a staff by id
     * @param id
     * @return staff object
     */
    public StaffModel findById(Integer id) {
        Optional<StaffEntity> staffOptional = repository.findById(id);
        if(staffOptional.isPresent()) {
            StaffEntity entity = staffOptional.get();
            return new StaffModel(entity);
        }
        return null;
    }

    /**
     * Finding all existed staff
     * @param page
     * @param perPage
     * @param sortBy
     * @return list of staff
     */
    public StaffResource findAll(Integer page, Integer perPage, String sortBy) {
        List<StaffModel> modelList = new ArrayList<>();
        String typedSort = "first_name";
        if(typedSort != null) {
            typedSort = sortBy;
        }
        Iterator<StaffEntity> entityIterator = repository.getAll(PageRequest.of(page, perPage, Sort.by(typedSort))).iterator();
        while (entityIterator.hasNext()) {
            StaffEntity entity = entityIterator.next();
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        resource.setPage(page);
        resource.setPerPage(perPage);
        return resource;
    }

    private Specification<StaffEntity> containFirstname(String searchedName) {
        return(root, query, criteriaBuilder) -> {
            String pattern = "%" + searchedName + "%";
            return criteriaBuilder.like(root.get(StaffEntity_.FIRSTNAME), pattern);
        };
    }

    private Specification<StaffEntity> containLastname(String searchedName) {
        return(root, query, criteriaBuilder) -> {
            String pattern = "%" + searchedName + "%";
            return criteriaBuilder.like(root.get(StaffEntity_.LASTNAME), pattern);
        };
    }

    /**
     * Search staff list
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
        if(sortType.equals("dsc")) {
             pageable = PageRequest.of(page, perPage, Sort.by(sortBy).descending());
        }
        else {
            pageable = PageRequest.of(page, perPage, Sort.by(sortBy).ascending());
        }

        Page<StaffEntity> entityList = repository.findAll(containFirstname(searchedName)
                                        .or(containLastname(searchedName)), pageable);
        for (StaffEntity entity: entityList) {
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        resource.setTotalPage(entityList.getTotalPages());
        resource.setTotal((int) entityList.getTotalElements());
        resource.setPage(page);
        resource.setPerPage(perPage);
        return resource;
    }


}
