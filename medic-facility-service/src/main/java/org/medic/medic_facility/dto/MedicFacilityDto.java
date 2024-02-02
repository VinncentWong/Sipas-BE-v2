package org.medic.medic_facility.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.medic.medic_facility.entity.MedicFacility;
import org.example.util.BcryptUtil;

public class MedicFacilityDto {

    public record Create(
            @NotNull(message = "email can't be null")
            @NotBlank(message = "email can't be blank")
            @Email
            String email,

            @NotNull(message = "password can't be null")
            @NotBlank(message = "password can't be blank")
            String password,

            @NotNull(message = "username can't be null")
            @NotBlank(message = "username can't be blank")
            String username,

            @NotNull(message = "address can't be null")
            @NotBlank(message = "address can't be blank")
            String address,

            @NotNull(message = "telephone number can't be null")
            @NotBlank(message = "telephone number can't be blank")
            String telephoneNumber,

            @NotNull(message = "whatsapp number can't be null")
            @NotBlank(message = "whatsapp number can't be blank")
            String whatsappNumber
    ){
        public MedicFacility toMedicFacility(){
            return MedicFacility
                    .builder()
                    .email(this.email)
                    .password(BcryptUtil.encode(this.password))
                    .username(this.username)
                    .address(this.address)
                    .telephoneNumber(this.telephoneNumber)
                    .whatsappNumber(this.whatsappNumber)
                    .uniqueCode(RandomStringUtils.randomAlphanumeric(8))
                    .isActive(true)
                    .build();
        }
    }

    public record Login(
            String email,
            String password
    ){
        public MedicFacility toMedicFacility(){
            return MedicFacility
                    .builder()
                    .email(this.email)
                    .password(this.password)
                    .build();
        }
    }
}
