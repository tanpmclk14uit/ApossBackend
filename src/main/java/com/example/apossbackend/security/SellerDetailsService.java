package com.example.apossbackend.security;

import com.example.apossbackend.model.entity.SellerEntity;
import com.example.apossbackend.repository.SellerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SellerDetailsService implements UserDetailsService {

    private final SellerRepository sellerRepository;

    public SellerDetailsService(SellerRepository sellerRepository)
    {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SellerEntity seller = sellerRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Seller not found with email" + email)
        );
        return new User(seller.getEmail(), seller.getPassword(), mapRolesToAuthorities(List.of("ROLE_USER")));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<String> roles){
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
