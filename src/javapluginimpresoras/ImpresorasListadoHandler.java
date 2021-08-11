package javapluginimpresoras;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class ImpresorasListadoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Respuestas
        ServidorHttp.Respuestas laRespuesta = new ServidorHttp.Respuestas();
        // Tipo de peticion y headers
        ServidorHttp.HttpPeticionHeaders(httpExchange);
        
        if( ServidorHttp.METHOD_GET.equals( ServidorHttp.PETICION_TIPO ) ){
            // Respuesta de tipo 200
            laRespuesta.setCodigo(ServidorHttp.PETICION_ESTADO_OK);
            laRespuesta.setEstado("Ok");
            laRespuesta.setMensaje("El listado de las impresoras se cargo correctamente");
            laRespuesta.setListadoImpresoras( obtenerListaDeImpresoras() ); // Impresoras: Obtener el listado de las impresoras de la computadora.
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

    public static ArrayList<String> obtenerListaDeImpresoras()
    {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        ArrayList<String> lasImpresoras = new ArrayList<>();
        // Recorremos las impresoras.
        for (PrintService printer : printServices)
        {
            lasImpresoras.add( printer.getName() );
        }
        return lasImpresoras;
    }
}
