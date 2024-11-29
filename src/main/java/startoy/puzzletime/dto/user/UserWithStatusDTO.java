package startoy.puzzletime.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import startoy.puzzletime.domain.User;

@Getter
@AllArgsConstructor
public class UserWithStatusDTO {
    private User user;
    private boolean isNewUser;
}