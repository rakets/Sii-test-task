package com.test_task.sii.dto;

import com.test_task.sii.entity.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MembershipPlanDTO {
    private Long id;

    @NotBlank(message = "Plan's name is required")
    private String name;

    @NotNull(message = "Plan's type is required")
    private PlanType type;

    @NotNull(message = "Plan's monthly price is required")
    private BigDecimal monthlyPrice;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Duration is required")
    private Integer durationMonths;

    @NotNull(message = "Members quantity is required")
    private Integer maxMembers;

    private GymDTO gymDTO;

    public MembershipPlanDTO() {
    }

    public GymDTO getGymDTO() {
        return gymDTO;
    }

    public void setGymDTO(GymDTO gymDTO) {
        this.gymDTO = gymDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanType getType() {
        return type;
    }

    public void setType(PlanType type) {
        this.type = type;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency.toUpperCase();
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
}
