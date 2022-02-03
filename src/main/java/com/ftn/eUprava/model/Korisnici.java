package com.ftn.eUprava.model;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Korisnici {
	
	private Map<Long, Korisnik> korisnici = new HashMap<>();
	private long nextJmbg = 1L;
	
	public Korisnici() {
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("korisnici.txt").toURI());
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				String[] tokens = line.split(";");
				Long jmbg = Long.parseLong(tokens[0]);
				String ime = tokens[1];
				String prezime = tokens[2];
				String lozinka = tokens[3];
				String tipKorisnika = tokens[4];
				
				korisnici.put(Long.parseLong(tokens[0]), new Korisnik(jmbg, ime, prezime, lozinka, tipKorisnika));
				if(nextJmbg<jmbg)
					nextJmbg=jmbg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	public Korisnik findOne(Long jmbg) {
		return korisnici.get(jmbg);
	}

	public List<Korisnik> findAll() {
		return new ArrayList<Korisnik>(korisnici.values());
	}
	
}
