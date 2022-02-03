package com.ftn.eUprava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;

public class VakcinalniKarton {

	private int idKartona;
	private Long jmbgPacijenta;
	private String imePacijenta;
	private String prezimePacijenta;
	private LocalDateTime prijavaPacijenta;
	private LocalDateTime datumVakcinacije;
	private Vakcine vakcina;
	private Doze doza;
	
	public VakcinalniKarton() {}
	
	public VakcinalniKarton(int idKartona, Long jmbgPacijenta, String imePacijenta, String prezimePacijenta, LocalDateTime prijavaPacijenta, LocalDateTime datumVakcinacije, Vakcine vakcina, Doze doza) {
		this.idKartona = idKartona;
		this.jmbgPacijenta = jmbgPacijenta;
		this.imePacijenta = imePacijenta;
		this.prezimePacijenta = prezimePacijenta;
		this.prijavaPacijenta = prijavaPacijenta;
		this.setDatumVakcinacije(datumVakcinacije);
		this.vakcina = vakcina;
		this.doza = doza;
	}
	
	public String getImePacijenta() {
		return imePacijenta;
	}
	public void setImePacijenta(String imePacijenta) {
		this.imePacijenta = imePacijenta;
	}
	public String getPrezimePacijenta() {
		return prezimePacijenta;
	}
	public void setPrezimePacijenta(String prezimePacijenta) {
		this.prezimePacijenta = prezimePacijenta;
	}
	public Long getJmbgPacijenta() {
		return jmbgPacijenta;
	}
	public void setJmbgPacijenta(Long jmbgPacijenta) {
		this.jmbgPacijenta = jmbgPacijenta;
	}
	public LocalDateTime getPrijavaPacijenta() {
		return prijavaPacijenta;
	}
	public void setPrijavaPacijenta(LocalDateTime prijavaPacijenta) {
		this.prijavaPacijenta = prijavaPacijenta;
	}

	public Vakcine getVakcina() {
		return vakcina;
	}
	public void setVakcina(Vakcine vakcina) {
		this.vakcina = vakcina;
	}
	public Doze getDoza() {
		return doza;
	}
	public void setDoza(Doze doza) {
		this.doza = doza;
	}
	
	public LocalDateTime getDatumVakcinacije() {
		return datumVakcinacije;
	}

	public void setDatumVakcinacije(LocalDateTime datumVakcinacije) {
		this.datumVakcinacije = datumVakcinacije;
	}
	
	public int getIdKartona() {
		return idKartona;
	}

	public void setIdKartona(int idKartona) {
		this.idKartona = idKartona;
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Override
    public String toString() {
        return this.getIdKartona() + ";" + this.getJmbgPacijenta() + ";" + this.getImePacijenta() + ";" + this.getPrezimePacijenta() + ";" + this.getPrijavaPacijenta().format(formatter)+ ";" + this.getDatumVakcinacije().format(formatter)  + ";" + this.getVakcina() + ";" + this.getDoza()  + "\n";
    }


}
