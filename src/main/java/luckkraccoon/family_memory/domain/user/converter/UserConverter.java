package luckkraccoon.family_memory.domain.user.converter;

import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
import luckkraccoon.family_memory.domain.user.dto.response.SignupResponse;
import luckkraccoon.family_memory.domain.user.entity.User;

public class UserConverter {

    public static SignupResponse toSignupResponse(User user) {
        return SignupResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .build();
    }

    public static void applySignupFields(User user, SignupRequest req, String encodedPassword, String imageUrl) {
        user.setUserId(req.getUserId());
        user.setPassword(encodedPassword);
        user.setUserName(req.getUserName());
        user.setNickName(req.getNickName());
        user.setEmail(req.getEmail());
        user.setGender(req.getGender());
        user.setBirth(req.getBirth());
        user.setUserImage(imageUrl); // null 가능
        user.setFontSize(req.getFontSize());
        user.setVoiceSpeed(req.getVoiceSpeed());
    }
}
