package ca.homedepot.preference.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Master
{
	/**
	 * The master_id
	 */
	private BigDecimal master_id;
	/**
	 * The key_id
	 */
	private BigDecimal key_id;
	/**
	 * The key_value
	 */
	private String key_value;

	/**
	 * The value_val
	 */
	private String value_val;
	/**
	 * The active value
	 */
	private Boolean active;

}
