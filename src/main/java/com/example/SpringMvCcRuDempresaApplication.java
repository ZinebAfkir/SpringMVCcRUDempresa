package com.example;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.entities.Empleado;
import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Telefono;
import com.example.entities.Empleado.Genero;
import com.example.services.EmpleadoService;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.TelefonoService;

@SpringBootApplication
public class SpringMvCcRuDempresaApplication implements CommandLineRunner {

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private TelefonoService telefonoService;

    @Autowired
    private CorreoService correoService;

    public static void main(String[] args) {
        SpringApplication.run(SpringMvCcRuDempresaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        /**
         * Método para agregar registros de muestra para departamento (crear y añadir
         * departamentoes),
         * Empleado (crear y añadir empleados) y Telefono (crear y añadir
         * telefonos):
         */

        departamentoService.save(Departamento.builder().nombre("LENGUA").build());
        departamentoService.save(Departamento.builder().nombre("FISICA").build());

        empleadoService.save(Empleado.builder()
                .id(1) // hay que meterle el id a mano o no funciona
                .nombre("Elisabet")
                .apellidos("Agulló García")
                .fechaAlta(LocalDate.of(2008, Month.APRIL, 2))
                .genero(Genero.MUJER)

                .departamento(departamentoService.findById(1))
                .build());

        telefonoService.save(Telefono.builder()
                .id(1)
                .numero("968700900")
                .empleado(empleadoService.findById(1))
                .build());

        telefonoService.save(Telefono.builder()
                .id(2)
                .numero("611234900")
                .empleado(empleadoService.findById(1))
                .build());

                correoService.save(Correo.builder()
                .id(2)
                .email("zinebaf96@gmail.com")
                .empleado(empleadoService.findById(1))
                .build());
    }
}
