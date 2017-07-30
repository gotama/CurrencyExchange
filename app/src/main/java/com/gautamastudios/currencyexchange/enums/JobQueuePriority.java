package com.gautamastudios.currencyexchange.enums;

public enum JobQueuePriority {
    LOW(1),
    MEDIUM(500),
    HIGH(1000);

    private final int mId;

    JobQueuePriority(int id) {
        this.mId = id;
    }

    public int getId() {
        return mId;
    }
}
