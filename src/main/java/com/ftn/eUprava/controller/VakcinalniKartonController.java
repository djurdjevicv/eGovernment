package com.ftn.eUprava.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ftn.eUprava.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.eUprava.model.Korisnik;
import com.ftn.eUprava.model.PrijavaZaVakcinaciju;
import com.ftn.eUprava.model.VakcinalniKarton;
import com.ftn.eUprava.model.VakcinalniKartonPacijenta;
import com.ftn.eUprava.service.VakcinalniKartonService;
import com.ftn.eUprava.service.impl.PrijavaZaVakcinacijuServiceImpl;
import com.ftn.eUprava.service.impl.VakcinalniKartonServiceImpl;

@Controller
@RequestMapping(value="/vakcinalniKartoni")
public class VakcinalniKartonController implements ServletContextAware{


	public static final String VAKCINALNI_KARTON_KEY = "vakcinalniKarton";
	public static final String KORISNICI_KEY = "korisnici";
	
	@Autowired
	private ServletContext servletContext;
	private  String bURL; 
	
	@Autowired
	private VakcinalniKartonServiceImpl vakcinalniKartonService;
	
	@Autowired
	private PrijavaZaVakcinacijuServiceImpl prijaveZaVakcinaciju;
	
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
		List<VakcinalniKarton> listaKartona = vakcinalniKartonService.findAll();
		List<PrijavaZaVakcinaciju> listaPrijava = prijaveZaVakcinaciju.findAll();
		
		
		if(listaKartona.isEmpty()){
			for (int i=0; i < listaPrijava.size(); i++) {
				retVal.append("<!DOCTYPE html>\r\n"
						+ "<html>\r\n"
						+ "<head>\r\n"
						+ "	<meta charset=\"UTF-8\">\r\n"
						+ "	<base href=\"/eUprava/\">\r\n"
						+ "	<title>Vakcinalni karton</title>\r\n"
						+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
						+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+"<table>\r\n"
						+ "<caption> Vakcinalni karton - " + listaPrijava.get(i).getImePacijenta() + " " + listaPrijava.get(i).getPrezimePacijenta()
						+ " JMBG: " + listaPrijava.get(i).getJmbgPacijenta() +"</caption>\r\n"
						+ "<tr>\r\n"
						+ "<th>Prijava izvrsena</th>\r\n"
						+ "<th>Vakcinisan</th>\r\n"
						+ "<th>Vakcina</th>\r\n"
						+ "<th>Doza</th>\r\n"
						+ "</tr>\r\n"
						+ "<tr>\r\n"
						+ "<td>"+ listaPrijava.get(i).getPrijavaPacijenta() +"</td>\r\n"
						+ "<td></td>\r\n"
						+ "<td>"+ listaPrijava.get(i).getVakcina() +"</td>\r\n"
						+ "<td>"+ listaPrijava.get(i).getDoza() +"</td>\r\n"
						+ "</tr> <br><br>\r\n"
						+"</table>\r\n"
						+ "</body>\r\n"
						+ "</html>");
				}
		}else {
			for (int i=0; i < listaKartona.size(); i++) {
				retVal.append("<!DOCTYPE html>\r\n"
						+ "<html>\r\n"
						+ "<head>\r\n"
						+ "	<meta charset=\"UTF-8\">\r\n"
						+ "	<base href=\"/eUprava/\">\r\n"
						+ "	<title>Vakcinalni karton</title>\r\n"
						+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
						+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+"<table>\r\n"
						+ "<caption> Vakcinalni karton - " + listaKartona.get(i).getImePacijenta() + " " + listaKartona.get(i).getPrezimePacijenta()
						+ " JMBG: " + listaKartona.get(i).getJmbgPacijenta() +"</caption>\r\n"
						+ "<tr>\r\n"
						+ "<th>ID kartona</th>\r\n"
						+ "<th>Prijava izvrsena</th>\r\n"
						+ "<th>Vakcinisan</th>\r\n"
						+ "<th>Vakcina</th>\r\n"
						+ "<th>Doza</th>\r\n"
						+ "</tr>\r\n"
						+ "<tr>\r\n"
						+ "<td>"+ listaKartona.get(i).getIdKartona() +"</td>\r\n"
						+ "<td>"+ listaKartona.get(i).getPrijavaPacijenta() +"</td>\r\n"
						+ "<td>"+ listaKartona.get(i).getDatumVakcinacije() +"</td>\r\n"
						+ "<td>"+ listaKartona.get(i).getVakcina() +"</td>\r\n"
						+ "<td>"+ listaKartona.get(i).getDoza() +"</td>\r\n"
						+ "</tr> <br><br>\r\n"
						+"</table>\r\n"
						+ "</body>\r\n"
						+ "</html>");
				}
		}

		
		
