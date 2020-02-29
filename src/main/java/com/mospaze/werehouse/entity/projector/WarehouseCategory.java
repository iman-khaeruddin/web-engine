package com.mospaze.werehouse.entity.projector;

/**
 * @author Iman Khaeruddin
 * @since 14/11/2019
 */
public interface WarehouseCategory {
	int getId();
	String getCategory_name();
	String getInbound_price();
	String getOutbound_price();
	String getInbound_uom();
	String getOutbound_uom();
}
