package com.test_task.sii.service;

import com.test_task.sii.dto.GymDTO;
import com.test_task.sii.dto.ReportDTO;
import com.test_task.sii.entity.Gym;
import com.test_task.sii.repository.GymRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymServiceTest {
    @Mock
    private GymRepository gymRepository;

    @InjectMocks
    private GymService gymService;


    GymDTO createGymDTO() {
        GymDTO gymDTO = new GymDTO();
        gymDTO.setName("Lublin Shark");
        gymDTO.setAddress("Kiepury 14");
        gymDTO.setPhoneNumber("+48511222333");
        return gymDTO;
    }

    Gym createGym() {
        Gym gym = new Gym();
        gym.setName("Lublin Shark");
        gym.setAddress("Kiepury 14");
        gym.setPhoneNumber("+48511222333");
        return gym;
    }

    // test for success method 'createNewGym'
    @Test
    void addNewGym_ShouldGetDTOCheckIfExistsConvertToEntityAndSave() {
        // GIVEN
        GymDTO givenGymDTO = createGymDTO();

        Gym savedGym = createGym();
        savedGym.setId(1L);

        when(gymRepository.existsByName(givenGymDTO.getName())).thenReturn(false);
        when(gymRepository.save(any(Gym.class))).thenReturn(savedGym);

        // WHEN
        GymDTO gym = gymService.createNewGym(givenGymDTO);

        // THEN
        assertNotNull(gym);
        assertEquals("Lublin Shark", gym.getName());
        assertEquals(1L, gym.getId());
        verify(gymRepository, times(1)).existsByName(givenGymDTO.getName());
        verify(gymRepository, times(1)).save(any(Gym.class));

        ArgumentCaptor<Gym> gymCaptor = ArgumentCaptor.forClass(Gym.class);
        verify(gymRepository).save(gymCaptor.capture());

        // checking is DTO converted to Entity correctly
        Gym captureGym = gymCaptor.getValue();
        assertEquals(givenGymDTO.getName(), captureGym.getName());
        assertEquals(givenGymDTO.getAddress(), captureGym.getAddress());
        assertEquals(givenGymDTO.getPhoneNumber(), captureGym.getPhoneNumber());
    }

    // test for exception in method 'createNewGym'
    @Test
    void addNewGym_ShouldThrowExceptionIfGymAlreadyExists() {
        // GIVEN
        GymDTO givenGymDTO = createGymDTO();
        when(gymRepository.existsByName(givenGymDTO.getName())).thenReturn(true);
        // WHEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gymService.createNewGym(givenGymDTO));
        // THEN
        assertEquals("Gym with this name already exists", exception.getMessage());
        verify(gymRepository, times(1)).existsByName(givenGymDTO.getName());
        verify(gymRepository, never()).save(any(Gym.class));
    }

    @Test
    void getAllGyms_ShouldReturnListGyms() {
        // GIVEN
        Gym gym1 = createGym();
        gym1.setId(1L);
        Gym gym2 = createGym();
        gym2.setId(2L);
        gym2.setName("Lublin GYM");
        List<Gym> gymList = List.of(gym1, gym2);

        when(gymRepository.findAll()).thenReturn(gymList);

        // WHEN
        List<GymDTO> gettingGymDTOList = gymService.getAllGyms();
        // THEN
        assertNotNull(gettingGymDTOList);
        assertEquals(gymList.size(), gettingGymDTOList.size());
        assertEquals("Lublin Shark", gettingGymDTOList.get(0).getName());
        assertEquals(1L, gettingGymDTOList.get(0).getId());
        assertEquals("Lublin GYM", gettingGymDTOList.get(1).getName());
        assertEquals(2L, gettingGymDTOList.get(1).getId());
        verify(gymRepository, times(1)).findAll();
    }

    @Test
    void getTotalReport_ShouldReturnListOfReportDTO() {
        // GIVEN
        ReportDTO report1 = new ReportDTO("Lublin Gym", new BigDecimal("1500.00"), "USD");
        ReportDTO report2 = new ReportDTO("Lublin Shark", new BigDecimal("1000.00"), "EUR");
        List<ReportDTO> listReports = List.of(report1, report2);
        when(gymRepository.getTotalReport()).thenReturn(listReports);
        // WHEN
        List<ReportDTO> response = gymService.getTotalReport();
        // THEN
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Lublin Gym", response.get(0).getGymName());
        assertEquals(0, new BigDecimal("1500.00").compareTo(response.get(0).getAmount()));
        assertEquals("USD", response.get(0).getCurrency());
        assertEquals("Lublin Shark", response.get(1).getGymName());
        assertEquals(0, new BigDecimal("1000.00").compareTo(response.get(1).getAmount()));
        assertEquals("EUR", response.get(1).getCurrency());
        verify(gymRepository, times(1)).getTotalReport();
    }

    @Test
    void convertDTOtoEntity_ShouldReturnGym() {
        // GIVEN
        GymDTO gymDTO = createGymDTO();
        // WHEN
        Gym response = gymService.convertDTOtoEntity(gymDTO);
        // THEN
        assertNotNull(response);
        assertEquals(gymDTO.getName(), response.getName());
        assertEquals(gymDTO.getAddress(), response.getAddress());
        assertEquals(gymDTO.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    void convertEntityToDTO_ShouldReturnGymDTO() {
        // GIVEN
        Gym gym = createGym();
        gym.setId(1L);
        // WHEN
        GymDTO response = gymService.convertEntityToDTO(gym);
        // THEN
        assertNotNull(response);
        assertEquals(gym.getId(), response.getId());
        assertEquals(gym.getName(), response.getName());
        assertEquals(gym.getAddress(), response.getAddress());
        assertEquals(gym.getPhoneNumber(), response.getPhoneNumber());
    }
}
