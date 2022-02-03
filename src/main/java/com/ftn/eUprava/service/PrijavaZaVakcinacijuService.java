package com.ftn.eUprava.service;

import java.util.List;

import com.ftn.eUprava.model.PrijavaZaVakcinaciju;

public interface PrijavaZaVakcinacijuService {

	PrijavaZaVakcinaciju findOne(Long jmbgPacijenta);
	List<PrijavaZaVakcinaciju> findAll();
	PrijavaZaVakcinaciju save(PrijavaZaVakcinaciju prijava);
}
