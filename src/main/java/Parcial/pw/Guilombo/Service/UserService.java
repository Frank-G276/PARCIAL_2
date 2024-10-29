package Parcial.pw.Guilombo.Service;

import Parcial.pw.Guilombo.Persistence.Entity.UserEntity;
import Parcial.pw.Guilombo.Persistence.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAll(){
        return userRepository.findAll();
    }

    public UserEntity save(UserEntity user){
        return this.userRepository.save(user);
    }
    public void deleteById(int user_id){
        this.userRepository.deleteById(user_id);
    }

    public Optional<UserEntity> findById(int id){
        return userRepository.findById(id);
    }

}