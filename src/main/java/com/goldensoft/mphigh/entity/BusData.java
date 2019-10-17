package com.goldensoft.mphigh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class BusData {

	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String region;
	
	private String buyOrg;
	
	private String sellOrg;
	
	private String orderCode;
	
	private String applyCode;
	
	private String usuallyName;
	
	private String productName;
	
	private String dosageForm;
	
	private String specs;
	
	private String producer;
	
	private String supplyPrice;
	
	private String price;
	
	private String purchaseCount;
	
	private String deliveryCount;
	
	private String checkCount;
	
	private String purchaseTime;
	
	private String deliveryTime;
	
	private String checkTime;
	
	private String status;
	
	private String searchProjectName;
	
	private String searchStartDate;
	
	private String searchEndDate;

	public BusData(String region, String buyOrg, String sellOrg, String orderCode, String applyCode, String usuallyName,
			String productName, String dosageForm, String specs, String producer, String supplyPrice, String price,
			String purchaseCount, String deliveryCount, String checkCount, String purchaseTime, String deliveryTime,
			String checkTime, String status, String searchProjectName, String searchStartDate, String searchEndDate) {
		super();
		this.region = region;
		this.buyOrg = buyOrg;
		this.sellOrg = sellOrg;
		this.orderCode = orderCode;
		this.applyCode = applyCode;
		this.usuallyName = usuallyName;
		this.productName = productName;
		this.dosageForm = dosageForm;
		this.specs = specs;
		this.producer = producer;
		this.supplyPrice = supplyPrice;
		this.price = price;
		this.purchaseCount = purchaseCount;
		this.deliveryCount = deliveryCount;
		this.checkCount = checkCount;
		this.purchaseTime = purchaseTime;
		this.deliveryTime = deliveryTime;
		this.checkTime = checkTime;
		this.status = status;
		this.searchProjectName = searchProjectName;
		this.searchStartDate = searchStartDate;
		this.searchEndDate = searchEndDate;
	}

	public BusData() {
		super();
	}
	
	
}
