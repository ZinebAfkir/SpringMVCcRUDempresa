
package com.example.services;

import java.util.List;

import com.example.entities.Empleado;

public interface EmpleadoService {
    
  
   
     public List<Empleado> findAll(); 
     public Empleado findById(int idEmpleado); 
     public void save(Empleado estudiante); 
     public void deleteById(int idEmpleado);
     public void delete(Empleado empleado);
     /**
      * El metodo update no es necesario porq el "save" inserta o actualiza en dependencia de que el idEstudiante exista o no, es 
        decir si no existe lo crea y si existe actualiza la informacion 
      */
}
