package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;



@DataJpaTest
class ParticipantServiceImplTest {

    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;






}