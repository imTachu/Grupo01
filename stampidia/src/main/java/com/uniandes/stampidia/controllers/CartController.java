package com.uniandes.stampidia.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.uniandes.stampidia.model.StmpOrder;
import com.uniandes.stampidia.model.StmpOrderDetail;
import com.uniandes.stampidia.services.CartService;
import com.uniandes.stampidia.utilities.Constantes;
import com.uniandes.stampidia.utilities.Resultado;
import com.uniandes.stampidia.utilities.Status;
import com.uniandes.stampidia.utilities.enums.EStatusType;


@RestController
@RequestMapping(value="/rest")
public class CartController {

	@Autowired
	private CartService cartService;

	@RequestMapping(value="/cart",method= RequestMethod.PUT)
	public Resultado saveCart(
			@RequestBody StmpOrder order){
		Resultado resultado = new Resultado();

		if(!order.isNull()){

			order.calcTotalAmount();
			StmpOrder newOrder = cartService.updateOrder(order);

			if(newOrder != null){
				resultado.setResultado(newOrder);
				resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
			}else {
				resultado.setEstado(new Status(EStatusType.ERROR, Constantes.ERROR_RESULT.getDescription()));
			}

		}else {
			resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
		}

		return resultado;
	}

	@RequestMapping(value="/cart/{oderId}/details",method= RequestMethod.GET)
	public Resultado getCartProducts(
			@PathVariable("oderId")Integer oderId){
		Resultado resultado = new Resultado();

		if(oderId != null){
			List<StmpOrderDetail> detalles = cartService.getOrderDetailsByOrderId(oderId);
			if(detalles != null) {
				resultado.setResultado(detalles);
				resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
			}else{
				resultado.setEstado(new Status(EStatusType.ERROR, Constantes.ERROR_RESULT.getDescription()));
			}
		}else {
			resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
		}

		return resultado;
	}

	@RequestMapping(value="/order/{id}",method= RequestMethod.GET)
	public Resultado getCartOrdersByUser(
			@PathVariable("id")Integer Id){
		Resultado resultado = new Resultado();

		if(Id != null){
			resultado.setResultado(cartService.getOrdersByUser(Id));
			resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
			resultado.setMensajeConsulta("Este es el resultado!");
			resultado.setTotalObjetos(cartService.getOrdersByUser(Id).size());

		}else {
			resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
		}

		return resultado;
	}
	
	private boolean validateDetailsByOrderId(Integer orderId, List<StmpOrderDetail> details) throws Exception {
		if(orderId != null && details != null && !details.isEmpty()){
			for(StmpOrderDetail det : details){
				if (!det.getIdOrder().getId().equals(orderId)){
					throw new Exception();
				}
			}
			return true;
		}
		throw new Exception();
	}
	
	@RequestMapping(value="/cart/{oderId}/details",method= RequestMethod.PUT)
	public Resultado saveCartProducts(
			@PathVariable("oderId")Integer oderId,
			@RequestBody List<StmpOrderDetail> details){
		Resultado resultado = new Resultado();

		if(oderId != null && details != null && !details.isEmpty()){
			try {
				validateDetailsByOrderId(oderId, details);
				cartService.saveOrderDetails(details);
				resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
			}catch (Exception e){
				resultado.setEstado(new Status(EStatusType.ERROR, Constantes.ERROR_RESULT.getDescription()));

			}
		}else {
			resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
		}

		return resultado;
	}
	

	// TODO :: sebastian.gamba :: validar si este servicios si se va a necesitar
	//    @RequestMapping(value="/cart/{userId}",method= RequestMethod.GET)
	//    public Resultado getCart(
	//            @PathVariable("userId")Integer userId){
	//        Resultado resultado = new Resultado();
	//
	//        if(userId != null){
	//            resultado.setResultado(cartService.getCartProducts(userId));
	//            resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
	//        }else {
	//            resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
	//        }
	//
	//        return resultado;
	//    }

	@RequestMapping(value="/cart/{orderId}",method= RequestMethod.GET)
	public Resultado getCartByOrderId(
			@PathVariable("orderId")Integer orderId){
		Resultado resultado = new Resultado();

		if(orderId != null){
			resultado.setResultado(cartService.getOrderById(orderId));
			resultado.setEstado(new Status(EStatusType.OK, Constantes.SUCCESS_RESULT.getDescription()));
		}else {
			resultado.setEstado(new Status(EStatusType.ERROR, Constantes.INVALID_PARAMS_RESULT.getDescription()));
		}

		return resultado;
	}

	
}
