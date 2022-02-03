package com.ftn.eUprava.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.eUprava.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.eUprava.model.Korisnici;
import com.ftn.eUprava.model.Korisnik;

@Controller
@RequestMapping(value="/korisnici")
public class KorisnikController implements ApplicationContextAware{

	public static final String KORISNICI_KEY = "korisnici";
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private ApplicationMemory memorijaAplikacije;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;  
	}
	
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
		
		memorijaAplikacije = applicationContext.getBean(ApplicationMemory.class);
		Korisnici korisnici = new Korisnici();
		memorijaAplikacije.put(KorisnikController.KORISNICI_KEY, korisnici);
	}
	
	@PostMapping(value = "/login")
	public void login(@RequestParam long jmbg, @RequestParam String sifra, HttpServletResponse response, HttpSession session) throws IOException {
		Korisnici korisnici = (Korisnici) memorijaAplikacije.get(KorisnikController.KORISNICI_KEY);
		
		if(korisnici == null) {
			korisnici = new Korisnici();
			memorijaAplikacije.put(KorisnikController.KORISNICI_KEY, korisnici);
		}
		
		List<Korisnik> korisniciList = korisnici.findAll();
		
		Korisnik ulogovanKorisnik = null;
		String greska = "";
		
		for(Korisnik korisnik : korisniciList) {
			if(korisnik.getJmbg() == jmbg && korisnik.getLozinka().equals(sifra)) {
				if(korisnik.getTipKorisnika().equals("radnik")) {
					ulogovanKorisnik = korisnik;
					session.setAttribute(KORISNICI_KEY, ulogovanKorisnik);
					response.sendRedirect(bURL + "svePrijaveZaVakcinaciju");
					break;
				}else {
					ulogovanKorisnik = korisnik;
					session.setAttribute(KORISNICI_KEY, ulogovanKorisnik);					
					response.sendRedirect(bURL + "svePrijaveZaVakcinaciju/add");
					break;
				}
			}
		}
		if(ulogovanKorisnik == null) {
			greska = "Ne postoji korisnik sa unetim podacima";
		}
		
		if(session.getAttribute(KORISNICI_KEY) != null) {
			greska = "Korisnik je vec prijavljen na sistem. Morate se prvo odjaviti";
		}
		if(!greska.equals("")) {
			PrintWriter out = response.getWriter();
			out.write(greska);
			return;
		}

	}
	
	@GetMapping(value="/logout")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNICI_KEY);
		
		request.getSession().removeAttribute(KORISNICI_KEY);
		request.getSession().invalidate();
		response.sendRedirect(bURL);
	}

}
