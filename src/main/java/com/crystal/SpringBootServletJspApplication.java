package com.crystal;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

@ServletComponentScan
@SpringBootApplication
public class SpringBootServletJspApplication {

	public static void main(String[] args) throws IOException, URISyntaxException {
		SpringApplication.run(SpringBootServletJspApplication.class, args);

		String url = "http://localhost:8080";

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            String os = System.getProperty("os.name").toLowerCase();

            try {
                if (os.contains("win")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("open " + url);
                } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                    Runtime.getRuntime().exec("xdg-open " + url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
	}

	
	

}
