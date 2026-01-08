package com.cms.constant;

import com.cms.utils.AppConfig;

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

    public interface messageResponse {
        String SUCCESS = "Thành công";
        String ERROR = "Lỗi hệ thống: ";
        String NO_TOKEN = "Không có token hoặc phiên đăng nhập hợp lệ";
        String NO_USER_INFO = "Thông tin người dùng không hợp lệ";
    }

    public interface errorResponse {
        String DATA_CHANGED = "error.DataChangedReload";
    }

    public static interface upload{
        public String IMAGE_DIRECTORY = "C:/u02";
    }
}
