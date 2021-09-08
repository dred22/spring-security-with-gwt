package com.eazybytes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id")
	private int id;
	private String name;
	private String email;
	@Column(name = "mobile_number")
	private String mobileNumber;
	@JsonIgnore
	private String pwd;
	private String role;
	@Column(name = "create_dt")
	private String createDt;

	@JsonIgnore
	//@OneToMany(mappedBy="customer",fetch=FetchType.EAGER)
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Authority> authorities = new HashSet<>();
}
