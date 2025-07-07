package com.grepp.spring.app.controller.api;


import com.grepp.spring.infra.util.ReferencedException;
import com.grepp.spring.infra.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

//    private final MemberService memberService;
//
//    public AuthController(final MemberService memberService) {
//        this.memberService = memberService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<MemberDTO>> getAllMembers() {
//        return ResponseEntity.ok(memberService.findAll());
//    }
//
//    @GetMapping("/{memberId}")
//    public ResponseEntity<MemberDTO> getMember(
//            @PathVariable(name = "memberId") final Integer memberId) {
//        return ResponseEntity.ok(memberService.get(memberId));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Integer> createMember(@RequestBody @Valid final MemberDTO memberDTO) {
//        final Integer createdMemberId = memberService.create(memberDTO);
//        return new ResponseEntity<>(createdMemberId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{memberId}")
//    public ResponseEntity<Integer> updateMember(
//            @PathVariable(name = "memberId") final Integer memberId,
//            @RequestBody @Valid final MemberDTO memberDTO) {
//        memberService.update(memberId, memberDTO);
//        return ResponseEntity.ok(memberId);
//    }
//
//    @DeleteMapping("/{memberId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteMember(
//            @PathVariable(name = "memberId") final Integer memberId) {
//        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(memberId);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        memberService.delete(memberId);
//        return ResponseEntity.noContent().build();
//    }

}
