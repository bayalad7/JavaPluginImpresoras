package javapluginimpresoras;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImpresorasImprimirEnHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Tipo de peticion y headers
        ServidorHttp.HttpPeticionHeaders(httpExchange);

        if( ServidorHttp.METHOD_POST.equals( ServidorHttp.PETICION_TIPO ) ){
            // Parametros POST
            InputStream is = httpExchange.getRequestBody();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, ServidorHttp.CHARSET ));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) { // br.readLine is always null!!!
                content.append(line);
                content.append("\n");
            }
            System.out.println("Content: " + content.toString());

            // Respuesta de tipo 200
            ServidorHttp.RJsonCodigo = ServidorHttp.PETICION_ESTADO_OK;
            ServidorHttp.RJsonEstado = "Ok";
            ServidorHttp.RJsonMensaje = "El tipo de petición es correcta";
            ServidorHttp.RJson = "{ \"laRespuesta\": { \"elCodigo\": "+ServidorHttp.RJsonCodigo+", \"elEstado\": \""+ServidorHttp.RJsonEstado+"\", \"elMensaje\": \""+ServidorHttp.RJsonMensaje+"\" }}";
        }
        else{
            // Respuesta de tipo 405
            ServidorHttp.RJsonCodigo = ServidorHttp.PETICION_ESTADO_ERROR;
            ServidorHttp.RJsonEstado = "Error";
            ServidorHttp.RJsonMensaje = "El tipo de petición ["+ServidorHttp.PETICION_TIPO+"] es incorrecta para este método";
            ServidorHttp.RJson = "{ \"laRespuesta\": { \"elCodigo\": "+ServidorHttp.RJsonCodigo+", \"elEstado\": \""+ServidorHttp.RJsonEstado+"\", \"elMensaje\": \""+ServidorHttp.RJsonMensaje+"\" }}";
        }

        // Respuesta al cliente
        ServidorHttp.HttpResponse(httpExchange);
    }
}
