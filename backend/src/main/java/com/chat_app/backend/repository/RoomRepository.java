package com.chat_app.backend.repository;

import com.chat_app.backend.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository  extends MongoRepository<Room,String> {
    //Get room using room id
    Room findByRoomId(String roomId);
}
