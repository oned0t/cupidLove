package com.ictech.cupidlove.enums;

public enum Puchase {
    PAID_CHAT,
    PAID_LOCATION,
    PAID_SUPERLIKE,
    PAID_AD;

    public String getPurchase() {
        switch (this) {
            case PAID_CHAT:
                return "PAID_CHAT";
            case PAID_LOCATION:
                return "PAID_LOCATION";
            case PAID_SUPERLIKE:
                return "PAID_SUPERLIKE";
            case PAID_AD:
                return "PAID_AD";
            default:
                return "";
        }
    }


}
