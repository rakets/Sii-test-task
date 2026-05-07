package com.test_task.sii.dto;

import com.test_task.sii.entity.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class MemberDTO {
    private Long id;

    @NotBlank(message = "Member's fullname is required")
    private String fullname;

    @NotBlank(message = "Member's email is required")
    @Email(message = "Email should be valid")
    private String email;

    private LocalDate startDate;
    private MemberStatus status;
    private MembershipPlanDTO membershipPlanDTO;

    public MemberDTO() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public MembershipPlanDTO getMembershipPlanDTO() {
        return membershipPlanDTO;
    }

    public void setMembershipPlanDTO(MembershipPlanDTO membershipPlanDTO) {
        this.membershipPlanDTO = membershipPlanDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