		return retVal.toString();
	}
	
	@GetMapping(value = "/vakcinalniKarton")
	@ResponseBody
	public String index(@RequestParam Long jmbg, HttpServletRequest request, HttpServletResponse response) {
		
		PrijavaZaVakcinaciju prijava = prijaveZaVakcinaciju.findOne(jmbg);
		List<VakcinalniKarton> listaKartona = vakcinalniKartonService.findAll();
		List<VakcinalniKarton> kartoniPacijenta = vakcinalniKartonService.nadjiSveKartonePacijenta(listaKartona, jmbg);
		StringBuilder retVal = new StringBuilder();
		
		
		if(kartoniPacijenta.isEmpty() == true && prijava != null) {
			retVal.append("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/eUprava/\">\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "	<title>Vakcinalni karton</title>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<table>\r\n"
					+ "<caption> Vakcinalni karton - " + prijava.getImePacijenta() + " " + prijava.getPrezimePacijenta()
					+ " JMBG: " + prijava.getJmbgPacijenta() +"</caption>\r\n"
					+ "<tr>\r\n"
					+ "<th>Prijava izvrsena</th>\r\n"
					+ "<th>Vakcinisan</th>\r\n"
					+ "<th>Vakcina</th>\r\n"
					+ "<th>Doza</th>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td>"+ prijava.getPrijavaPacijenta() +"</td>\r\n"
					+ "<td></td>\r\n"
					+ "<td>"+ prijava.getVakcina() +"</td>\r\n"
					+ "<td>"+ prijava.getDoza() +"</td>\r\n"
					+ "</tr>\r\n"
					+"</table>\r\n"
					+"</body>\r\n"
					+ "</html>");

		}else if(prijava != null && kartoniPacijenta.isEmpty() == false && prijava.getVakcinisan() == false){
			retVal.append("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/eUprava/\">\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "	<title>Vakcinalni karton</title>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<table>\r\n"
					+ "<caption> Vakcinalni karton - " + kartoniPacijenta.get(0).getImePacijenta() + " " + kartoniPacijenta.get(0).getPrezimePacijenta()
					+ " JMBG: " + kartoniPacijenta.get(0).getJmbgPacijenta() +"</caption>\r\n"
					+ "<tr>\r\n"
						+ "<th>Prijava izvrsena</th>\r\n"
						+ "<th>Vakcinisan</th>\r\n"
						+ "<th>Vakcina</th>\r\n"
						+ "<th>Doza</th>\r\n"
					+ "</tr>\r\n");

			for(int i =0; i < kartoniPacijenta.size() ; i++) {
				retVal.append(
						"<tr>\r\n"
							+"<td>"+ kartoniPacijenta.get(i).getPrijavaPacijenta() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getDatumVakcinacije() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getVakcina() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getDoza() +"</td>\r\n"
						+ "</tr>\r\n");
			}
			retVal.append(					
					"<tr>\r\n"
					+"<td>"+ prijava.getPrijavaPacijenta() +"</td>\r\n"
					+ "<td></td>\r\n"
					+ "<td>"+ prijava.getVakcina() +"</td>\r\n"
					+ "<td>"+ prijava.getDoza() +"</td>\r\n"
					+ "</tr>\r\n");

				retVal.append(
						"</table>\r\n");
				retVal.append(
						"</body>\r\n"
						+ "</html>");
		} else if(kartoniPacijenta.isEmpty() == false) {
			retVal.append("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/eUprava/\">\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "	<title>Vakcinalni karton</title>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<table>\r\n"
					+ "<caption> Vakcinalni karton - " + kartoniPacijenta.get(0).getImePacijenta() + " " + kartoniPacijenta.get(0).getPrezimePacijenta()
					+ " JMBG: " + kartoniPacijenta.get(0).getJmbgPacijenta() +"</caption>\r\n"
					+ "<tr>\r\n"
						+ "<th>Prijava izvrsena</th>\r\n"
						+ "<th>Vakcinisan</th>\r\n"
						+ "<th>Vakcina</th>\r\n"
						+ "<th>Doza</th>\r\n"
					+ "</tr>\r\n");

			for(int i =0; i < kartoniPacijenta.size() ; i++) {
				retVal.append(
						"<tr>\r\n"
							+"<td>"+ kartoniPacijenta.get(i).getPrijavaPacijenta() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getDatumVakcinacije() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getVakcina() +"</td>\r\n"
							+ "<td>"+ kartoniPacijenta.get(i).getDoza() +"</td>\r\n"
						+ "</tr>\r\n");
			}
				retVal.append(
						"</table>\r\n");
				retVal.append(
						"</body>\r\n"
						+ "</html>");
			
		} else if(prijava == null && kartoniPacijenta.isEmpty() == true){
			retVal.append("<!DOCTYPE html>\r\n"
					+ "<html>\r\n"
					+ "<head>\r\n"
					+ "	<meta charset=\"UTF-8\">\r\n"
					+ "	<base href=\"/eUprava/\">\r\n"
					+ "	<title>Vakcinalni karton</title>\r\n"
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviTabela.css\"/>\r\n" 
					+ "	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/StiloviHorizontalniMeni.css\"/>\r\n"
					+ "</head>\r\n"
					+ "<body>\r\n"
					+ "<h1>Nije pronadjen karton korisnika<h1>"
					+ "</body>\r\n"
					+ "</html>");
		}
		return retVal.toString();
	}



}
