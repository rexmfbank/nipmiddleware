package com.globalaccelerex.nipmiddleware.payload.client;

import com.globalaccelerex.nipmiddleware.payload.outward.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateClientPasswordRequest extends BaseRequest {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "new password is required")
    @Size(max=50 ,message = "Password Length can't be more than 50 characters")
    private String newPassword;

    @NotBlank(message = "confirm password is required")
    private String confirmPassword;

    public boolean isNewPasswordAndConfirmPasswordEqual(){
        return StringUtils.equals(newPassword, confirmPassword);
    }
}
