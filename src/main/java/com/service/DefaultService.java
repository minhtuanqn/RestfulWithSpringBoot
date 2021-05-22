package com.service;

import com.entity.StaffEntity;
import com.model.StaffModel;
import com.model.StaffResource;
import com.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        StaffEntity savedEntity = repository.save(entity);
        model.setId(savedEntity.getId());
        model.setCreateAt(LocalDateTime.now());
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
            repository.save(staffEntity);
            model.setUpdatedAt(LocalDateTime.now());
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
    public boolean deleteStaffById(Integer id) {
        boolean isExist = checkExistStaff(id);
        if(isExist) {
            repository.deleteById(id);
            LOGGER.info("Deleted staff with id: " + id);
            return true;
        }
        LOGGER.info("Staff with id does not exist: " + id);
        return false;
    }

    /**
     * Finding a staff by id
     * @param id
     * @return staff object
     */
    public StaffModel findById(Integer id) {
        StaffEntity entity = repository.findById(id).get();
        return new StaffModel(entity);
    }

    /**
     * Finding all existed staff
     * @return list of staff
     */
    public StaffResource findAll() {
        List<StaffModel> modelList = new ArrayList<>();
        Iterator<StaffEntity> entityIterator = repository.findAll().iterator();
        while (entityIterator.hasNext()) {
            StaffEntity entity = entityIterator.next();
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        return resource;
    }

    /**
     * Service for finding staff by first name
     * @param firstName
     * @return list of staff
     */
    public StaffResource findByFirstName(String firstName) {
        List<StaffModel> modelList = new ArrayList<>();
        List<StaffEntity> entityList = repository.findByFirstName(firstName);
        for (StaffEntity entity : entityList) {
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        return resource;
    }

    /**
     * Service for finding staff by last name
     * @param lastName
     * @return list of staff
     */
    public StaffResource findByLastName(String lastName) {
        List<StaffModel> modelList = new ArrayList<>();
        List<StaffEntity> entityList = repository.findByLastName(lastName);
        for (StaffEntity entity: entityList) {
            modelList.add(new StaffModel(entity));
        }
        StaffResource resource = new StaffResource();
        resource.setData(modelList);
        return resource;
    }

}
