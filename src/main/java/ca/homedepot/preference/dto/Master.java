package ca.homedepot.preference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Master
{
	/**
	 * The master_id
	 */
	private BigDecimal masterId;
	/**
	 * The key_id
	 */
	private BigDecimal keyId;
	/**
	 * The key_value
	 */
	private String keyValue;

	/**
	 * The value_val
	 */
	private String valueVal;
	/**
	 * The active value
	 */
	private Boolean active;

	/**
	 * The old id
	 */
	private BigDecimal oldID;

}
