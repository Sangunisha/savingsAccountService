package com.inteview.banking.savingsAccountService.base.service;

import com.inteview.banking.savingsAccountService.base.domain.BaseDomainObject;
import com.inteview.banking.savingsAccountService.base.dto.BaseDTO;
import com.inteview.banking.savingsAccountService.base.exceptions.SavingsAppException;
import com.inteview.banking.savingsAccountService.constants.StatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class AbstractService<T extends BaseDomainObject, U extends BaseDTO> {

    public abstract CrudRepository<T, String> getRepository();

    public abstract U getDTO(T entity, U dto) throws SavingsAppException;

    public abstract T populateEntity(U dto, T entity) throws SavingsAppException;

    public abstract T updateModifiedParams(U dto, T existingEntity) throws SavingsAppException;

    public abstract U getNewDTO() throws SavingsAppException;

    public abstract T getNewEntity() throws SavingsAppException;

    public U get(String id) throws SavingsAppException {
        return getDTO(getEntity(id), getNewDTO());
    }

    public T getEntity(String id) throws SavingsAppException {
        if (id == null)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.parameter_missing, "id is null");
        Optional<T> entity = getRepository().findById(id);
        if (!entity.isPresent())
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.object_does_not_exist, "Cannot find object with id:" + id);
        return entity.get();
    }

    public U create(U dto) throws SavingsAppException {
        if (dto == null)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.parameter_missing, "dto is null");
        T entity = populateEntity(dto, getNewEntity());
        return getDTO(createEntity(entity), getNewDTO());
    }

    public T setBaseDomainObjectParamsForCreateEntity(T entity) throws SavingsAppException {
        entity.setId(null);
        entity.setCreatedTs(System.currentTimeMillis());
        entity.setModifiedTs(System.currentTimeMillis());
        entity.setStatus(StatusEnum.ACTIVE);
        return entity;
    }

    public T createEntity(T entity) throws SavingsAppException {
        entity = setBaseDomainObjectParamsForCreateEntity(entity);
        return getRepository().save(entity);
    }


    public U update(U dto) throws SavingsAppException {
        if (dto == null)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.parameter_missing, "dto is null");
        String id = dto.getId();
        T existingEntity = getEntity(id);

        if (existingEntity.getStatus() == StatusEnum.INACTIVE)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_state, "Attempting to update an inactive object. id:" + id);
        existingEntity = updateModifiedParams(dto, existingEntity);
        return getDTO(updateEntity(existingEntity), getNewDTO());
    }

    public T updateEntity(T entity) throws SavingsAppException {
        entity.setModifiedTs(System.currentTimeMillis());
        return getRepository().save(entity);
    }

    public Boolean disable(String id) throws SavingsAppException {
        T existingEntity = getEntity(id);
        disableEntity(existingEntity);
        return true;
    }

    public T disableEntity(T entity) throws SavingsAppException {
        if (entity.getStatus() == StatusEnum.INACTIVE)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_state, "Attempting to disable an inactive object. id:" + entity.getId());
        entity.setModifiedTs(System.currentTimeMillis());
        entity.setStatus(StatusEnum.INACTIVE);
        return getRepository().save(entity);
    }

    public Boolean enable(String id) throws SavingsAppException {
        T existingEntity = getEntity(id);
        enableEntity(existingEntity);
        return true;
    }

    public T enableEntity(T entity) throws SavingsAppException {
        if (entity.getStatus() == StatusEnum.ACTIVE)
            throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.invalid_state, "Attempting to enable an active object. id:" + entity.getId());

        entity.setModifiedTs(System.currentTimeMillis());
        entity.setStatus(StatusEnum.ACTIVE);
        return getRepository().save(entity);
    }

    public Boolean delete(String id) throws SavingsAppException {
        T existingEntity = getEntity(id);
        getRepository().delete(existingEntity);
        return true;
    }

    public U copyDTO(T t, U u) throws SavingsAppException {
        if (t != null && u != null) {
            u.setCreatedTs(t.getCreatedTs());
            u.setId(t.getId());
            u.setModifiedTs(t.getModifiedTs());
            u.setVersion(t.getVersion());
            u.setStatus(t.getStatus());
            return u;
        }
        throw new SavingsAppException(SavingsAppException.AppExceptionErrorCode.parameter_missing, "entity=" + t + " dto=" + u);
    }
}
