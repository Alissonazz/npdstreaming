package com.npd.npdstreaming;

import com.npd.npdstreaming.model.SeriesData;
import com.npd.npdstreaming.service.ApiConsumption;
import com.npd.npdstreaming.service.DataConvert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NpdstreamingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NpdstreamingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var ApiConsumption = new ApiConsumption();
		var json = ApiConsumption.obtainData("https://www.omdbapi.com/?t=gilmore+girls&apikey=8e6340f0");
		System.out.println(json);
		DataConvert convert = new DataConvert();
		SeriesData data = convert.obtainData(json, SeriesData.class);
		System.out.println(data);
	}
}
