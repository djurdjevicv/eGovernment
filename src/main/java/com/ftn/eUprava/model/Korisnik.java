package com.ftn.eUprava.model;

public class Korisnik {

	private Long jmbg;
	private String ime;
	private String prezime;
	private String lozinka;
	private String tipKorisnika;
	
	public Korisnik() {}
	
	public Korisnik(Long jmbg, String ime, String prezime, String lozinka, String tipKorisnika) {
		super();
		this.jmbg = jmbg;
		this.ime = ime;
		this.prezime = prezime;
		this.lozinka = lozinka;
		this.setTipKorisnika(tipKorisnika);
	}
	
	public Long getJmbg() {
		return jmbg;
	}
	public void setJmbg(Long jmbg) {
		this.jmbg = jmbg;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(String tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}
}
