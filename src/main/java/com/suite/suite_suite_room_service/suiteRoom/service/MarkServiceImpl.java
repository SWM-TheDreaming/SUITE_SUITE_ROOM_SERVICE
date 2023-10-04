package com.suite.suite_suite_room_service.suiteRoom.service;


import com.suite.suite_suite_room_service.suiteRoom.entity.Mark;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.MarkRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final SuiteRoomRepository suiteRoomRepository;

    @Override
    @Transactional
    public void mark(long memberId, long suiteRoomId) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).get();

        markRepository.findByMemberIdAndSuiteRoom_SuiteRoomId(memberId, suiteRoomId).ifPresentOrElse(
                mark -> {
                    markRepository.deleteByMemberIdAndSuiteRoom_SuiteRoomId(memberId, suiteRoomId);
                },
                () -> {
                    Mark mark = Mark.builder()
                            .memberId(memberId)
                            .suiteRoom(suiteRoom).build();
                    suiteRoom.addMark(mark);
                    markRepository.save(mark);
                }
        );
    }

    public void getMarkOfSuiteRoom(long memberId) {
        List<Mark> marks =  markRepository.findByMemberId(memberId);



    }
}
