package com.globalaccelerex.nipmiddleware.config;

import com.globalaccelerex.nipmiddleware.institution.SLSConfig;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import static com.globalaccelerex.nipmiddleware.api.NipAPI.*;

@EnableWs
@Configuration
public class WebServiceConfig {

    private SLSConfig slsConfig;

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        val servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, URL_MAPPINGS);
    }

    @Bean(name = "nipInward")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema nipInwardSchema) {
        val wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(PORT_TYPE_NAME);
        wsdl11Definition.setLocationUri(INWARD_WS_URI + slsConfig.getInstitutionCode() + "/nipInward.wsdl");
        wsdl11Definition.setTargetNamespace(INWARD_TARGET_NAMESPACE);
        wsdl11Definition.setSchema(nipInwardSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema nipInwardSchema(){
        return new SimpleXsdSchema(new ClassPathResource("xsd/nip.xsd"));
    }

    @Autowired
    public void setSlsConfig(SLSConfig slsConfig) {
        this.slsConfig = slsConfig;
    }
}
