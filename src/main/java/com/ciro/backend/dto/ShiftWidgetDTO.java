package com.ciro.backend.dto;

public class ShiftWidgetDTO {
    private long weeklyCount;
    private long todayCount;
    private ShiftResponseDTO nextShift;

    public ShiftWidgetDTO(long weeklyCount, long todayCount, ShiftResponseDTO nextShift) {
        this.weeklyCount = weeklyCount;
        this.todayCount = todayCount;
        this.nextShift = nextShift;
    }

    public long getWeeklyCount() { return weeklyCount; }
    public void setWeeklyCount(long weeklyCount) { this.weeklyCount = weeklyCount; }
    public long getTodayCount() { return todayCount; }
    public void setTodayCount(long todayCount) { this.todayCount = todayCount; }
    public ShiftResponseDTO getNextShift() { return nextShift; }
    public void setNextShift(ShiftResponseDTO nextShift) { this.nextShift = nextShift; }
}