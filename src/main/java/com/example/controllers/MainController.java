package com.example.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Empleado;

import com.example.entities.Telefono;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.EmpleadoService;

import com.example.services.TelefonoService;


//En la capa de controller  no hace falta un request y un response esto se resume en lo siguiente

@Controller
@RequestMapping("/") 
public class MainController { 
    public static final Logger LOG = Logger.getLogger("MainController");

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private TelefonoService telefonoService;

    @Autowired
    private CorreoService correoService;


   //El siguiente metodo devuelve un listado de empleados
    @GetMapping("/listar") 
    public ModelAndView listar(){ 
       
        List<Empleado> empleados = empleadoService.findAll();
        
        ModelAndView mav = new ModelAndView("views/listarEmpelado");
        
        mav.addObject("empleados",empleados);
        return mav; 
    }

    /**
     * Muestra el formulario de alta de empleado
     */

     @GetMapping("/frmAltaEst") // aqui es el nombre de la url que va a resoponder y le damos el nombre que quieras no tiene porq ser igual que el nombre de abajo 
     public String formularioAltaEmpleado(Model model){


     List<Departamento> departamentos = departamentoService.findAll();
     Empleado empleado = new Empleado();

        model.addAttribute("empleado",empleado);
        model.addAttribute("departamentos", departamentos);

        return "views/FormularioAltaEmpleado";

     }




        /**
         * Metodo que recibe los datos procedentes de los controladores del formulario 
         *
         */

         @PostMapping("/altaModificacionEmpleado") //post es para que lo que envie este dentro del protocolo es decir no lo puede ver todo el mundo
        public String altaEmpleado(@ModelAttribute Empleado empleado,
                      @RequestParam(name ="numerosTelefonos") String telefonosRecibidos, 
                      @RequestParam(name ="emailsCorreos") String correosRecibidos){


            LOG.info("Telefonos recibidos: " + telefonosRecibidos);
 
            empleadoService.save(empleado); //guarada el empleado en la bbdd empleado


            List<String> listadoNumerosTelefonos = null;

            if(telefonosRecibidos != null) {
            String[] arrayTelefonos = telefonosRecibidos.split(";"); 
         
            listadoNumerosTelefonos = Arrays.asList(arrayTelefonos);
            }

  
             if(listadoNumerosTelefonos!= null) {
                telefonoService.deleteByEmpleado(empleado);
                listadoNumerosTelefonos.stream().forEach(n->{
                     Telefono telefonoObject = Telefono.builder().numero(n).empleado(empleado).build();

                     telefonoService.save(telefonoObject);
                });
                
             }
            
             LOG.info("Correos recibidos: " + correosRecibidos);

            empleadoService.save(empleado);

            List<String> listadoMailsCorreo = null;
            if (correosRecibidos != null) {
                String[] arrayCorreos = correosRecibidos.split(";");

                listadoMailsCorreo = Arrays.asList(arrayCorreos);
            }

            if (listadoMailsCorreo != null) {
                correoService.deleteByEmpleado(empleado);
                listadoMailsCorreo.stream().forEach(n -> {
                    Correo correoObject = Correo.builder()
                            .email(n)
                            .empleado(empleado)
                            .build();

                    correoService.save(correoObject);
                });

            }

        
            return "redirect:/listar"; // es a la url a cual nos llevara despues de haber rellenado el formulario del estudiante 
           //lo anterior me redirecta a la url o pagina listar 
        }
     


/*
 * Metodo para actualizar los datos de un empleado dado su id
 */

       @GetMapping("/fmrActualizar/{id}")
       public String frmactualizaEmpleado(@PathVariable(name ="id") int idEmpleado, // este metodo le paso el id de un estudiante
                                         Model model){ 

        Empleado empleado= empleadoService.findById(idEmpleado);
        
        List<Telefono> todosTelefonos = telefonoService.findAll(); // findAll devuelve una lista de todos telefonos 
        
    
        List<Telefono> telefonosDelEstudiante = todosTelefonos.stream().filter(t->t.getEmpleado().getId() == idEmpleado)
        .collect(Collectors.toList()); 
    
        String numerosDeTelefono = telefonosDelEstudiante.stream().map(t->t.getNumero()).collect(Collectors.joining(";"));
        
        model.addAttribute("empleado", empleado);
        model.addAttribute("telefonos", numerosDeTelefono);

        //Correos 

        List<Correo> todosCorreos = correoService.findAll();



       List<Correo> correosEmpleado = todosCorreos
       .stream()
       .filter(correo -> correo.getEmpleado().getId() == idEmpleado)
       .collect(Collectors.toList());



        String emailsDecorreo = correosEmpleado.stream().map(t -> t.getEmail())
               .collect(Collectors.joining(";"));



        model.addAttribute("correos", correosEmpleado);


        List<Departamento> departamentos =departamentoService.findAll();
        model.addAttribute("departamentos", departamentos);

        return "views/FormularioAltaEmpleado";

       }

       @GetMapping("/borrar/{id}")
       public String borrarEstudiante(@PathVariable(name="id") int idEmpleado){

        empleadoService.delete(empleadoService.findById(idEmpleado));
         
         return "redirect:/listar";

       }

       /**
     * Métodoque encuentre los telefonso de cada estudiante: (hecho por nosotras):
     */
    @GetMapping("/detalles/{id}")
    public String detallesEmpelado(@PathVariable(name = "id") int id, Model model) {

        Empleado empleado = empleadoService.findById(id);

        List<Telefono> telefonos = telefonoService.findByEmpleado(empleado);
        List<String> numerosTelefono = telefonos.stream().map(t -> t.getNumero()).toList();

        List<Correo> correos = correoService.findByEmpleado(empleado);
        List<String> emailsCorreo = correos.stream().map(t -> t.getEmail()).toList();

        model.addAttribute("telefonos", numerosTelefono);
        model.addAttribute("correos", emailsCorreo);
        model.addAttribute("empleado", empleado);

       
        return "views/detalleEmpleado";
    }
}