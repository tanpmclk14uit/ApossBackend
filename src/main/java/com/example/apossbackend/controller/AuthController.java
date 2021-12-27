package com.example.apossbackend.controller;
import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.model.dto.JWTAuthResponse;
import com.example.apossbackend.model.dto.SignInDTO;
import com.example.apossbackend.model.dto.SignInWithSocialDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.AuthService;
import com.example.apossbackend.service.ConfirmationService;
import com.example.apossbackend.service.CustomerService;
import com.example.apossbackend.service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ConfirmationService confirmationService;
    private final JwtTokenProvider tokenProvider;
    private final EmailSender emailSender;
    private final CustomerService customerService;

    @Autowired
    public AuthController(AuthService authService, ConfirmationService confirmationService, JwtTokenProvider tokenProvider, EmailSender emailSender, CustomerService customerService) {
        this.authService = authService;
        this.confirmationService = confirmationService;
        this.tokenProvider = tokenProvider;
        this.emailSender = emailSender;
        this.customerService = customerService;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody SignInDTO signInDTO) {
        Authentication authentication = authService.signIn(signInDTO.getEmail(), signInDTO.getPassword());
        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(signInDTO.getEmail(), signInDTO.getPassword());
        return ResponseEntity.ok(new JWTAuthResponse(token, refreshToken));
    }

    @PostMapping("/sign-in-with-social-account")
    public ResponseEntity<JWTAuthResponse> signInWithGoogle(@RequestBody SignInWithSocialDTO signInWithGoogle) {
        if(!signInWithGoogle.getSecretKey().equals("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")){
            throw  new ApossBackendException(HttpStatus.BAD_REQUEST, "Invalid google login!!");
        }
        String password = "";
        if(authService.isEmailExist(signInWithGoogle.getEmail())){
            password = customerService.findCustomerByEmail(signInWithGoogle.getEmail()).getPassword();
        }else{
            password = UUID.randomUUID().toString();
        }
        authService.signInWithGoogle(signInWithGoogle, password);
        String token = tokenProvider.generateSocialAccountToken(signInWithGoogle.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(signInWithGoogle.getEmail(), password);
        return ResponseEntity.ok(new JWTAuthResponse(token, refreshToken));
    }

    @PostMapping("/sign-up")
    @Transactional
    public ResponseEntity<String> registerCustomer(@RequestBody SignUpDTO signUpDTO) {
        if (authService.isEmailExist(signUpDTO.getEmail())) {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Email was already taken!");
        }
        CustomerEntity customer = authService.createCustomer(signUpDTO);
        String token = confirmationService.createNewToken(customer);
        String link = "http://127.0.0.1:8081/api/v1/auth/confirm?token="+token;
        emailSender.send(customer.getEmail(), buildEmail(customer.getName(), link));
        return new ResponseEntity<>("\"Waiting for confirm account!\"", HttpStatus.OK);
    }

    @GetMapping("/resent-confirm")
    public ResponseEntity<String> resentConfirmMail(@RequestParam("email") String email){
        if(email.startsWith("\"") && email.endsWith("\"")){
            email = email.substring(1, email.length()-1);
        }
        CustomerEntity customer = customerService.findCustomerByEmail(email);
        if(customer.isActive()){
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Customer account was already activated");
        }
        String token = confirmationService.createNewToken(customer);
        String link = "http://127.0.0.1:8081/api/v1/auth/confirm?token="+token;
        emailSender.send(customer.getEmail(), buildEmail(customer.getName(), link));
        return new ResponseEntity<>("\"Resent confirmation email success!\"", HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> validateCustomer(@RequestParam("token") String token){
        ConfirmationToken confirmationToken = confirmationService.findByToken(token);
        if(authService.updateActivatedByToken(confirmationToken)){
            return new ResponseEntity<>("Confirm account success!", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Confirm account failure!", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/access-token")
    public ResponseEntity<String> getNewAccessToken(@RequestBody String refreshToken) {
        if(refreshToken.startsWith("\"") && refreshToken.endsWith("\"")){
            refreshToken = refreshToken.substring(1, refreshToken.length()-1);
        }
        if(!tokenProvider.validateToken(refreshToken)){
            throw new ApossBackendException(HttpStatus.BAD_REQUEST,"Refresh token error");
        }
        SignInDTO signInDTO = tokenProvider.getUserNamePasswordFromRefreshToken(refreshToken);

        Authentication authentication = refreshToken.startsWith("Bearer Seller ")?authService.signInSeller(signInDTO.getEmail(), signInDTO.getPassword()):authService.signIn(signInDTO.getEmail(), signInDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/seller-sign-in")
    public ResponseEntity<JWTAuthResponse> authenticateSeller(@RequestBody SignInDTO signInDTO) {
        Authentication authentication = authService.signInSeller(signInDTO.getEmail(), signInDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(signInDTO.getEmail(), signInDTO.getPassword());
        return ResponseEntity.ok(new JWTAuthResponse(token, refreshToken));
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
