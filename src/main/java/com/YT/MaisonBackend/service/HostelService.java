package com.YT.MaisonBackend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.YT.MaisonBackend.dto.HostelCreateRequest;
import com.YT.MaisonBackend.dto.HostelResponse;
import com.YT.MaisonBackend.entity.Hostel;
import com.YT.MaisonBackend.entity.Student;
import com.YT.MaisonBackend.exception.ConflictException;
import com.YT.MaisonBackend.exception.NotFoundException;
import com.YT.MaisonBackend.mapper.HostelMapper;
import com.YT.MaisonBackend.repository.HostelRepository;
import com.YT.MaisonBackend.repository.StudentRepository;

@Service
@Transactional
public class HostelService {
	private final HostelRepository hostelRepository;
	private final StudentRepository studentRepository;

	public HostelService(HostelRepository hostelRepository, StudentRepository studentRepository) {
		this.hostelRepository = hostelRepository;
		this.studentRepository = studentRepository;
	}

	public HostelResponse registerHostel(HostelCreateRequest request) {
		return HostelMapper.toResponse(hostelRepository.save(HostelMapper.toEntity(request)));
	}

	public void addStudentToHostel(UUID hostelId, UUID studentId) {
		Hostel hostel = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new NotFoundException("Hostel not found"));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException("Student not found"));

		if (student.getHostel() != null && !student.getHostel().getId().equals(hostelId)) {
			throw new ConflictException("Student is already assigned to another hostel");
		}

		if (student.getHostel() == null) {
			hostel.addStudent(student);
			studentRepository.save(student);
		}
	}

	public void removeStudentFromHostel(UUID hostelId, UUID studentId) {
		Hostel hostel = hostelRepository.findById(hostelId)
				.orElseThrow(() -> new NotFoundException("Hostel not found"));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException("Student not found"));

		if (student.getHostel() == null || !student.getHostel().getId().equals(hostelId)) {
			throw new ConflictException("Student is not assigned to this hostel");
		}

		hostel.removeStudent(student);
		studentRepository.save(student);
	}
}