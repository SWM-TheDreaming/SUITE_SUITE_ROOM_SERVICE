package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuiteRoomServiceImpl implements SuiteRoomService{
    private final SuiteRoomRepository suiteRoomRepository;

    @Override
    public Optional<List<SuiteRoom>> getAllSuiteRooms() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> getSuiteRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<List<SuiteRoom>> getAllProgressRooms() {
        return Optional.empty();
    }

    @Override
    public Optional<List<SuiteRoom>> getAllCompletionRooms() {
        return Optional.empty();
    }

    @Override
    public SuiteRoom createSuiteRoom(SuiteRoom suiteRoom) {
        return suiteRoomRepository.save(suiteRoom);
    }

    @Override
    public Optional<SuiteRoom> joinRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> deleteRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<SuiteRoom> renewalRoom() {
        return Optional.empty();
    }

    @Override
    public Optional<?> commitPaymentStatus() {
        return Optional.empty();
    }
}
