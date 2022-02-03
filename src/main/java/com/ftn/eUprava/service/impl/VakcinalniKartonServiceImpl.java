package com.ftn.eUprava.service.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;
import com.ftn.eUprava.model.PrijavaZaVakcinaciju;
import com.ftn.eUprava.model.VakcinalniKarton;
import com.ftn.eUprava.service.VakcinalniKartonService;
@Service
@Qualifier("fajloviVakcinalniKarton")
public class VakcinalniKartonServiceImpl implements VakcinalniKartonService{

	@Value("${vakcinalniKarton.pathToFile}")
	private String pathToFile;
	
    private Map<Integer, VakcinalniKarton> readFromFile() {

    	Map<Integer, VakcinalniKarton> vakcinalniKartonPacijanta = new HashMap<>();
    	int nextidKartona = 1;
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				String[] tokens = line.split(";");
				int idKartona = Integer.parseInt(tokens[0]);
				Long jmbgPacijenta = Long.parseLong(tokens[1]);
				String imePacijenta = tokens[2];
				String prezimePacijenta = tokens[3];
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime prijavaPacijenta = LocalDateTime.parse(tokens[4],formatter);
				LocalDateTime datumVakcinacije = LocalDateTime.parse(tokens[5],formatter);
				Vakcine vakcina = Vakcine.valueOf(tokens[6]);
				Doze doza = Doze.valueOf(tokens[7]);
				
				vakcinalniKartonPacijanta.put(Integer.parseInt(tokens[0]), new VakcinalniKarton(idKartona, jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, datumVakcinacije, vakcina, doza));
				if(nextidKartona<idKartona)
					nextidKartona=idKartona;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return vakcinalniKartonPacijanta;
    }
    
    private Map<Integer, VakcinalniKarton> saveToFile(Map<Integer, VakcinalniKarton> vakcinalniKartoni) {
    	
    	Map<Integer, VakcinalniKarton> ret = new HashMap<>();
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();
			
			for (VakcinalniKarton vakcinalniKarton : vakcinalniKartoni.values()) {
				String line = vakcinalniKarton.toString(); 
				lines.add(line);
				ret.put(vakcinalniKarton.getIdKartona(), vakcinalniKarton);
			}
			Files.write(path, lines, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }
	
    public Integer nextIdKartona(Map<Integer, VakcinalniKarton> vakcinalniKartoni) {
    	Integer nextidKartona = 1;
    	for(Integer idKartona : vakcinalniKartoni.keySet()) {
    		if(idKartona>nextidKartona) {
    			nextidKartona = idKartona;
    		}
    	}
    	
    	return ++nextidKartona;
    }
    


	@Override
	public List<VakcinalniKarton> findAll() {
		Map<Integer, VakcinalniKarton> vakcinalniKartoni = readFromFile();
		return new ArrayList<VakcinalniKarton>(vakcinalniKartoni.values());
	}
	
	public List<VakcinalniKarton> nadjiSveKartonePacijenta(List<VakcinalniKarton> listaKartona, Long jmbg){
		ArrayList<VakcinalniKarton> sviKartoni = new ArrayList<VakcinalniKarton>();
		for(VakcinalniKarton kartoni : listaKartona){
			if(kartoni.getJmbgPacijenta().equals(jmbg)){
				sviKartoni.add(kartoni);
			}
		}
		return sviKartoni;
	}
	

    public int generisiNoviId(List<VakcinalniKarton> listaKartona) {
        int maks = -1;
        for (VakcinalniKarton a : listaKartona) {
            if (a.getIdKartona() > maks) {
                maks = a.getIdKartona();
            }
        }
        return maks + 1;
    }
	
	@Override
	public VakcinalniKarton save(VakcinalniKarton vakcinalniKarton) {
		Map<Integer, VakcinalniKarton> vakcinalniKartoni = readFromFile();
		
		Integer nextIdKartona = nextIdKartona(vakcinalniKartoni);
		
		if(vakcinalniKarton.getIdKartona() == 0) {
			vakcinalniKarton.setIdKartona(nextIdKartona);
		}
		
		vakcinalniKartoni.put(vakcinalniKarton.getIdKartona(), vakcinalniKarton);
		saveToFile(vakcinalniKartoni);
		return vakcinalniKarton;
	}
	
	@Override
	public VakcinalniKarton update(VakcinalniKarton vakcinalniKarton) {
		Map<Integer, VakcinalniKarton> vakcinalniKartoni = readFromFile();
		vakcinalniKartoni.put(vakcinalniKarton.getIdKartona(), vakcinalniKarton);
		
		saveToFile(vakcinalniKartoni);
		return vakcinalniKarton;
	}

	@Override
	public VakcinalniKarton findOne(Long jmbg) {
		Map<Integer, VakcinalniKarton> prijave = readFromFile();
		return prijave.get(jmbg);
	}



}
