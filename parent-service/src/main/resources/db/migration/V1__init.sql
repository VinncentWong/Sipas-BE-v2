DROP TABLE IF EXISTS `parent`;
CREATE TABLE IF NOT EXISTS `parent`(
    id INT NOT NULL PRIMARY KEY,
    father_name VARCHAR(255),
    mother_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    is_connected_with_faskes BOOLEAN
) ENGINE = INNODB;
CREATE INDEX parent_index ON `parent`(email);

--DROP TABLE IF EXISTS `medic_facility`;
--CREATE TABLE IF NOT EXISTS `medic_facility`(
--    id INT NOT,
--    email VARCHAR(255),
--    password VARCHAR(255),
--    unique_code VARCHAR(255),
--    telephone_number VARCHAR(255),
--    address VARCHAR(255),
--    PRIMARY KEY(id, unique_code)
--) ENGINE = INNODB;
--CREATE INDEX parent_index ON `parent`(email);
--
--DROP TABLE IF EXISTS `submit_support`;
--CREATE TABLE IF NOT EXISTS `submit_support`(
--    id INT NOT PRIMARY KEY,
--    title VARCHAR(255),
--    description TEXT,
--    status VARCHAR(255),
--    additional_message TEXT,
--    fk_parent_id INT
--) ENGINE = INNODB;
--CREATE INDEX submit_support_index ON `submit_support`(fk_parent_id);