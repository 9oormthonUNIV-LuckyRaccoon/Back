package luckkraccoon.family_memory.domain.user.service;

import luckkraccoon.family_memory.domain.user.dto.request.LoginRequest;
import luckkraccoon.family_memory.domain.user.dto.request.SignupRequest;
import luckkraccoon.family_memory.domain.user.dto.request.UserUpdateRequest;
import luckkraccoon.family_memory.domain.user.dto.response.LoginResponse;
import luckkraccoon.family_memory.domain.user.dto.response.SignupResponse;
import luckkraccoon.family_memory.domain.user.dto.response.UserUpdateResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    SignupResponse signup(SignupRequest request, MultipartFile imageFile);
    LoginResponse login(LoginRequest request);
    UserUpdateResponse updateUser(Long id, UserUpdateRequest request);

}