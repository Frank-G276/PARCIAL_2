package Parcial.pw.Guilombo.Service;


import Parcial.pw.Guilombo.Persistence.Entity.UserRoleEntity;
import Parcial.pw.Guilombo.Persistence.Repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRoleEntity> getAll(){
        return userRoleRepository.findAll();
    }

    public UserRoleEntity save(UserRoleEntity userRole){
        return userRoleRepository.save(userRole);
    }
}
