package com.ftn.eUprava.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.eUprava.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.eUprava.enumi.Doze;
import com.ftn.eUprava.enumi.Vakcine;
import com.ftn.eUprava.model.Korisnici;
import com.ftn.eUprava.model.Korisnik;
import com.ftn.eUprava.model.PrijavaZaVakcinaciju;
import com.ftn.eUprava.model.PrijaveZaVakcinaciju;
import com.ftn.eUprava.model.VakcinalniKarton;
import com.ftn.eUprava.model.VakcinalniKartonPacijenta;
import com.ftn.eUprava.service.PrijavaZaVakcinacijuService;
import com.ftn.eUprava.service.impl.PrijavaZaVakcinacijuServiceImpl;
import com.ftn.eUprava.service.impl.VakcinalniKartonServiceImpl;

@Controller
@RequestMapping(value="/svePrijaveZaVakcinaciju")
public class SvePrijaveZaVakcinacijuController implements ServletContextAware{

	public static final String SVE_PRIJAVE_ZA_VAKCINACIJU_KEY = "svePrijaveZaVakcinaciju";
	public static final String KORISNICI_KEY = "korisnici";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private PrijavaZaVakcinacijuServiceImpl prijavaService;
	
	@Autowired
	private VakcinalniKartonServiceImpl vakcinalniKartonService;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";

	}
	
	@GetMapping
	@ResponseBody
	public String index() {
		
		StringBuilder retVal = new StringBuilder();
		List<PrijavaZaVakcinaciju> listaPrijava = prijavaService.findAll();
				retVal.append(
				"<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"	<meta charset=\"UTF-8\">\r\n" + 
	    		"	<base href=\""+bURL+"\">" + 
				"	<title>Prijave za vakcinaciju</title>\r\n" + 
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" + 
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"+
				"</head>\r\n" + 
				"<body> "+
				"		<table>\r\n" + 
				"			<caption>Sve prijave za vakcinaciju</caption>\r\n" + 
				"			<tr>\r\n" + 
				"				<th>Ime</th>\r\n" + 
				"				<th>Prezime</th>\r\n" + 
				"				<th>Jmbg</th>\r\n" +
				"				<th>Prijava kreirana</th>\r\n" +
				"				<th>Vakcina</th>\r\n" +
				"				<th>Doza</th>\r\n" +
				"				<th></th>\r\n" +
				"				<th></th>\r\n" +
				"			</tr>\r\n");  

		for (int i=0; i < listaPrijava.size(); i++) {
			if(listaPrijava.get(i).getVakcinisan() == false) {
				retVal.append(
					"	<tr>\r\n" + 
					"		<td>"+ listaPrijava.get(i).getImePacijenta() +"</td>\r\n" +
					"		<td>"+ listaPrijava.get(i).getPrezimePacijenta() +"</td>\r\n" +
					"		<td>"+ listaPrijava.get(i).getJmbgPacijenta() +"</td>\r\n" +
					"		<td>"+ listaPrijava.get(i).getPrijavaPacijenta() +"</td>\r\n" +
					"		<td>"+ listaPrijava.get(i).getVakcina() +"</td>\r\n" +
					"		<td>"+ listaPrijava.get(i).getDoza() +"</td>\r\n" +
					"		<td><a href=\"vakcinalniKartoni/vakcinalniKarton?jmbg="+listaPrijava.get(i).getJmbgPacijenta()+"\">Pogledaj vakcinalni karton</a></td>\r\n" +
					"			<td>" + 
					"				<form method=\"post\" action=\"svePrijaveZaVakcinaciju/vakcinisi\">\r\n" + 
					"					<input type=\"hidden\" name=\"jmbg\" value=\""+listaPrijava.get(i).getJmbgPacijenta()+"\">\r\n" + 
					"					<input type=\"submit\" value=\"Vakcinisi\"></td>\r\n" + 
					"				</form>\r\n" +
					"				</td>" +
					"	</tr>\r\n");
			}
		}
		retVal.append(
				"		</table>\r\n");
		retVal.append(
				"		<ul>\r\n" +
				"			<li><a href=\"korisnici/logout\">Odjavi se</a></li>\r\n" +
				"			<li><a href=\"vakcinalniKartoni\">Pogledaj sve kartone</a></li>\r\n" +
				"		</ul>\r\n" +
				"</body>\r\n"+
				"</html>\r\n");		
		return retVal.toString();
	}
	
	@GetMapping(value="/add")
	@ResponseBody
	public String create(HttpServletRequest request) {
		
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNICI_KEY);
		
		PrijavaZaVakcinaciju prijava = prijavaService.findOne(korisnik.getJmbg()); 
		List<PrijavaZaVakcinaciju> listaPrijava = prijavaService.findAll(); 
		
		StringBuilder retVal = new StringBuilder();
			retVal.append("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\""+bURL+"\">"
					+ "	<title>Prijava za vakcinaciju</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "    <h1>Prijava za vakcinaciju</h1>\r\n"
					+ "		<table>\r\n"  
					+ "			<caption>Tvoje prijave za vakcinaciju</caption>\r\n" 
					+ "			<tr>\r\n" 
					+ "				<th>Ime</th>\r\n" 
					+ "				<th>Prezime</th>\r\n" 
					+ "				<th>Jmbg</th>\r\n" 
					+ "				<th>Prijava kreirana</th>\r\n"
					+ "				<th>Vakcina</th>\r\n" 
					+ "				<th>Doza</th>\r\n" 
					+ "				<th></th>\r\n" 
					+ "			</tr>\r\n");  

				if(prijava == null) {
					retVal.append(
							"	<tr>\r\n" + 
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" + 
							"	</tr>\r\n");
				}else if(prijava.getVakcinisan() == false){
					retVal.append(
							"	<tr>\r\n" + 
							"		<td>"+ prijava.getImePacijenta() +"</td>\r\n" +
							"		<td>"+ prijava.getPrezimePacijenta() +"</td>\r\n" +
							"		<td>"+ prijava.getJmbgPacijenta() +"</td>\r\n" +
							"		<td>"+ prijava.getPrijavaPacijenta() +"</td>\r\n" +
							"		<td>"+ prijava.getVakcina() +"</td>\r\n" +
							"		<td>"+ prijava.getDoza() +"</td>\r\n" +
							"		<td><a href=\"svePrijaveZaVakcinaciju/edit\">Izmeni</a></td>\r\n" +
							"	</tr>\r\n");
				}else {
					retVal.append(
							"	<tr>\r\n" + 
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" +
							"		<td></td>\r\n" + 
							"	</tr>\r\n");
				}
			retVal.append(
					"		</table>\r\n<br><br>");
			retVal.append(
					  "    <form method=\"post\" action=\"svePrijaveZaVakcinaciju/add\">\r\n"
					+ "        <label>Vakcina: </label>\r\n"
					+ "        <select name=\"vakcina\">\r\n"
					+ "            <option value=\"Pfizer\">Pfizer</option>\r\n"
					+ "            <option value=\"SputnikV\">SputnikV</option>\r\n"
					+ "            <option value=\"AstraZeneca\">AstraZeneca</option>\r\n"
					+ "        </select>\r\n<br>"
					+ "\r\n"
					+ "        <label>Doza: </label>\r\n"
					+ "        <select name=\"doza\">\r\n"
					+ "            <option value=\"Doza1\">Doza 1</option>\r\n"
					+ "            <option value=\"Doza2\">Doza 2</option>\r\n"
					+ "            <option value=\"Doza3\">Doza 3</option>\r\n"
					+ "        </select>\r\n<br>"
					+ "    <input type=\"submit\" value=\"Prijavi se za vakcinaciju\">\r\n"
					+ "    </form>\r\n"
					+ "    <br>\r\n"
					
					+ "		<ul>\r\n"
					+"			<li><a href=\"vakcinalniKartoni/vakcinalniKarton?jmbg=" + korisnik.getJmbg() + "\">Pogledaj vakcinalni karton</a></li>\r\n"
					+"			<li><a href=\"korisnici/logout\">Odjavi se</a></li>\r\n"
					+ "		</ul>\r\n"
					+ "</body>\r\n"
					+ "</html>");
		

		return retVal.toString();
	}
	
	
	@PostMapping(value= "/add")
	public void create(@RequestParam Vakcine vakcina,@RequestParam Doze doza, HttpServletResponse response, HttpSession session, HttpServletRequest request)throws IOException {
			
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNICI_KEY);
		PrijavaZaVakcinaciju prijavaZaVakcinaciju = prijavaService.findOne(korisnik.getJmbg());
		VakcinalniKarton vakcinalniKarton = vakcinalniKartonService.findOne(korisnik.getJmbg());
		List<VakcinalniKarton> listaKartona = vakcinalniKartonService.findAll();
		List<VakcinalniKarton> kartoniPacijenta = vakcinalniKartonService.nadjiSveKartonePacijenta(listaKartona, korisnik.getJmbg());

		String greska = "";
		Long jmbgPacijenta = korisnik.getJmbg();
		String imePacijenta = korisnik.getIme();
		String prezimePacijenta = korisnik.getPrezime();
		LocalDateTime prijavaPacijenta = LocalDateTime.now();
		Boolean vakcinisan = false;
		
		int count = 0;
		for(VakcinalniKarton kartoni : kartoniPacijenta) {
			count++;
		}
				
		if(prijavaZaVakcinaciju != null) {
			if(prijavaZaVakcinaciju.getVakcinisan() == false) {
				greska = "Morate sacekati da se vakcinisete!";
			}
		}
		
		if(kartoniPacijenta.isEmpty() == true) {
			if(doza.equals(Doze.Doza2) || doza.equals(Doze.Doza3)) {
				greska = "Niste primili ni jednu dozu, morate se prijaviti za dozu 1!";
			}
		}
		
		for(int i = 0; i < kartoniPacijenta.size(); i++) {
			if(kartoniPacijenta.get(i).getDoza() == doza) {
				greska = "Ovu dozu ste vec primili: " + doza;
			}
		}
		
		if(!greska.equals("")) {
			PrintWriter out = response.getWriter();
			out.write(greska);
			return;
		}
		
		
		if(count == 0 && doza == Doze.Doza1) {
			if(greska.equals("")) {
				PrijavaZaVakcinaciju prijava = new PrijavaZaVakcinaciju(jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, vakcina, doza, vakcinisan);
				PrijavaZaVakcinaciju saved = prijavaService.save(prijava);
				response.sendRedirect(bURL+"svePrijaveZaVakcinaciju/add");
			}
		}else if(count == 1 && doza == Doze.Doza2) {
			if(greska.equals("")) {
				PrijavaZaVakcinaciju prijava = new PrijavaZaVakcinaciju(jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, vakcina, doza, vakcinisan);
				PrijavaZaVakcinaciju saved = prijavaService.save(prijava);
				response.sendRedirect(bURL+"svePrijaveZaVakcinaciju/add");
			}
		}else if(count == 2 && doza == Doze.Doza3) {
			if(greska.equals("")) {
				PrijavaZaVakcinaciju prijava = new PrijavaZaVakcinaciju(jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, vakcina, doza, vakcinisan);
				PrijavaZaVakcinaciju saved = prijavaService.save(prijava);
				response.sendRedirect(bURL+"svePrijaveZaVakcinaciju/add");
			}
		}else {
			PrintWriter out = response.getWriter();
			out.write("Morate doze redom primati!");
			return;
		}
		
		//PROVERA KOJU VAKCINU JE PRIMIO KORISNIK
