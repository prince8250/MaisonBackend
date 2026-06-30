package com.YT.MaisonBackend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.YT.MaisonBackend.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
	boolean existsByHostel_IdAndRoomNumber(UUID hostelId, String roomNumber);
}