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
import com.ftn.eUprava.service.PrijavaZaVakcinacijuService;

@Service
@Qualifier("fajloviPrijavaZaVakcinaciju")
public class PrijavaZaVakcinacijuServiceImpl  implements PrijavaZaVakcinacijuService{


	@Value("${prijaveZaVakcinaciju.pathToFile}")
	private String pathToFile;
	
    private Map<Long, PrijavaZaVakcinaciju> readFromFile() {

    	Map<Long, PrijavaZaVakcinaciju> prijaveZaVakcinaciju = new HashMap<>();
    	Long nextJmbg = 1L;
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				String[] tokens = line.split(";");
				Long jmbgPacijenta = Long.parseLong(tokens[0]);
				String imePacijenta = tokens[1];
				String prezimePacijenta = tokens[2];
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime prijavaPacijenta = LocalDateTime.parse(tokens[3],formatter);
				Vakcine vakcina = Vakcine.valueOf(tokens[4]);
				Doze doza = Doze.valueOf(tokens[5]);
				Boolean vakcinisan = Boolean.parseBoolean(tokens[6]);
				
				prijaveZaVakcinaciju.put(Long.parseLong(tokens[0]), new PrijavaZaVakcinaciju(jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, vakcina, doza, vakcinisan));
				
				if(nextJmbg<jmbgPacijenta)
					nextJmbg=jmbgPacijenta;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return prijaveZaVakcinaciju;
    }
    
    private Map<Long, PrijavaZaVakcinaciju> saveToFile(Map<Long, PrijavaZaVakcinaciju> prijaveZaVakcinaciju) {
    	
    	Map<Long, PrijavaZaVakcinaciju> ret = new HashMap<>();
    	
    	try {
			Path path = Paths.get(pathToFile);
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = new ArrayList<>();
			
			for (PrijavaZaVakcinaciju prijava : prijaveZaVakcinaciju.values()) {
				String line = prijava.toString(); 
				lines.add(line);
				ret.put(prijava.getJmbgPacijenta(), prijava);
			}
			Files.write(path, lines, Charset.forName("UTF-8"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ret;
    }

    private Long nextJmbg(Map<Long, PrijavaZaVakcinaciju> prijave) {
    	Long nextJmbg = 1L;
    	for(Long jmbg : prijave.keySet()) {
    		if(jmbg>nextJmbg) {
    			nextJmbg = jmbg;
    		}
    	}
    	return ++nextJmbg;
    }
    
	@Override
	public PrijavaZaVakcinaciju findOne(Long jmbg) {
		Map<Long, PrijavaZaVakcinaciju> prijave = readFromFile();
		return prijave.get(jmbg);
	}

	@Override
	public List<PrijavaZaVakcinaciju> findAll() {
		Map<Long, PrijavaZaVakcinaciju> prijave = readFromFile();
		return new ArrayList<PrijavaZaVakcinaciju>(prijave.values());
	}

	@Override
	public PrijavaZaVakcinaciju save(PrijavaZaVakcinaciju prijava) {
		Map<Long, PrijavaZaVakcinaciju> prijave = readFromFile();
		
		Long nextJmbg = nextJmbg (prijave);
		
		if(prijava.getJmbgPacijenta() == null) {
			prijava.setJmbgPacijenta(nextJmbg);
		}
		
		prijave.put(prijava.getJmbgPacijenta(), prijava);
		saveToFile(prijave);
		return prijava;
	}
	
}
