ALTER TABLE `medic_facility`
ADD COLUMN `username` VARCHAR(255),
ADD COLUMN `created_at` TIMESTAMP,
ADD COLUMN `updated_at` TIMESTAMP,
ADD COLUMN `deleted_at` TIMESTAMP,
ADD COLUMN `is_active` BOOLEAN;