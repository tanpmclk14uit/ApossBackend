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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductPropertyRepository productPropertyRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, ProductRepository productRepository, OrderItemRepository orderItemRepository, CartRepository cartRepository, ProductPropertyRepository productPropertyRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productPropertyRepository = productPropertyRepository;
    }


    @Override
    public List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional = customerRepository.findByEmail(userName);
        if (customerOptional.isPresent()) {
            List<OrderEntity> orderEntity = orderRepository.findOrderEntitiesByCustomerEmailAndStatus(userName, status);
            return orderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
        } else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    @Transactional
    public void addNewOrder(String accessToken, OrderDTO orderDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "email", email)
        );

        OrderEntity orderEntity = mapToOrderEntity(orderDTO, customer);
        List<OrderItemEntity> listOrderItemEntity = orderDTO.getOrderItemDTOList().stream().map(orderItemDTO -> mapToOrderItemEntity(orderItemDTO, orderEntity)).collect(Collectors.toList());

        for (OrderItemDTO orderItem : orderDTO.getOrderItemDTOList()) {
            // update quantity of product
            updateQuantityOfProduct(orderItem.getProduct(), orderItem.getQuantity());
            // update quantity of set
            updateQuantityOfSet(orderItem.getSetId(), orderItem.getQuantity());
        }
        // add update total price of order
        orderEntity.setTotalPrice(calculateTotalPrice(listOrderItemEntity));
        orderRepository.save(orderEntity);
        orderItemRepository.saveAll(listOrderItemEntity);
        // clear cart
        List<Long> cartIds = orderDTO.getOrderItemDTOList().stream().map(OrderItemDTO::getCartId).filter(aLong -> aLong != -1).collect(Collectors.toList());
        deleteALlCartInListId(cartIds);
    }

    private void updateQuantityOfSet(long setId, int soldQuantity) {
        SetEntity setEntity = productPropertyRepository.findById(setId).orElseThrow(
                () -> new ResourceNotFoundException("Set entity", "id", setId)
        );
        int newQuantity = setEntity.getQuantity() - soldQuantity;
        productPropertyRepository.updateSetQuantity(setId, newQuantity);
    }

    private void updateQuantityOfProduct(long productId, int soldQuantity) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId)
        );
        // update quantity
        productRepository.setProductQuantity(product.getId(), product.getQuantity() - soldQuantity);
    }

    private void deleteALlCartInListId(List<Long> cartIds) {
        for (Long cartId : cartIds) {
            cartRepository.deleteById(cartId);
        }
    }

    @Override
    public OrderDTO findOrderByCustomerIdAndOrderId(long id, String accessToken) {
        Optional<OrderEntity> optionalOrderEntity = orderRepository.getOrderEntityById(id);
        if (optionalOrderEntity.isPresent()) {
            OrderEntity orderEntity = optionalOrderEntity.get();
            return mapToOrderDTO(orderEntity);
        } else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Order not exist!");
        }
    }

    @Override
    public void cancelOrder(Long orderId, String cancelReason, String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findByEmail(email);
        if (customerEntityOptional.isPresent()) {
            Optional<OrderEntity> optionalPendingOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Pending, email);
            if (optionalPendingOrderEntity.isPresent()) {
                OrderEntity orderEntity = optionalPendingOrderEntity.get();
                orderEntity.setCancelReason(cancelReason);
                orderEntity.setStatus(OrderStatus.Cancel);
                orderRepository.save(orderEntity);
            } else {
                Optional<OrderEntity> optionalConfirmedOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Confirmed, email);
                if (optionalConfirmedOrderEntity.isPresent()) {
                    OrderEntity orderEntity = optionalConfirmedOrderEntity.get();
                    orderEntity.setCancelReason(cancelReason);
                    orderEntity.setStatus(OrderStatus.Cancel);
                    orderRepository.save(orderEntity);
                } else {
                    throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
                }
            }
        } else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void changeOrderStatus(long orderId, OrderStatus orderStatus) {
        Optional<OrderEntity> optionalConfirmedOrderEntity = orderRepository.getOrderEntityById(orderId);
        if (optionalConfirmedOrderEntity.isPresent()) {
            OrderEntity orderEntity = optionalConfirmedOrderEntity.get();
            orderEntity.setStatus(orderStatus);
            orderRepository.save(orderEntity);
        } else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Order not exist!");
        }
    }

    @Override
    public List<OrderDTO> findAllOrderByStatus(OrderStatus orderStatus) {
        List<OrderEntity> listOrderEntity = orderRepository.findOrderEntitiesByStatus(orderStatus);
        return listOrderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }

    @Override
    public int countAllOnPlaceOrder() {
        int totalPending = orderRepository.countAllByStatus(OrderStatus.Pending);
        int totalConfirmed = orderRepository.countAllByStatus(OrderStatus.Confirmed);
        int totalDelivered = orderRepository.countAllByStatus(OrderStatus.Delivering);
        return totalConfirmed + totalDelivered + totalPending;
    }


    @Override
    public void cancelOrderSeller(long orderId, String cancelReason) {
        OrderEntity orderEntity = orderRepository.getOrderEntityById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "Id", orderId)
        );
        orderEntity.setCancelReason(cancelReason);
        orderEntity.setStatus(OrderStatus.Cancel);
        orderRepository.save(orderEntity);
    }

    @Override
    public void makeSuccessOrder(long orderId, String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerEntityOptional = customerRepository.findByEmail(email);
        if (customerEntityOptional.isPresent()) {
            Optional<OrderEntity> optionalPendingOrderEntity = orderRepository.getOrderEntityByIdAndStatusAndCustomerEmail(orderId, OrderStatus.Delivering, email);
            if (optionalPendingOrderEntity.isPresent()) {
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

    private OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItemEntity) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItemEntity.getId());
        orderItemDTO.setImageUrl(orderItemEntity.getImageUrl());
        orderItemDTO.setName(orderItemEntity.getName());
        orderItemDTO.setPrice((int) orderItemEntity.getPrice());
        orderItemDTO.setProperty(orderItemEntity.getProperty());
        orderItemDTO.setQuantity(orderItemEntity.getQuantity());
        orderItemDTO.setProduct(orderItemEntity.getProduct());
        orderItemDTO.setSetId(orderItemEntity.getSetId());
        return orderItemDTO;
    }

    private OrderItemEntity mapToOrderItemEntity(OrderItemDTO orderItemDTO, OrderEntity orderEntity) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();

        ProductEntity product = productRepository.findById(orderItemDTO.getProduct()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", orderItemDTO.getProduct())
        );
        SetEntity setEntity = productPropertyRepository.findById(orderItemDTO.getSetId()).orElseThrow(
                () -> new ResourceNotFoundException("Set", "id", orderItemDTO.getSetId())
        );
        orderItemEntity.setName(product.getName());
        orderItemEntity.setImageUrl(product.getProductImages().get(0).getImageUrl());
        orderItemEntity.setPrice(product.getPrice() + setEntity.getAdditionalPrice());
        orderItemEntity.setQuantity(orderItemDTO.getQuantity());
        orderItemEntity.setCreateTime(new Timestamp(new Date().getTime()));
        orderItemEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        orderItemEntity.setOrder(orderEntity);
        orderItemEntity.setProduct(orderItemDTO.getProduct());
        orderItemEntity.setSetId(orderItemDTO.getSetId());
        orderItemEntity.setProperty(makeStringPropertyBySet(setEntity));
        return orderItemEntity;
    }

    private String makeStringPropertyBySet(SetEntity setEntity) {
        StringBuilder property = new StringBuilder();
        for (SetValueEntity setValueEntity : setEntity.getSetValueEntity()) {
            property.append(setValueEntity.getClassifyProductValue().getClassifyProduct().getName()).append(": ").append(setValueEntity.getClassifyProductValue().getName()).append(", ");
        }
        return property.toString();
    }

    private OrderDTO mapToOrderDTO(OrderEntity orderEntity) {
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

    private OrderEntity mapToOrderEntity(OrderDTO orderDTO, CustomerEntity customerEntity) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreateTime(new Timestamp(new Date().getTime()));
        orderEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        orderEntity.setCustomer(customerEntity);
        orderEntity.setDeliveryAddress(orderDTO.getAddress());
        orderEntity.setStatus(OrderStatus.Pending);
        return orderEntity;
    }

    private int calculateTotalPrice(List<OrderItemEntity> orderItemEntities) {
        int totalPrice = 0;
        for (OrderItemEntity orderItem : orderItemEntities) {
            totalPrice += orderItem.getPrice() * orderItem.getQuantity();
        }
        return totalPrice;
    }

//    private OrderStatus mapToOrderStatus(int statusInt) {
//        if (statusInt == 0) {
//            return OrderStatus.Pending;
//        } else if (statusInt == 1) {
//            return OrderStatus.Confirmed;
//        } else if (statusInt == 2) {
//            return OrderStatus.Delivering;
//        } else if (statusInt == 3) {
//            return OrderStatus.Success;
//        } else {
//            return OrderStatus.Cancel;
//        }
//    }
}
