package com.ciro.backend.dto;

public class DrillDownDetailDTO {
    private Long id;
    private String primaryText;
    private String secondaryText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public DrillDownDetailDTO(Long id, String primaryText, String secondaryText) {
        this.id = id;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
    }

    public DrillDownDetailDTO() {
        super();
    }
}