package Parcial.pw.Guilombo.Service;

import Parcial.pw.Guilombo.Persistence.Entity.RoleEntity;
import Parcial.pw.Guilombo.Persistence.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleEntity> getAll(){
        return this.roleRepository.findAll();
    }
    public RoleEntity save(RoleEntity role){
        return this.roleRepository.save(role);
    }
    public RoleEntity findByName(String rolename){
        return roleRepository.findByRolename(rolename);
    }

}
