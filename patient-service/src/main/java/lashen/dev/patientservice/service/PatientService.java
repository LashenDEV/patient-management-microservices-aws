package lashen.dev.patientservice.service;

import lashen.dev.patientservice.dto.PatientRequestDTO;
import lashen.dev.patientservice.dto.PatientResponseDTO;
import lashen.dev.patientservice.exception.EmailAlreadyExistsException;
import lashen.dev.patientservice.mapper.PatientMapper;
import lashen.dev.patientservice.model.Patient;
import lashen.dev.patientservice.repository.PatientRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients () {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient (PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists " +
                    patientRequestDTO.getEmail());
        }

        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));

        //an email address must be unique
        return PatientMapper.toDTO(newPatient);
    }
}
