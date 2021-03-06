package com.unitec.amigos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//// comentario de prueba
@RestController
@RequestMapping("/api")
public class ControladorUsuario {
    //Aqui va un metodo que representa cada uno de  los estados que vamos a transferir
    //es decir, va un GET, POST, PUT, y DELETE. como minimo

    //Aqui viene ya el uso de la inversion de control
    @Autowired RepositorioUsuario repoUsuario;



    //Implementamos el codfio para guardar un usuario en MongoDB
    @PostMapping("/usuario")
    public Estatus guardar(@RequestBody String json)throws Exception {
        //Primero leemos y convertimos el objeto json a objeto Java
        ObjectMapper mapper=new ObjectMapper();
        Usuario u=mapper.readValue(json, Usuario.class);
         //Este usuario lo guardamos en mongoDB
        repoUsuario.save(u);
        //Creamos un onjeto de tipo Estatus y este objeto lo retornamos al cliente (android o postman)
        Estatus estatus= new Estatus();
        estatus.setSuccess(true);
        estatus.setMensaje("Tu usuario se guardó con éxito!");
        return estatus;

    }
        @GetMapping("/usuario/{id}")
        public Usuario obtenerPorId (@PathVariable String id){
            //leemos un usuario con el metodo findById pasandole como argumento el id que queremos apoyandonos del
            //repoUsuario
            Usuario u = repoUsuario.findById(id).get();
            return u;
        }

        @GetMapping("/usuario")
        public List<Usuario> buscarTodos(){

        return repoUsuario.findAll();
        }
        //Metodo para actualizar
        @PutMapping("/usuario")
        public Estatus actualizar(@RequestBody String json)throws Exception{
            //Primero debemos verificar que exita, por lo tanto primero lo buscamos
            ObjectMapper mapper=new ObjectMapper();
            Usuario u=mapper.readValue(json, Usuario.class);
            Estatus e=new Estatus();
                if( repoUsuario.findById(u.getEmail()).isPresent()){
                    //lo volvemos a guardar
                    repoUsuario.save(u);
                    e.setMensaje("Este usuario se actualizo con exito");
                    e.setSuccess(true);
                }else{
                    e.setMensaje("Ese usuario no existe, no se actualizará");
                    e.setSuccess(false);
            }
            return e;
        }
        @DeleteMapping("/usuario/{id}")
         public Estatus borrar(@PathVariable String id){
            Estatus estatus = new Estatus();
            if (repoUsuario.findById(id).isPresent()) {
                repoUsuario.deleteById(id);
                estatus.setSuccess(true);
                estatus.setMensaje("Usuario borrado con exito");
            }else{
                estatus.setSuccess(false);
                estatus.setMensaje("Ese usuario no existe");
            }
         return  estatus;
        }
}
