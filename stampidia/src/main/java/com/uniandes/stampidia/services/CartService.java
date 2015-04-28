package com.uniandes.stampidia.services;

import com.uniandes.stampidia.model.StmpOrder;
import com.uniandes.stampidia.model.StmpOrderDetail;
import com.uniandes.stampidia.model.StmpShirt;
import com.uniandes.stampidia.repos.OrderDetailRepository;
import com.uniandes.stampidia.repos.OrderRepository;
import com.uniandes.stampidia.repos.ShirtRepository;
import com.uniandes.stampidia.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Created by SEBASTIAN on 02/04/2015.
 */
@Service
@Transactional
public class CartService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ShirtRepository shirtRepository;

    @Autowired
    private UserRepository userRepository;


    public StmpOrder updateOrder(StmpOrder order){
        StmpOrder answer = null;
        if(order != null){
            try {
                if(order.getIdUser() != null && order.getIdUser().getId() != null){
                    order.setIdUser(userRepository.findOne(order.getIdUser().getId()));
                    answer = orderRepository.save(order);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }
        return answer;
    }

    public void addItemToCart(Integer shirtId, Integer orderId){
        if(orderId != null && shirtId != null ){
            boolean contained = false;

            StmpOrder order = orderRepository.findOne(orderId);

            if(order != null){
                if(order.getStmpOrderDetailList() != null && !order.getStmpOrderDetailList().isEmpty()){
                    for(StmpOrderDetail detail : order.getStmpOrderDetailList()){
                        if(detail.getIdShirt().getId().equals(shirtId)){
                            contained = true;
                            detail.setQuantity(detail.getQuantity() + 1);
                            break;
                        }
                    }
                }
                // Si la Orden no contiene la camiseta
                if(!contained){
                    StmpShirt shirt = shirtRepository.findOne(shirtId);
                    StmpOrderDetail newDetail = new StmpOrderDetail();
                    newDetail.setQuantity(1);
                    newDetail.setUnitValue(shirt.getIdStamp().getPrice().add(shirt.getIdStyle().getPrice()));
                    newDetail.setIdOrder(order);
                    newDetail.setIdShirt(shirt);
                    order.getStmpOrderDetailList().add(newDetail);
                }
                orderRepository.save(order);
            }
        }
    }

    public StmpOrder deleteItemFromCart(Integer orderId, Integer itemId){
        if(itemId != null && orderId != null){
            StmpOrder order = orderRepository.findOne(orderId);

            if(order != null && order.getStmpOrderDetailList() != null){
                for(StmpOrderDetail detail : order.getStmpOrderDetailList()){
                    if(detail.getIdShirt().getId().equals(itemId)){
                        order.getStmpOrderDetailList().remove(detail);
                        orderDetailRepository.delete(detail);
                        break;
                    }
                }
                return orderRepository.save(order);
            }
        }
        // TODO :: implementar cuando else
        return null;
    }

    public StmpOrder getCartProducts(Integer userId){
        StmpOrder answer = null;

        // TODO :: manejar errores posibles
        answer = (StmpOrder) orderRepository.findStmpOrderByUserId(userId);
//        answer.getStmpOrderDetailList().size();

        return answer;
    }


    public StmpOrder getOrderById(Integer orderId){
        return orderRepository.findOne(orderId);
    }

    public List<StmpOrderDetail> getOrderDetailsByOrderId(Integer orderId){
        try {
            return orderDetailRepository.getOrderDetailsByOrderId(orderId);
        }catch (NoResultException e){
            return null;
        }
    }

    public void saveOrderDetails(List<StmpOrderDetail> details) {
        if (details != null && !details.isEmpty()) {
            for (StmpOrderDetail det : details) {
                orderDetailRepository.save(det);
            }
        }
    }

    public List<StmpOrder> getOrdersByUser(Integer userId){

		return orderRepository.findStmpOrderClosedByUserId(userId);
    }
}
