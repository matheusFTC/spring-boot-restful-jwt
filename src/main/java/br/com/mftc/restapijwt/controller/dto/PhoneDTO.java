package br.com.mftc.restapijwt.controller.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class PhoneDTO implements Serializable {

	private static final long serialVersionUID = 3262917041635231453L;

	private Integer number;

	@JsonProperty("area_code")
	private Integer areaCode;

	@JsonProperty("country_code")
	private String countryCode;
	
	public PhoneDTO() {
		super();
	}

	public PhoneDTO(Integer number, Integer areaCode, String countryCode) {
		super();
		this.number = number;
		this.areaCode = areaCode;
		this.countryCode = countryCode;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
}
