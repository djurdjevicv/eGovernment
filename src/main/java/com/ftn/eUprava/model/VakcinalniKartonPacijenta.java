package com.ftn.eUprava.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;

public class VakcinalniKartonPacijenta {

	private Map<Integer, VakcinalniKarton> vakcinalniKartonPacijanta = new HashMap<>();
	private int nextidKartona = 1;
	
	public VakcinalniKartonPacijenta() {
		
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("vakcinalniKarton.txt").toURI());
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
				
				vakcinalniKartonPacijanta.put(Integer.parseInt(tokens[0]), new VakcinalniKarton(idKartona, jmbgPacijenta,imePacijenta, prezimePacijenta, prijavaPacijenta, datumVakcinacije, vakcina, doza));
				if(nextidKartona<idKartona)
					nextidKartona=idKartona;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public VakcinalniKarton findOne(Integer idKartona) {
		return vakcinalniKartonPacijanta.get(idKartona);
	}
	public List<VakcinalniKarton> findAll() {
		return new ArrayList<VakcinalniKarton>(vakcinalniKartonPacijanta.values());
	}
	
	public VakcinalniKarton save(VakcinalniKarton vakcinalniKarton) {
		if (vakcinalniKarton.getIdKartona() == 0) {
			vakcinalniKarton.setIdKartona(++nextidKartona);
		}
		vakcinalniKartonPacijanta.put(vakcinalniKarton.getIdKartona(), vakcinalniKarton);
		return vakcinalniKarton;
	}
	
	public List<VakcinalniKarton> save(List<VakcinalniKarton> vakcinalniKarton) {
		List<VakcinalniKarton> ret = new ArrayList<>();

		for (VakcinalniKarton vk : vakcinalniKarton) {
			VakcinalniKarton saved = save(vk);
			if (saved != null) {
				ret.add(saved);
			}
		}
		return ret;
	}
	

}
