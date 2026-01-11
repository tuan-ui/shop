package com.customer.constant;

public final class Constants {
    public interface roleStatus {
        Integer UNLOCK = 1; // Mo khoa
        Integer LOCK = 0; // Bi khoa
    }

    public interface isActive {
        Integer INACTIVE = 0; // Xoa
        Integer ACTIVE = 1; // Hoat dong
    }

    public interface isDeleted {
        Boolean ACTIVE = false; // Xoa
        Boolean DELETED = true; // Hoat dong
    }

    public interface errorResponse {
        String DATA_CHANGED = "error.DataChangedReload";
    }

    public static interface upload{
        public String IMAGE_DIRECTORY = "C:/u02";
    }
}
