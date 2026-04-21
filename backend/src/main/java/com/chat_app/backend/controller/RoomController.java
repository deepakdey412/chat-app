package com.chat_app.backend.controller;

import com.chat_app.backend.entities.Room;
import com.chat_app.backend.entities.Messages;
import com.chat_app.backend.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // ✅ Create room
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody Room room) {

        Room existingRoom = roomRepository.findByRoomId(room.getRoomId());

        if (existingRoom != null) {
            return ResponseEntity
                    .badRequest()
                    .body("Room already exists with id: " + room.getRoomId());
        }

        Room newRoom = new Room();
        newRoom.setRoomId(room.getRoomId());

        Room savedRoom = roomRepository.save(newRoom);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    // ✅ Get room by id
    @GetMapping("/{id}")
    public ResponseEntity<?> joinRoom(@PathVariable String id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        return ResponseEntity.ok(room);
    }

    // ✅ Get messages with pagination
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {

        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found");
        }

        List<Messages> messages = room.getMessages();

        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        }

        // ✅ Pagination logic
        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        List<Messages> paginatedMessages = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessages);
    }
}