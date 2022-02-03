package com.ftn.eUprava.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;

public class PrijaveZaVakcinaciju {

	private Map<Long, PrijavaZaVakcinaciju> prijaveZaVakcinaciju = new HashMap<>();
	private long nextJmbg = 1L;
	
	public PrijaveZaVakcinaciju() {

		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("prijaveZaVakcinaciju.txt").toURI());
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
	}
	

	
	public PrijavaZaVakcinaciju findOne(Long jmbgPacijenta) {
		return prijaveZaVakcinaciju.get(jmbgPacijenta);
	}

	public List<PrijavaZaVakcinaciju> findAll() {
		return new ArrayList<PrijavaZaVakcinaciju>(prijaveZaVakcinaciju.values());
	}
	

	public PrijavaZaVakcinaciju save(PrijavaZaVakcinaciju prijava) {
		if (prijava.getJmbgPacijenta() == null) {
			prijava.setJmbgPacijenta(++nextJmbg);
		}
		prijaveZaVakcinaciju.put(prijava.getJmbgPacijenta(), prijava);
		return prijava;
	}
	

	public List<PrijavaZaVakcinaciju> save(List<PrijavaZaVakcinaciju> prijave) {
		List<PrijavaZaVakcinaciju> ret = new ArrayList<>();

		for (PrijavaZaVakcinaciju p : prijave) {

			PrijavaZaVakcinaciju saved = save(p);

			if (saved != null) {
				ret.add(saved);
			}
		}
		return ret;
	}
	
	
}
