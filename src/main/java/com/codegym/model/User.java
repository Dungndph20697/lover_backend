package com.codegym.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "username không được để trống")
    @Size(min = 3, max = 50, message = "username phải từ 3 đến 50 ký tự")
    private String username;

    @NotBlank(message = "password không được để trống")
//    @Size(min = 6, max = 15, message = "password phải từ 6 đến 15 ký tự")
    @Column(length = 100) // đủ chứa bcrypt hash
    private String password;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "email không được để trống")
    @Email(message = "email không đúng định dạng")
    private String email;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Column(nullable = false, length = 12)
    @NotBlank(message = "Số CCCD không được để trống")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Số CCCD phải từ 9-12 chữ số")
    private String cccd;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Họ không được để trống")
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Tên không được để trống")
    private String lastName;

    @Column(length = 100)
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}