package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.model.entity.*;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    private final SellerRepository sellerRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, CustomerRepository customerRepository,
                            ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository,
                            OrderItemRepository orderItemRepository, ProductRepository productRepository, SellerRepository sellerRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.sellerRepository =sellerRepository;
    }

    @Override
    public List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional = customerRepository.findByEmail(userName);
        if (customerOptional.isPresent()) {
            List<OrderEntity> orderEntity = orderRepository.findOrderEntitiesByCustomerEmailAndStatus(userName, status);
            return orderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
        }
        else
        {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
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
                System.out.println("product " + referenceProduct.getId() + ": " + referenceProduct.getHoldQuantity());
                productRepository.setProductHoldQuantity(orderItemDTO.getProduct(),referenceProduct.getHoldQuantity()+orderItemDTO.getQuantity());
                System.out.println("product " + productRepository.findProductEntityById(orderItemDTO.getProduct()).getId() + ": " + productRepository.findProductEntityById(orderItemDTO.getProduct()).getHoldQuantity());
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

    @Override
    public OrderDTO findOrderByCustomerIdAndOrderId(long id, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional = customerRepository.findByEmail(userName);
        if (customerOptional.isPresent()) {
            Optional<OrderEntity> optionalOrderEntity = orderRepository.getOrderEntityById(id);
            if (optionalOrderEntity.isPresent()) {
                OrderEntity orderEntity = optionalOrderEntity.get();
                return mapToOrderDTO(orderEntity);
            } else {
                throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Order not exist!");
            }
        }
        else
        {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void cancelOrder(Long orderId, String cancelReason, String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findByEmail(email);
        if (customerEntityOptional.isPresent())
        {
            Optional<OrderEntity> optionalPendingOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Pending, email);
            if (optionalPendingOrderEntity.isPresent())
            {
                OrderEntity orderEntity = optionalPendingOrderEntity.get();
                orderEntity.setCancelReason(cancelReason);
                orderEntity.setStatus(OrderStatus.Cancel);
                orderRepository.save(orderEntity);
            }
            else {
                Optional<OrderEntity> optionalConfirmedOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Confirmed, email);
                if (optionalConfirmedOrderEntity.isPresent())
                {
                    OrderEntity orderEntity = optionalConfirmedOrderEntity.get();
                    orderEntity.setCancelReason(cancelReason);
                    orderEntity.setStatus(OrderStatus.Cancel);
                    orderRepository.save(orderEntity);
                }
                else {
                    throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
                }
            }
        }
        else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void changeOrderStatus(long orderId, String accessToken, OrderStatus orderStatus) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<SellerEntity> sellerEntityOptional = sellerRepository.findByEmail(email);
        if (sellerEntityOptional.isPresent())
        {
            Optional<OrderEntity> optionalConfirmedOrderEntity = orderRepository.getOrderEntityById(orderId);
                if (optionalConfirmedOrderEntity.isPresent())
                {
                    OrderEntity orderEntity = optionalConfirmedOrderEntity.get();
                    orderEntity.setStatus(orderStatus);
                    orderRepository.save(orderEntity);
                }
                else {
                    throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Order not exist!");
                }
        }
        else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public List<OrderDTO> findAllOrderByStatus(OrderStatus orderStatus, String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<SellerEntity> sellerEntityOptional  = sellerRepository.findByEmail(email);
        if (sellerEntityOptional.isPresent())
        {
            List<OrderEntity> listOrderEntity = orderRepository.findOrderEntitiesByStatus(orderStatus);
            return listOrderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
        }
        else  {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public int countAllOnPlaceOrder(String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<SellerEntity> sellerEntityOptional  = sellerRepository.findByEmail(email);
        if (sellerEntityOptional.isPresent())
        {
            int totalPending = orderRepository.countAllByStatus(OrderStatus.Pending);
            int totalConfirmed = orderRepository.countAllByStatus(OrderStatus.Confirmed);
            int totalDelivered = orderRepository.countAllByStatus(OrderStatus.Delivering);
            return totalConfirmed + totalDelivered + totalPending;
        }
        else  {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }


    @Override
    public void cancelOrderSeller(long orderId, String cancelReason) {
        OrderEntity orderEntity = orderRepository.getOrderEntityById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order", "Id", orderId)
        );
        orderEntity.setCancelReason(cancelReason);
        orderEntity.setStatus(OrderStatus.Cancel);
        orderRepository.save(orderEntity);
    }

    @Override
    public void makeSuccessOrder(long orderId, String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findByEmail(email);
        if (customerEntityOptional.isPresent())
        {
            Optional<OrderEntity> optionalPendingOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Delivering, email);
            if (optionalPendingOrderEntity.isPresent())
            {
                OrderEntity orderEntity = optionalPendingOrderEntity.get();
                orderEntity.setStatus(OrderStatus.Success);
                orderRepository.save(orderEntity);
            } else {
                    throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
                }
        } else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItemEntity){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItemEntity.getId());
        orderItemDTO.setImageUrl(orderItemEntity.getImageUrl());
        orderItemDTO.setName(orderItemEntity.getName());
        orderItemDTO.setProduct(orderItemEntity.getProduct());
        orderItemDTO.setPrice((int)orderItemEntity.getPrice());
        orderItemDTO.setProperty(orderItemEntity.getProperty());
        orderItemDTO.setQuantity(orderItemEntity.getQuantity());
        return orderItemDTO;
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
        orderDTO.setOrderItemDTOList(orderItemDTOList);
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
