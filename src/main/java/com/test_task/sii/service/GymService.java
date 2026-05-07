package com.test_task.sii.service;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.repository.GymRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class GymService {
    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    @Transactional
    public GymDTO createNewGym(GymDTO requestGym) {
        if(gymRepository.existsByName(requestGym.getName())) {
            throw new IllegalArgumentException("Gym with this name already exists");
        }
        Gym gym = convertDTOtoEntity(requestGym);
        Gym savedGym = gymRepository.save(gym);
        return convertEntityToDTO(savedGym);
    }

    public List<GymDTO> getAllGyms() {
        List<Gym> gymList = gymRepository.findAll();
        List<GymDTO> gymDTOList = new ArrayList<>();
        for (Gym gym : gymList) {
            gymDTOList.add(convertEntityToDTO(gym));
        }
        return gymDTOList;
    }

    private Gym convertDTOtoEntity(GymDTO gymDTO){
        Gym gymEntity = new Gym();
        gymEntity.setName(gymDTO.getName());
        gymEntity.setAddress(gymDTO.getAddress());
        gymEntity.setPhoneNumber(gymDTO.getPhoneNumber());
        return gymEntity;
    }

    public GymDTO convertEntityToDTO(Gym gym){
        GymDTO gymDTO = new GymDTO();
        gymDTO.setId(gym.getId());
        gymDTO.setName(gym.getName());
        gymDTO.setAddress(gym.getAddress());
        gymDTO.setPhoneNumber(gym.getPhoneNumber());
        return gymDTO;
    }
}
