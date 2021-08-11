package javapluginimpresoras;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class ImpresorasImprimirEnHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Respuestas
        ServidorHttp.Respuestas laRespuesta = new ServidorHttp.Respuestas();
        // Tipo de peticion y headers
        ServidorHttp.HttpPeticionHeaders(httpExchange);

        if( ServidorHttp.METHOD_POST.equals( ServidorHttp.PETICION_TIPO ) ){
            // Parametros POST - Json Recibido
            String JsonRequest = ServidorHttp.HttpRequestBodyGetJson( httpExchange ); // System.out.println("Contenido Json: " + JsonRequest );
            JsonObject elJsonObject = new Gson().fromJson(JsonRequest, JsonObject.class);
            //System.out.println( "JsonRequest: " + JsonRequest );
            System.out.println( "elJsonObject: " + elJsonObject );

            // Validaciones del Json
            // ¿Es un Json válido?
            if( !elJsonObject.isJsonObject() ){
                laRespuesta.setCodigo(ServidorHttp.PETICION_ESTADO_ERROR);
                laRespuesta.setEstado("Error");
                laRespuesta.setMensaje("Objeto Json no válido");
            }
            else{
                // ¿Es un Json vacío?
                if( elJsonObject.keySet().isEmpty() ){
                    laRespuesta.setCodigo(ServidorHttp.PETICION_ESTADO_ERROR);
                    laRespuesta.setEstado("Error");
                    laRespuesta.setMensaje("Objeto Json vacío");
                }
                else{
                    // Respuesta de tipo 200
                    laRespuesta.setCodigo(ServidorHttp.PETICION_ESTADO_OK);
                    laRespuesta.setEstado("Ok");
                    laRespuesta.setMensaje("El tipo de petición es correcta");
                }
            }
        }
        else{
            // Respuesta de tipo 403
            laRespuesta.setCodigo(ServidorHttp.PETICION_ESTADO_ERROR);
            laRespuesta.setEstado("Error");
            laRespuesta.setMensaje("El tipo de petición ["+ServidorHttp.PETICION_TIPO+"] es incorrecta para este método");
        }

        // Respuesta al cliente
        ServidorHttp.HttpResponse(httpExchange, laRespuesta);
    }
}
