package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.model.entity.OrderItemEntity;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import io.jsonwebtoken.Jwt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProvinceRepository provinceRepository;
    private final ProductRepository productRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final OrderItemRepository orderItemRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, CustomerRepository customerRepository,
                            ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository,
                            OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        List<OrderEntity> orderEntity = orderRepository.findOrderEntitiesByCustomerEmailAndStatus(userName, status);
        return orderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }

    @Override
    public void addNewOrder(String accessToken, OrderDTO orderDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent())
        {
            CustomerEntity customer = customerOptional.get();
            OrderEntity orderEntity = mapToOrderEntity(orderDTO, customer);
            List<OrderItemEntity> listOrderItemEntity =  orderDTO.getOrderItemDTOList().stream().map(this::mapToOrderItemEntity).collect(Collectors.toList());
            for (OrderItemEntity orderItemEntity: listOrderItemEntity) {
                orderItemEntity.setOrder(orderEntity);
                ProductEntity product  = productRepository.findProductEntityById(orderItemEntity.getProduct());
                productRepository.setProductQuantity(product.getId(), product.getQuantity()-orderItemEntity.getQuantity());
                productRepository.setProductHoldQuantity(product.getId(), 0);
            }
            orderRepository.save(orderEntity);
            orderItemRepository.saveAll(listOrderItemEntity);
        }
        else
        {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void holdOrder(String accessToken, List<OrderItemDTO> listOrderItemDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional  = customerRepository.findByEmail(email);
        if (customerOptional.isPresent())
        {
            for (OrderItemDTO orderItemDTO: listOrderItemDTO) {
                ProductEntity referenceProduct = productRepository.findProductEntityById(orderItemDTO.getProduct());
                int diffQuantity = referenceProduct.getQuantity() - referenceProduct.getHoldQuantity();
                if (orderItemDTO.getQuantity() > diffQuantity)
                {
                    throw new ApossBackendException(HttpStatus.CHECKPOINT, "Not having enough product's quantity");
                }
            }
            for (OrderItemDTO orderItemDTO: listOrderItemDTO) {
                ProductEntity referenceProduct = productRepository.findProductEntityById(orderItemDTO.getProduct());
                productRepository.setProductHoldQuantity(orderItemDTO.getProduct(), referenceProduct.getHoldQuantity() + orderItemDTO.getQuantity());
            }
        }
        else  {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void reduceHold(String accessToken, List<OrderItemDTO> listOrderItemDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional  = customerRepository.findByEmail(email);
        if (customerOptional.isPresent())
        {
            for (OrderItemDTO orderItemDTO: listOrderItemDTO) {
                ProductEntity referenceProduct = productRepository.findProductEntityById(orderItemDTO.getProduct());
                productRepository.setProductHoldQuantity(orderItemDTO.getProduct(), referenceProduct.getHoldQuantity() - orderItemDTO.getQuantity());
            }
        }
        else  {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItemEntity){
        return modelMapper.map(orderItemEntity, OrderItemDTO.class);
    }

    private  OrderItemEntity mapToOrderItemEntity(OrderItemDTO orderItemDTO){
        OrderItemEntity orderItemEntity = modelMapper.map(orderItemDTO, OrderItemEntity.class);
        orderItemEntity.setCreateTime(new Timestamp(new Date().getTime()));
        orderItemEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return orderItemEntity;
    }
    private OrderDTO mapToOrderDTO(OrderEntity orderEntity){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderEntity.getId());
        orderDTO.setOrderTime(orderEntity.getCreateTime());
        orderDTO.setOrderStatus(orderEntity.getStatus());
        orderDTO.setAddress(orderEntity.getDeliveryAddress());
        orderDTO.setTotalPrice(orderEntity.getTotalPrice());
        orderDTO.setCancelReason(orderEntity.getCancelReason());
        List<OrderItemDTO> orderItemDTOList = orderEntity.getItems().stream().map(this::mapToOrderItemDTO).collect(Collectors.toList());
        orderDTO.setOrderItemDTOList(orderItemDTOList.subList(0, 1));
        return orderDTO;
    }

    private OrderEntity mapToOrderEntity(OrderDTO orderDTO, CustomerEntity customerEntity)
    {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateTime(new Timestamp(new Date().getTime()));
        orderEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        orderEntity.setCustomer(customerEntity);
        orderEntity.setDeliveryAddress(orderDTO.getAddress());
        orderEntity.setTotalPrice(orderDTO.getTotalPrice());
        orderEntity.setStatus(OrderStatus.Pending);
        String[] listAddressName= orderDTO.getAddress().split(", ");
        long province = provinceRepository.getAllByName(listAddressName[listAddressName.length-1]).getId();
        long district = districtRepository.getDistrictEntityByNameAndProvince_Id(listAddressName[listAddressName.length-2], province).getId();
        long ward = wardRepository.getWardEntityByNameAndDistrict_Id(listAddressName[listAddressName.length-3], district).getId();
        orderEntity.setProvince(province);
        orderEntity.setDistrict(district);
        orderEntity.setWard(ward);
        return orderEntity;
    }
}
