package at.uibk.leco.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}
