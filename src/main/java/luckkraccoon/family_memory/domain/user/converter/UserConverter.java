package luckkraccoon.family_memory.domain.user.converter;

import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
import luckkraccoon.family_memory.domain.user.dto.response.LoginResponse;
import luckkraccoon.family_memory.domain.user.dto.response.SignupResponse;
import luckkraccoon.family_memory.domain.user.dto.response.UserUpdateResponse;
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

    /** 멀티파트 전용: URL은 서비스에서 S3 업로드 후 주입 */
    public static User toSignupEntity(SignupRequest req, String encodedPassword, String imageUrl) {
        return User.builder()
                .userId(req.getUserId())
                .userPassword(encodedPassword)    // ✅ 엔티티 필드명 일치
                .userName(req.getUserName())
                .nickName(req.getNickName())
                .email(req.getEmail())
                .gender(req.getGender())
                .birth(req.getBirth())
                .userImage(imageUrl)              // S3 업로드 URL
                .fontSize(req.getFontSize())
                .voiceSpeed(req.getVoiceSpeed())
                .build(); // familyGroup은 가입 시 null 유지(이후 참여 API에서 세팅)
    }

    public static LoginResponse toLoginResponse(User u) {
        return LoginResponse.builder()
                .id(u.getId())
                .userId(u.getUserId())
                .nickName(u.getNickName())
                .email(u.getEmail())
                .gender(u.getGender() == null ? null : u.getGender().name())
                .birth(u.getBirth() == null ? null : u.getBirth().toString())
                .userImage(u.getUserImage())
                .fontSize(u.getFontSize())
                .voiceSpeed(u.getVoiceSpeed())
                .groupId(u.getFamilyGroup() == null ? null : u.getFamilyGroup().getId())
                .build();
    }

    public static UserUpdateResponse toUpdateResponse(User u) {
        return UserUpdateResponse.builder()
                .id(u.getId())
                .userId(u.getUserId())
                .userName(u.getUserName())
                .nickName(u.getNickName())
                .email(u.getEmail())
                .gender(u.getGender() == null ? null : u.getGender().name())
                .birth(u.getBirth() == null ? null : u.getBirth().toString())
                .userImage(u.getUserImage())
                .fontSize(u.getFontSize())
                .voiceSpeed(u.getVoiceSpeed())
                .updatedAt(u.getUpdatedAt() == null ? null : u.getUpdatedAt().toString())
                .build();
    }

}