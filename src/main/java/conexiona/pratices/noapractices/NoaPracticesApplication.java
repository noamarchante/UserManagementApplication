//@SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan con sus configuraciones por defecto
	//@Configuration: Esta anotación se utiliza para indicar que la clase puede contener beans que serán registrados al iniciar la aplicación.
	//@EnableAutoConfiguration: Con esta anotación se le indica a Spring que se encargue de configurar todas las dependencias que tengamos en el proyecto.
	//@ComponentScan: Permite que se escaneen todos los @Component que se encuentre dentro del paquete en el que se define, se puede configurar para que busque en los paquetes que queramos.

package conexiona.pratices.noapractices;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class NoaPracticesApplication {

    public static void main(String[] args) {

        SpringApplication.run(NoaPracticesApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer(){
        //Enable SSL Trafic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){

            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");

                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");

                securityConstraint.addCollection(collection);

                context.addConstraint(securityConstraint);
            }
        };

        //Add HTTP to HTTPS redirect
        tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());
        return tomcat;
    }

    //Redirecciona HTTTP a HTTPS, sin SSL pasa por el puerto 8080 per con SSL pasa por el puerto 8443
    private Connector httpToHttpsRedirectConnector(){
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
}
