package com.om.project.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.om.project.user.dto.BuyerDTO;
import com.om.project.user.dto.CartDTO;
import com.om.project.user.dto.LoginDTO;
import com.om.project.user.dto.WishlistDTO;
import com.om.project.user.entity.Buyer;
import com.om.project.user.entity.Cart;
import com.om.project.user.entity.Wishlist;
import com.om.project.user.exception.InfyMarketException;
import com.om.project.user.repository.BuyerRepository;
import com.om.project.user.repository.CartRepository;
import com.om.project.user.repository.WishlistRepository;

@Service
@Transactional
public class BuyerService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	BuyerRepository buyerrepo;

	@Autowired
	WishlistRepository wishrepo;

	@Autowired
	CartRepository cartrepo;

	//Register buyer
	public void saveBuyerDetails(BuyerDTO buyerDTO) throws InfyMarketException {
		logger.info("Registration request for buyer with data {}", buyerDTO);
		Buyer buyer = buyerDTO.generateBuyer();
		buyerrepo.save(buyer);
	}

	//Get all buyer details
	public List<BuyerDTO> getAllBuyerDetails() throws InfyMarketException {

		Iterable<Buyer> buyers = buyerrepo.findAll();
		List<BuyerDTO> buyerDTOs = new ArrayList<>();

		buyers.forEach(buyer -> {
			BuyerDTO buyerDTO = BuyerDTO.valueOf(buyer);
			buyerDTOs.add(buyerDTO);
		});
		if (buyerDTOs.isEmpty())
			throw new InfyMarketException("Service.BUYERS_NOT_FOUND");
		logger.info("Buyer Details : {}", buyerDTOs);
		return buyerDTOs;
	}

	//Get buyer by id
	public BuyerDTO getBuyerById(String buyerId) throws InfyMarketException {
		BuyerDTO buyerDTO = null;
		logger.info("Profile request for buyer {}", buyerId);
		Optional<Buyer> optBuyer = buyerrepo.findById(buyerId);
		if (optBuyer.isPresent()) {
			Buyer buyer = optBuyer.get();
			buyerDTO = BuyerDTO.valueOf(buyer);
		} else {
			throw new InfyMarketException("Service.BUYERS_NOT_FOUND");
		}
		logger.info("Profile for buyer : {}", buyerDTO);
		return buyerDTO;
	}

	//Buyer Login 
	public boolean buyerLogin(LoginDTO loginDTO) throws InfyMarketException {
		logger.info("Login request for buyer {} with password {}", loginDTO.getEmail(), loginDTO.getPassword());
		Buyer buy = buyerrepo.findByEmail(loginDTO.getEmail());
		if (buy != null && buy.getPassword().equals(loginDTO.getPassword())) {
			return true;
		} else {
			throw new InfyMarketException("Service.DETAILS_NOT_FOUND");
		}
	}

	//Delete buyer
	//public void removeBuyer(String buyerid) throws InfyMarketException {
	//	Optional<Buyer> buyer = buyerrepo.findById(buyerid);
	//	buyer.orElseThrow(() -> new InfyMarketException("Service.BUYERS_NOT_FOUND"));
	//	buyerrepo.deleteById(buyerid);
	//}

//	//Get wishlist of buyer
//	public WishlistDTO getWishlistOfBuyer(String buyerid) throws InfyMarketException {
//		Wishlist wishlist = buyerrepo.getWishlist(buyerid);
//		WishlistDTO wishlistDTOs = null;
//		if (wishlist != null) {
//			wishlistDTOs = WishlistDTO.valueOf(wishlist);
//		} else {
//			throw new InfyMarketException("Service.ORDERS_NOT_FOUND");
//		}
//		return wishlistDTOs;
//	}

	

	

	//Delete product from wishlist
	//public void removeWishlist(String buyerid) throws InfyMarketException {
	//	Optional<Wishlist> buyer = wishrepo.findById(buyerid);
	//	buyer.orElseThrow(() -> new InfyMarketException("Service.Buyer_NOT_FOUND"));
	//	wishrepo.deleteById(buyerid);
	//}
	
	

	

	//Delete product from cart
	//public void removeCart(String buyerid) throws InfyMarketException {
	//	Optional<Cart> buyer = cartrepo.findById(buyerid);
	//	buyer.orElseThrow(() -> new InfyMarketException("Service.Buyer_NOT_FOUND"));
	//	cartrepo.deleteById(buyerid);
	//}

	//Update isprivileged
	public Buyer updateIsprivilege(Buyer buyer, String buyerid) throws InfyMarketException{
		Buyer existingBuyer = buyerrepo.findById(buyerid).orElse(null);
		if ((existingBuyer != null) && (existingBuyer.getRewardpoints() >= 10000)) {
			existingBuyer.setIsprivileged(buyer.getIsprivileged());
			return buyerrepo.save(existingBuyer);
		}else {
			throw new InfyMarketException("Service.NO_REWARD_POINTS");
		}
	}

}
