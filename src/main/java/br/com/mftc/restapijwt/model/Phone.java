package br.com.mftc.restapijwt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
public class Phone implements Serializable {

	private static final long serialVersionUID = 3811191486982376281L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ph_id")
    private Long id;
	
	@Column(name = "ph_number", nullable = false)
	private Integer number;
	
	@Column(name = "ph_areacode", nullable = false)
	private Integer areaCode;
	
	@Column(name = "ph_countrycode", nullable = false)
	private String countryCode;
	
	public Phone() {
		super();
	}
	
	public Phone(Integer number, Integer areaCode, String countryCode) {
		super();
		this.number = number;
		this.areaCode = areaCode;
		this.countryCode = countryCode;
	}

	public Phone(Long id, Integer number, Integer areaCode, String countryCode) {
		super();
		this.id = id;
		this.number = number;
		this.areaCode = areaCode;
		this.countryCode = countryCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
