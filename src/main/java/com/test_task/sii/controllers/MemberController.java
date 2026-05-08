package com.test_task.sii.controllers;

import com.test_task.sii.dto.MemberDTO;
import com.test_task.sii.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{gymId}/{planId}/new")
    public ResponseEntity<MemberDTO> addNewMember(@Valid @RequestBody MemberDTO request,
                                                  @PathVariable("gymId") Long gymId,
                                                  @PathVariable("planId") Long planId) {
        MemberDTO newMember = memberService.saveNewMember(request, gymId, planId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMember);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MemberDTO>> getAll() {
        List<MemberDTO> allMembers = memberService.getAllMembers();
        return ResponseEntity.status(HttpStatus.OK).body(allMembers);
    }

    @PatchMapping("/cancel-membership/{memberId}")
    public ResponseEntity<MemberDTO> cancelMembership(@PathVariable("memberId") Long memberId) {
        MemberDTO response = memberService.cancelMembership(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
