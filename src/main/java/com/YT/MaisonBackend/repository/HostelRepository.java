package com.YT.MaisonBackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YT.MaisonBackend.entity.Hostel;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, UUID> {
}