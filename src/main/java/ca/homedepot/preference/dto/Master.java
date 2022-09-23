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

	private BigDecimal master_id;

	private BigDecimal key_id;

	private String key_value;

	private String value_val;

	private Boolean active;

}
