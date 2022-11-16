CREATE TABLE car
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    model VARCHAR(50)           NOT NULL,
    brand VARCHAR(50)           NOT NULL,
    year  INT                   NOT NULL,

    CONSTRAINT CAR_PK PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1;