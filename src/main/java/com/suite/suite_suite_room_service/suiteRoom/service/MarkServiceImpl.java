package com.suite.suite_suite_room_service.suiteRoom.service;


import com.suite.suite_suite_room_service.suiteRoom.entity.Mark;
import com.suite.suite_suite_room_service.suiteRoom.repository.MarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;

    @Override
    @Transactional
    public void mark(long memberId, long suiteRoomId) {
        markRepository.findByMemberIdAndSuiteRoomId(memberId, suiteRoomId).ifPresentOrElse(
                mark -> {
                    markRepository.deleteByMemberIdAndSuiteRoomId(memberId, suiteRoomId);
                },
                () -> {
                    markRepository.save(Mark.builder()
                            .memberId(memberId)
                            .suiteRoomId(suiteRoomId).build());
                }
        );
    }

    public void getMarkOfSuiteRoom(long memberId) {
        List<Mark> marks =  markRepository.findByMemberId(memberId);



    }
}
