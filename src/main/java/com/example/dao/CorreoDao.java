
package com.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Correo;
import com.example.entities.Empleado;

@Repository
public interface CorreoDao extends JpaRepository <Correo,Integer> { //Interfaz de la clase Correo
    
    

    long deleteByEmpleado(Empleado empleado);
    


    /**Es necesario crear un método que encuentre los telefonso de cada empleado: */
    List<Correo> findByEmpleado(Empleado empleado);
}


