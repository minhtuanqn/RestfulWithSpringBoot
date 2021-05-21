package com.controller;

import com.model.StaffModel;
import com.service.DefaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Controller layer
 */
@RestController
@Validated
@RequestMapping(path = "/staff")
public class DefaultController {
    private DefaultService defaultService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    public DefaultController(DefaultService defaultService) {
        this.defaultService = defaultService;
    }

    /**
     * Handle exception by hibernate validator
     * @param ex
     * @return  map contains errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;

    }

    /**
     * Handle exception for path variable
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public Map<String, String> handleValidationExceptions(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Iterator<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations().iterator();
        while(constraintViolations.hasNext()) {
            ConstraintViolation constraintViolation = constraintViolations.next();
            errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        }
        return errors;
    }

    /**
     * Create staff
     * @param staffModel
     * @return response entity contains saved staff
     */
    @PostMapping(path = "",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> saveStaff(@Valid @RequestBody StaffModel staffModel) {
        defaultService.createStaff(staffModel);
        return new ResponseEntity<StaffModel>(staffModel, HttpStatus.OK);
    }

    /**
     * Update staff by id
     * @param staffModel
     * @param id of updated staff
     * @return response entity contains updated staff
     */
    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updateStaff(@Valid @RequestBody StaffModel staffModel,
                                      @PathVariable @Positive Integer id) {
        staffModel.setId(id);
        boolean isUpdated = defaultService.updateStaff(staffModel);
        if(isUpdated) {
            return new ResponseEntity<StaffModel>(staffModel, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<StaffModel>(new StaffModel(), HttpStatus.NO_CONTENT);
        }
    }


    /**
     * Delete staff by id
     * @param id
     * @return status
     */
    @DeleteMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deleteStaff(@PathVariable @Positive Integer id) {
        boolean isDeleted = defaultService.deleteStaffById(id);
        if(isDeleted) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Find staff by id
     * @param id
     * @return Staff object
     */
    @GetMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<StaffModel> findById(@PathVariable @Positive Integer id) {
        StaffModel model = defaultService.findById(id);
        return new ResponseEntity<StaffModel>(model, HttpStatus.OK);
    }


    /**
     * Find all existed staff
     * @return List of staff
     */
    @GetMapping(path = "/all",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<StaffModel>> findAll() {
        List<StaffModel> modelList = defaultService.findAll();
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    /**
     * Find existed staff by first name
     * @param firstName
     * @return list of staff
     */
    @GetMapping(path = "/first-name",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<StaffModel>> findByFirstName(@RequestParam("firstName") String firstName) {
        List<StaffModel> modelList = defaultService.findByFirstName(firstName);
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    /**
     * Find staff by last name
     * @param lastName
     * @return list of staff
     */
    @GetMapping(path = "/last-name",
    consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<StaffModel>> findByLastName(@RequestParam("lastName") String lastName) {
        List<StaffModel> modelList = defaultService.findByLastName(lastName);
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

}
