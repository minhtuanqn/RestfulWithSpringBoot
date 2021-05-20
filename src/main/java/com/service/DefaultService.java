package com.service;

import com.entity.StaffEntity;
import com.model.StaffModel;
import com.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
     * Service for create a new staff
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
     * Service for update a existed staff
     * @param model
     * @return updated or not
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

//    /**
//     * Service for delete a existed staff
//     * @param id
//     */
//    public void deleteStaffById(Integer id) {
//        repository.deleteById(id);
//    }
//
//    /**
//     * Service for finding a staff by id
//     * @param id
//     * @return
//     */
//    public StaffModel findById(Integer id) {
//        StaffEntity entity = repository.findById(id).get();
//        return new StaffModel(entity);
//    }
//
//    /**
//     * Service for finding all exiested staff
//     * @return
//     */
//    public List<StaffModel> findAll() {
//        List<StaffModel> modelList = new ArrayList<>();
//        Iterator<StaffEntity> entityIterator = repository.findAll().iterator();
//        while (entityIterator.hasNext()) {
//            StaffEntity entity = entityIterator.next();
//            modelList.add(new StaffModel(entity));
//        }
//        return modelList;
//    }
//
//    /**
//     * Service for finding staff by first name
//     * @param firstName
//     * @return
//     */
//    public List<StaffModel> findByFirstName(String firstName) {
//        List<StaffModel> modelList = new ArrayList<>();
//        List<StaffEntity> entityList = repository.findByFirstName(firstName);
//        for (StaffEntity entity : entityList) {
//            modelList.add(new StaffModel(entity));
//        }
//        return modelList;
//    }

}
