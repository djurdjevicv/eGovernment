package com.ftn.eUprava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;

public class PrijavaZaVakcinaciju {

	private Long jmbgPacijenta;
	private String imePacijenta;
	private String prezimePacijenta;
	private LocalDateTime prijavaPacijenta;
	private Vakcine vakcina;
	private Doze doza;
	private Boolean vakcinisan;
	
	public PrijavaZaVakcinaciju() {}
	
	public PrijavaZaVakcinaciju(Long jmbgPacijenta, String imePacijenta, String prezimePacijenta, LocalDateTime prijavaPacijenta
			, Vakcine vakcina, Doze doza,Boolean vakcinisan) {
		super();
		this.jmbgPacijenta = jmbgPacijenta;
		this.imePacijenta = imePacijenta;
		this.prezimePacijenta = prezimePacijenta;
		this.prijavaPacijenta = (prijavaPacijenta);
		
		this.vakcina =vakcina;
		this.doza = doza;
		this.setVakcinisan(vakcinisan);
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
	
	public Boolean getVakcinisan() {
		return vakcinisan;
	}

	public void setVakcinisan(Boolean vakcinisan) {
		this.vakcinisan = vakcinisan;
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Override
    public String toString() {
        return this.getJmbgPacijenta() + ";" + this.getImePacijenta() + ";" + this.getPrezimePacijenta() + ";" + this.getPrijavaPacijenta().format(formatter) + ";" + this.getVakcina() + ";" + this.getDoza() + ";" + this.getVakcinisan() + "\n";
    }
	
    public String pripremiZaSnimanjePrijavu(){
        return jmbgPacijenta + ";" + imePacijenta + ";" + prezimePacijenta + ";" + prijavaPacijenta.format(formatter) + ";" + vakcina + ";" + doza + ";" + vakcinisan + "\n";
    }


}
