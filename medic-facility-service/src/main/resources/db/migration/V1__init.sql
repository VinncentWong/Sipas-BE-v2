DROP TABLE IF EXISTS `medic_facility`;
CREATE TABLE IF NOT EXISTS `medic_facility`(
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    password VARCHAR(255),
    unique_code VARCHAR(255),
    telephone_number VARCHAR(255),
    whatsapp_number VARCHAR(255),
    address TEXT,
    PRIMARY KEY(id, unique_code)
) ENGINE = INNODB;
CREATE INDEX medic_facility_index ON `medic_facility`(email);