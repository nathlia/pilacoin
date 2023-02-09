//package br.ufsm.poli.csi.tapw.pilacoin.server.controller;
//
//import br.ufsm.poli.csi.tapw.pilacoin.server.model.Usuario;
//import br.ufsm.poli.csi.tapw.pilacoin.server.repositories.UsuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class LoginController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    final
//    UsuarioRepository userAccountRepository;
//
//    public LoginController(UsuarioRepository userAccountRepository) {
//        this.userAccountRepository = userAccountRepository;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Object> auth(@RequestBody Usuario userAccount) {
//        System.out.println("Username: " + userAccount.getNome());
//
//        try {
//            final Authentication authenticaticon = this.authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(userAccount.getNome(), userAccount.getSenha()));
//            if (authenticaticon.isAuthenticated()) {
//                SecurityContextHolder.getContext().setAuthentication(authenticaticon);
//
//                System.out.println("*** Generating Authorization Token ***");
//                String token = new JWTUtil().geraToken(userAccount.getNome());
//
//                userAccount.setToken(token);
//                userAccount.setSenha("");
//
//                return new ResponseEntity<>(userAccount, HttpStatus.OK);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("User or Password Incorrect", HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>("User or Password Incorrect", HttpStatus.BAD_REQUEST);
//    }
//}
