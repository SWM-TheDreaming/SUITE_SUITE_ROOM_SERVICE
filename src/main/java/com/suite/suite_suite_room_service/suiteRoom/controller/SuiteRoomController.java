package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.security.AuthorizationJwtCreator;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class SuiteRoomController {

    private final SuiteRoomService suiteRoomService;
    private final AuthorizationJwtCreator authorizationJwtCreator;

    @Value("${jwt.access.key}")
    private String accessKey;

    @GetMapping("/suiteroom")
    public ResponseEntity<Message> listUpSuiteRooms(@RequestHeader("Authorization") String authorizationHeader, AuthorizerDto authorizerDto) {
        // 토큰 재발급 부분 여기서 처리
        String token = authorizationHeader.substring("Bearer ".length());
        Token renewalToken = renewalTokenValidator(token, authorizerDto);

        List<ResSuiteRoomDto> getAllSuiteRooms = suiteRoomService.getAllSuiteRooms(getSuiteAuthorizer());

        Message.MessageAppender message = new Message.MessageAppender();
        return ResponseEntity.ok(message.messageAppenderCaller("token", renewalToken,StatusCode.OK,getAllSuiteRooms));
    }
    @GetMapping("/suiteroom/detail/{suiteRoomId}")
    public ResponseEntity<Message> listUpSuiteRoom(@PathVariable Long suiteRoomId, AuthorizerDto authorizerDto) {
        ResSuiteRoomDto getSuiteRoom = suiteRoomService.getSuiteRoom(suiteRoomId, authorizerDto);
        return ResponseEntity.ok(new Message(StatusCode.OK, getSuiteRoom));
    }
    @GetMapping("/progression")
    public ResponseEntity<Message> listUpProgressionRooms() {
        return null;
    }
    @GetMapping("/completion")
    public ResponseEntity<Message> listUpCompletionRooms() {
        return null;
    }
    @PostMapping("/suiteroom/registration")
    public ResponseEntity<Message> createRoom(@RequestBody ReqSuiteRoomDto reqSuiteRoomDto) {
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PostMapping("/suiteroom/attendance")
    public ResponseEntity<Message> attendanceRoom() {
        return null;
    }
    @DeleteMapping("/suiteroom/delete/{suiteRoomId}")
    public ResponseEntity<Message> deleteRoom(@PathVariable Long suiteRoomId) {
        suiteRoomService.deleteSuiteRoom(suiteRoomId, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PatchMapping("/suiteroom/update")
    public ResponseEntity<Message> updateRoom(@RequestBody ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto) {
        suiteRoomService.updateSuiteRoom(reqUpdateSuiteRoomDto, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    private Token renewalTokenValidator(String token, AuthorizerDto authorizerDto) {
        Claims claims = Jwts.parser().setSigningKey(accessKey.getBytes()).parseClaimsJws(token).getBody();
        Token renewalToken = null;

        if (claims.getExpiration() == null || claims.getExpiration().getTime() < (new Date()).getTime() + 86400000) {
            renewalToken = authorizationJwtCreator.createToken(authorizerDto);
        }
        return renewalToken;
    }




}
