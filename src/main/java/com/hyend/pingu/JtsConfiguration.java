package com.hyend.pingu;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JtsConfiguration {

    @Value("${jts.srid}")
    private int srid;

    @Bean
    public GeometryFactory getGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), srid);
    }
}
