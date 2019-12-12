package com.voice.law.util

import com.voice.law.domain.AuthUser
import com.voice.law.domain.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

class JwtUtil {
    private static final String secret = "law-voice"

    static String generateToken(String username, List<Role> roles) {
        Map<String, Object> claims = new HashMap<>()
        String rolesStr = ""

        roles.each { it ->
            rolesStr += (it.getRoleName() + ",")
        }

        if (rolesStr.length() > 0) {
            rolesStr = rolesStr.substring(0, rolesStr.length() - 1)
        }

        claims.put("roles", rolesStr)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
        //创建时间
                .setIssuedAt(new Date())
        //过期时间，我们设置为 30分钟
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
        //签名，通过密钥保证安全性
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    static AuthUser parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
        String username = claims.getSubject()
        String roles = (String) claims.get("roles")

        //因为生成的时候没有放入密码，所以不需要密码
        return new AuthUser(username, null, roles.split(",").collect { name ->
            Role role = new Role()
            role.setRoleName(name)
            return role
        }
        )
    }
}
