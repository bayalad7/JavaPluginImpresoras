package javapluginimpresoras;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ServidorHttp {

    // Respuestas Peticiones
    protected static final int PETICION_ESTADO_OK = 200;
    protected static final int PETICION_ESTADO_ERROR = 405;
    protected static String PETICION_TIPO = "";

    // Respuestas Json
    protected static String RJson;
    protected static int RJsonCodigo;
    protected static String RJsonEstado;
    protected static String RJsonMensaje;
    protected static byte[] RJsonBytes;
    
    // Tipo de peticiones y headers
    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final Charset CHARSET = StandardCharsets.UTF_8;
    protected static final String HEADER_ALLOW = "Allow";
    protected static final String HEADER_CONTENT_TYPE = "Content-Type";
    protected static final String HEADER_CONTENT_TYPE_FORMAT = String.format( "application/json; charset=%s" , ServidorHttp.CHARSET);
    protected static final String HEADER_ALLOWED_METHODS = ServidorHttp.METHOD_GET + "," + ServidorHttp.METHOD_POST;

    public static void main(String[] args) throws IOException {
        final int elPuerto = 9000;
        HttpServer elServidor = HttpServer.create( new InetSocketAddress( "localhost" , elPuerto ) , 0);
        elServidor.createContext("/Api/", new ServerRootHandler() );
        elServidor.createContext("/Api/Estado/", new ApiEstadoHandler() );
        elServidor.createContext("/Api/Impresoras/Listado/"   , new ImpresorasListadoHandler() );
        elServidor.createContext("/Api/Impresoras/ImprimirEn/", new ImpresorasImprimirEnHandler() );
        elServidor.setExecutor(null);
        elServidor.start();
    }
    
    protected static void HttpPeticionHeaders(HttpExchange httpExchange){
        ServidorHttp.PETICION_TIPO = httpExchange.getRequestMethod().toUpperCase();
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, authorization");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", ServidorHttp.HEADER_ALLOWED_METHODS);
        httpExchange.getResponseHeaders().add(ServidorHttp.HEADER_CONTENT_TYPE, ServidorHttp.HEADER_CONTENT_TYPE_FORMAT);
    }
    
    protected static void HttpResponse(HttpExchange httpExchange) throws IOException{
        try{
            httpExchange.getResponseHeaders().set(ServidorHttp.HEADER_CONTENT_TYPE, ServidorHttp.HEADER_CONTENT_TYPE_FORMAT);
            httpExchange.sendResponseHeaders(ServidorHttp.PETICION_ESTADO_OK, ServidorHttp.RJson.getBytes(ServidorHttp.CHARSET).length);
            httpExchange.getResponseBody().write( ServidorHttp.RJson.getBytes(ServidorHttp.CHARSET) );
        }
        catch (IOException e) {
            /*Pendiente de controlar errores IOException*/
            System.out.println("ImpresorasListadoHandler: IOException: " + e.getMessage());
        }
        finally{
            httpExchange.getResponseBody().close();
            httpExchange.close();
        }
    }

}
