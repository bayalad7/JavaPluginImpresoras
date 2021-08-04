package javapluginimpresoras;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.ArrayList;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class ImpresorasListadoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("::: ImpresorasListadoHandler :::");
        System.out.println("Request: getRequestMethod: " + httpExchange.getRequestMethod());
        
        // Cabeceras
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, authorization");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        headers.add("Content-Type", "application/json");
        // final String Origin = headers.getFirst("Origin");
        // System.out.println("Request: Origin: " + Origin);

        // Impresoras: Obtener el listado de las impresoras.
        ArrayList<String> lasImpresoras = obtenerListaDeImpresoras();

        // Parseo de Parametros.
        
        // Respuesta
        ServidorHttp.RJsonCodigo = 0;
        ServidorHttp.RJsonEstado = "PT-OK";
        ServidorHttp.RJsonMensaje = "Mensaje de la petición";
        ServidorHttp.RJson = "{ \"laRespuesta\": { \"elCodigo\": "+ServidorHttp.RJsonCodigo+", \"elEstado\": \""+ServidorHttp.RJsonEstado+"\", \"elMensaje\": \""+ServidorHttp.RJsonMensaje+"\", \"lasImpresoras\": "+lasImpresoras+" } }";

        try{
            byte[] RJsonBytes = ServidorHttp.RJson.getBytes("UTF-8");
            httpExchange.sendResponseHeaders(ServidorHttp.RPeticionCodigo, RJsonBytes.length);
            httpExchange.getResponseBody().write(RJsonBytes);
            httpExchange.getResponseBody().close();
        }
        catch (IOException e) {
            System.out.println("ImpresorasListadoHandler: IOException: " + e.getMessage());
        }
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
