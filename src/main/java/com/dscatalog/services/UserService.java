package com.dscatalog.services;

import com.dscatalog.dto.CategoryDTO;
import com.dscatalog.dto.RoleDTO;
import com.dscatalog.dto.UserDTO;
import com.dscatalog.entities.Category;
import com.dscatalog.entities.Role;
import com.dscatalog.entities.User;
import com.dscatalog.repositories.CategoryRepository;
import com.dscatalog.repositories.RoleRepository;
import com.dscatalog.repositories.UserRepository;
import com.dscatalog.services.exceptions.DatabaseException;
import com.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable){
        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));
    }
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO insert(UserDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity){
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.getRoles().clear();
        for(RoleDTO roleDto : dto.getRoles()){
            Role role = roleRepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);
        }
    }

}