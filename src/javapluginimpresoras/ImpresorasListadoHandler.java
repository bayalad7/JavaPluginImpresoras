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
        // Tipo de peticion y headers
        ServidorHttp.HttpPeticionHeaders(httpExchange);
        
        if( ServidorHttp.METHOD_GET.equals( ServidorHttp.PETICION_TIPO ) ){
            // Impresoras: Obtener el listado de las impresoras.
            ArrayList<String> lasImpresoras = obtenerListaDeImpresoras();

            // Respuesta de tipo 200
            ServidorHttp.RJsonCodigo = ServidorHttp.PETICION_ESTADO_OK;
            ServidorHttp.RJsonEstado = "Ok";
            ServidorHttp.RJsonMensaje = "El listado de las impresoras se cargo correctamente";
            ServidorHttp.RJson = "{ \"laRespuesta\": { \"elCodigo\": "+ServidorHttp.RJsonCodigo+", \"elEstado\": \""+ServidorHttp.RJsonEstado+"\", \"elMensaje\": \""+ServidorHttp.RJsonMensaje+"\", \"lasImpresoras\": "+lasImpresoras+" }}";
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

    public static ArrayList<String> obtenerListaDeImpresoras()
    {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        ArrayList<String> lasImpresoras = new ArrayList<>();
        // Recorremos las impresoras.
        for (PrintService printer : printServices)
        {
            lasImpresoras.add( '"'+ printer.getName() +'"' );
        }
        return lasImpresoras;
    }
}
