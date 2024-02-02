package org.parent_service.parent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.parent_service.parent.entity.Parent;
import org.example.util.BcryptUtil;

public class ParentDto {

    public record Create(

            @NotNull(message = "mother name can't null")
            @NotBlank(message = "mother name can't blank")
            String motherName,

            @NotNull(message = "telephone number can't null")
            @NotBlank(message = "telephone number can't blank")
            String telephoneNumber,

            @NotNull(message = "email can't null")
            @NotBlank(message = "email can't blank")
            String email,

            @NotNull(message = "password can't null")
            @NotBlank(message = "password can't blank")
            String password
    ){
      public Parent toParent(){
          return Parent
                  .builder()
                  .motherName(this.motherName)
                  .email(this.email)
                  .password(BcryptUtil.encode(this.password))
                  .build();
      }
    }

    public record Login(
            String email,

            String password
    ){
        public Parent toParent(){
            return Parent
                    .builder()
                    .email(this.email)
                    .password(this.password)
                    .build();
        }
    }
}
