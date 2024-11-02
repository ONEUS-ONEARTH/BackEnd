package kr.co.ouoe.Util;


import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class TokenUserInfo {
    private String userId;
    private String email;
}
