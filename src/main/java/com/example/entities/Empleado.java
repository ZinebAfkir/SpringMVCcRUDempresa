package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleados") //nombre de mi tabla en mysql
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class Empleado implements Serializable { 
    private static final long serialVersionUID = 1L;
  
    // las tablas tienen que tener id y para que ese id de dichas tablas sea autoincremental y Foreign key hacemos lo siguiente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @NotNull(message = "El nombre no puede ser null")
    
    private String nombre;
    private String apellidos;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaAlta;
    private Genero genero;
   
    public enum Genero{
        HOMBRE,MUJER,OTRO
    }

    @ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.PERSIST)//muchos a uno, es decir muchos empleados pertenecen 
    //a un departamento
    private Departamento departamento;  //Un departamento muchos empleados 

    //Tiene que ser remove porq antes de borrar el padre tengo que borrar los hijos 
    //cuando borre el estudiante ya me elimine los telefonos 
    
     @OneToMany (fetch = FetchType.LAZY, cascade =CascadeType.REMOVE, mappedBy = "empleado" ) //uno a muchos, es decir 
     //un empleado puede tener muchos telefonos y muchos correos
    
    private List<Telefono> telefonos; 

    //Antes de eliminar un empleado primero tengo que eliminar sus hijos que son los correos
    // y telefonos
    @OneToMany (fetch = FetchType.LAZY, cascade =CascadeType.REMOVE, mappedBy = "empleado" ) 

    private List<Correo> correos;

    
}

