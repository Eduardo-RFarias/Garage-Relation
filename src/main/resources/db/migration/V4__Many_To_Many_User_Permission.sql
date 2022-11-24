CREATE TABLE user_permission
(
    user_id         BIGINT      NOT NULL,
    permission_name VARCHAR(50) NOT NULL,

    CONSTRAINT USER_PERMISSION_PK PRIMARY KEY (user_id, permission_name),
    CONSTRAINT USER_PERMISSION_USER_FK FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT USER_PERMISSION_PERMISSION_FK FOREIGN KEY (permission_name) REFERENCES permission (name)
) ENGINE = InnoDB;