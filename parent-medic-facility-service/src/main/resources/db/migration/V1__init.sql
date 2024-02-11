DROP TABLE IF EXISTS `parent_medic_facility`;
CREATE TABLE IF NOT EXISTS `parent_medic_facility`(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fk_parent_id INT,
    fk_medic_id INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
) ENGINE = INNODB;

CREATE INDEX `index_parent_medic_facility` ON `parent_medic_facility`(fk_parent_id, fk_medic_id);