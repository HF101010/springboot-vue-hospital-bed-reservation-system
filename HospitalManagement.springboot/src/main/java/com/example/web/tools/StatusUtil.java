package com.example.web.tools;

public final class StatusUtil {

    private StatusUtil() {
    }

    public static final String BED_AVAILABLE = "1";
    public static final String BED_OCCUPIED = "2";
    public static final String BED_MAINTENANCE = "3";

    public static final String RESERVATION_PENDING = "1";
    public static final String RESERVATION_APPROVED = "2";
    public static final String RESERVATION_REJECTED = "3";
    public static final String RESERVATION_CANCELLED = "4";

    public static final String ADMISSION_IN_HOSPITAL = "in_hospital";
    public static final String ADMISSION_DISCHARGED = "discharged";

    public static String normalizeBedStatus(String status) {
        String value = safe(status);
        switch (value) {
            case BED_AVAILABLE:
            case "可用":
            case "空闲":
                return BED_AVAILABLE;
            case BED_OCCUPIED:
            case "已占用":
            case "已预约":
            case "已入住":
                return BED_OCCUPIED;
            case BED_MAINTENANCE:
            case "维修中":
                return BED_MAINTENANCE;
            default:
                return BED_AVAILABLE;
        }
    }

    public static String bedStatusLabel(String status) {
        switch (normalizeBedStatus(status)) {
            case BED_AVAILABLE:
                return "可用";
            case BED_OCCUPIED:
                return "已占用";
            case BED_MAINTENANCE:
                return "维修中";
            default:
                return "未知";
        }
    }

    public static String normalizeReservationStatus(String status) {
        String value = safe(status);
        switch (value) {
            case RESERVATION_PENDING:
            case "待审核":
                return RESERVATION_PENDING;
            case RESERVATION_APPROVED:
            case "已通过":
                return RESERVATION_APPROVED;
            case RESERVATION_REJECTED:
            case "已拒绝":
                return RESERVATION_REJECTED;
            case RESERVATION_CANCELLED:
            case "已取消":
                return RESERVATION_CANCELLED;
            default:
                return RESERVATION_PENDING;
        }
    }

    public static boolean isActiveReservation(String status) {
        String value = normalizeReservationStatus(status);
        return RESERVATION_PENDING.equals(value) || RESERVATION_APPROVED.equals(value);
    }

    public static String normalizeAdmissionStatus(String status) {
        String value = safe(status);
        switch (value) {
            case ADMISSION_IN_HOSPITAL:
            case "住院中":
                return ADMISSION_IN_HOSPITAL;
            case ADMISSION_DISCHARGED:
            case "已出院":
                return ADMISSION_DISCHARGED;
            default:
                return ADMISSION_IN_HOSPITAL;
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