//		for(int i = 0; i < kartoniPacijenta.size(); i++) {
//			if(kartoniPacijenta.get(i).getVakcina() != vakcina) {
//				greska = "Mozete primiti samo vakcinu: " + kartoniPacijenta.get(0).getVakcina();
//			}
//		}
	}
	
	

	@PostMapping(value="/vakcinisi")
	public void vakcinisi(@RequestParam Long jmbg , HttpServletResponse response) throws IOException {	

		PrijavaZaVakcinaciju prijava = prijavaService.findOne(jmbg);

		List<VakcinalniKarton> listaKartona = vakcinalniKartonService.findAll();
		
        int idKartona = vakcinalniKartonService.generisiNoviId(listaKartona);
		
		String greska = "";
		
		Long jmbgPacijenta = prijava.getJmbgPacijenta();
		String imePacijenta = prijava.getImePacijenta();
		String prezimePacijenta = prijava.getPrezimePacijenta();
		LocalDateTime prijavaPacijenta = prijava.getPrijavaPacijenta();
		LocalDateTime datumVakcinacije = LocalDateTime.now();
		Vakcine vakcina = prijava.getVakcina();
		Doze doza = prijava.getDoza();
				
		
		if(prijava != null) {
			prijava.setVakcinisan(true);

			VakcinalniKarton noviKarton = new VakcinalniKarton(idKartona, jmbgPacijenta, imePacijenta, prezimePacijenta, prijavaPacijenta, datumVakcinacije, vakcina, doza);
			PrijavaZaVakcinaciju saved = prijavaService.save(prijava);
			VakcinalniKarton savedKarton = vakcinalniKartonService.save(noviKarton);
			response.sendRedirect(bURL+"svePrijaveZaVakcinaciju");
		}else {
			greska = "Vakcinisanje pacijenta nije uspelo";
		}
		
		if(!greska.equals("")) {
			PrintWriter out = response.getWriter();
			out.write(greska);
			return;
		}

	}
	
	@GetMapping(value = "/edit")
	@ResponseBody
	public String edit(HttpServletRequest request) {
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNICI_KEY);
		PrijavaZaVakcinaciju prijava = prijavaService.findOne(korisnik.getJmbg());
		
		StringBuilder retVal = new StringBuilder();
		retVal.append("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "	<meta charset=\"UTF-8\">\r\n"
				+ "	<base href=\""+bURL+"\">"
				+ "	<title>Prijava za vakcinaciju</title>\r\n"
				+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
				+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "    <h1>Izmeni svoju prijavu</h1>\r\n<br>"
				+ "		<table>\r\n"  
				+ "		<caption>Prijava koju zelis da izmenis</caption>\r\n" 
				+ "			<tr>\r\n" 
				+ "				<th>Ime</th>\r\n" 
				+ "				<th>Prezime</th>\r\n" 
				+ "				<th>Jmbg</th>\r\n" 
				+ "				<th>Prijava kreirana</th>\r\n"
				+ "				<th>Vakcina</th>\r\n" 
				+ "				<th>Doza</th>\r\n"  
				+ "			</tr>\r\n"
				+"			<tr>\r\n" 
				+"				<td>"+ prijava.getImePacijenta() +"</td>\r\n" 
				+"				<td>"+ prijava.getPrezimePacijenta() +"</td>\r\n" 
				+"				<td>"+ prijava.getJmbgPacijenta() +"</td>\r\n" 
				+"				<td>"+ prijava.getPrijavaPacijenta() +"</td>\r\n" 
				+"				<td>"+ prijava.getVakcina() +"</td>\r\n" 
				+"				<td>"+ prijava.getDoza() +"</td>\r\n" 
				+"			</tr>\r\n"
				+ "		</table>\r\n<br><br>"  
				+  "    <form method=\"post\" action=\"svePrijaveZaVakcinaciju/edit\">\r\n"
				+ "        <label>Vakcina: </label>\r\n"
				+ "        <select name=\"vakcina\">\r\n"
				+ "            <option value=\"Pfizer\">Pfizer</option>\r\n"
				+ "            <option value=\"SputnikV\">SputnikV</option>\r\n"
				+ "            <option value=\"AstraZeneca\">AstraZeneca</option>\r\n"
				+ "        </select>\r\n<br>"
				+ "\r\n"
				+ "        <label>Doza: </label>\r\n"
				+ "        <select name=\"doza\">\r\n"
				+ "            <option value=\"Doza1\">"+ prijava.getDoza() +"</option>\r\n"
				+ "        </select>\r\n<br>"
				+ "    <input type=\"submit\" value=\"Izmeni\">\r\n"
				+ "    </form>\r\n"
				+ "    <br>\r\n"
				+ "		<ul>\r\n"
				+"			<li><a href=\"svePrijaveZaVakcinaciju/add\">Nazad</a></li>\r\n"
				+"			<li><a href=\"korisnici/logout\">Odjavi se</a></li>\r\n"
				+ "		</ul>\r\n"
				+ "</body>\r\n"
				+ "</html>");
		return retVal.toString();
	}
	
	@PostMapping(value= "/edit")
	public void edit(@RequestParam Vakcine vakcina,@RequestParam Doze doza, HttpServletResponse response, HttpSession session, HttpServletRequest request)throws IOException {
			
		Korisnik korisnik = (Korisnik) request.getSession().getAttribute(KORISNICI_KEY);
		PrijavaZaVakcinaciju prijavaZaVakcinaciju = prijavaService.findOne(korisnik.getJmbg());
		List<VakcinalniKarton> listaKartona = vakcinalniKartonService.findAll();
		List<VakcinalniKarton> kartoniPacijenta = vakcinalniKartonService.nadjiSveKartonePacijenta(listaKartona, korisnik.getJmbg());
		String greska = "";

		if(prijavaZaVakcinaciju.getVakcina() == vakcina) {
			greska = "Za ovu vakcinu ste se vec prijavili!"+ "Vakcina za koju ste se prijavili: " + vakcina;
		}
				
		if(!greska.equals("")) {
			PrintWriter out = response.getWriter();
			out.write(greska);
			return;
		}
		
		if(greska.equals("")) {
			prijavaZaVakcinaciju.setVakcina(vakcina);
			PrijavaZaVakcinaciju saved = prijavaService.save(prijavaZaVakcinaciju);
			response.sendRedirect(bURL+"svePrijaveZaVakcinaciju/add");
		}
				
	}
	



}
