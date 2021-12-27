package com.example.apossbackend.security;

import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.model.entity.SellerEntity;
import com.example.apossbackend.repository.CustomerRepository;
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
import java.util.stream.Collectors;

@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;


    public CustomerDetailsService(CustomerRepository customerRepository, SellerRepository sellerRepository) {
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(sellerRepository.existsByEmail(email)){
            SellerEntity seller = sellerRepository.findByEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException("Seller not found with email" + email)
            );
            return new User(seller.getEmail(), seller.getPassword(), mapRolesToAuthorities(List.of("ROLE_USER","ROLE_ADMIN")));
        }else{
            CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(()
                    -> new UsernameNotFoundException("Customer not found with email" + email));
            return new User(customer.getEmail(), customer.getPassword(), mapRolesToAuthorities(List.of("ROLE_USER")));
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<String> roles){
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
