package com.ftn.eUprava.service;

import java.util.ArrayList;
import java.util.List;

import com.ftn.eUprava.model.VakcinalniKarton;

public interface VakcinalniKartonService {

	VakcinalniKarton findOne(Long jmbg); 
	List<VakcinalniKarton> findAll(); 
	VakcinalniKarton save(VakcinalniKarton vakcinalniKarton); 
	VakcinalniKarton update(VakcinalniKarton vakcinalniKarton);

	
}
