package com.example.shopapp.constant;

public class MessageKeys {
    public static final String LOGIN_SUCCESSFULLY = "user.login.login_successfully";
    public static final String LOGIN_FAILED = "user.login.login_failed";
    public static final String WRONG_PHONE_PASSWORD =  "user.login.wrong_phone_password";
    public static final String PASSWORD_NOT_CORRECT = "user.login.password_not_correct";
    public static final String ROLE_DOES_NOT_EXISTS = "user.login.role_not_exist";
    public static final String USER_IS_LOCKED = "user.login.user_is_locked";

    public static final String REGISTER_SUCCESSFULLY = "user.register.register_successfully";
    public static final String PASSWORD_NOT_MATCH = "user.register.password_not_match";
    public static final String PHONE_EXISTS = "user.register.phone_exists";
    public static final String PERMISSION_DENY = "user.register.permission_deny";

    public static final String WRONG_CATEGORY_ID = "category.get_category_by_id.wrong_id";
    public static final String INSERT_CATEGORY_SUCCESSFULLY = "category.create_category.create_successfully";
    public static final String INSERT_CATEGORY_FAILED = "category.create_category.create_failed";
    public static final String UPDATE_CATEGORY_SUCCESSFULLY = "category.update_category.update_successfully";
    public static final String DELETE_CATEGORY_SUCCESSFULLY = "category.delete_category.delete_successfully";

    public static final String WRONG_ORDER_ID = "order.get_order_by_id.wrong_id";
    public static final String DELETE_ORDER_SUCCESSFULLY = "order.delete_order.delete_successfully";
    public static final String WRONG_ORDER_DETAIL_ID = "order_detail.get_order_detail_by_id.wrong_id";
    public static final String DELETE_ORDER_DETAIL_SUCCESSFULLY = "order_detail.delete_order_detail.delete_successfully";

    public static final String UPLOAD_IMAGES_MAX = "product.upload_images.error_max_images";
    public static final String UPLOAD_IMAGES_FILE_LARGE = "product.upload_images.file_large";
    public static final String UPLOAD_IMAGES_FILE_MUST_BE_IMAGE = "product.upload_images.file_must_be_image";
}
