CREATE TABLE user
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    username  VARCHAR(50)           NOT NULL,
    password  VARCHAR(100)          NOT NULL,
    email     VARCHAR(100)          NOT NULL,
    full_name VARCHAR(100)          NULL,

    CONSTRAINT USER_PK PRIMARY KEY (id),
    CONSTRAINT USER_USERNAME_UK UNIQUE (username),
    CONSTRAINT USER_EMAIL_UK UNIQUE (email)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1;