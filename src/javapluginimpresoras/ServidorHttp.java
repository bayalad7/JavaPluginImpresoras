package javapluginimpresoras;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHttp {

    // Respuestas Peticiones
    protected static final int PETICION_ESTADO_OK = 200;
    protected static final int PETICION_ESTADO_ERROR = 403;
    protected static String PETICION_TIPO = "";

    // Respuestas Json
    protected static class Respuestas{
        public int elCodigo;
        public String elEstado;
        public String elMensaje;
        public ArrayList<String> lasImpresoras;

        // elCodigo
        public int getCodigo(){ return elCodigo;}
        public void setCodigo(final int elCodigo){ this.elCodigo = elCodigo;}
        // elEstado
        public String getEstado(){ return elEstado; }
        public void setEstado(final String elEstado){ this.elEstado = elEstado; }
        // elMensaje
        public String getMensaje(){ return elMensaje; }
        public void setMensaje(final String elMensaje){ this.elMensaje = elMensaje; }

        // lasImpresoras
        public ArrayList<String> getListadoImpresoras(){ return lasImpresoras; }
        public void setListadoImpresoras(final ArrayList<String> lasImpresoras){ this.lasImpresoras = lasImpresoras; }
    }

    // Respuestas Json a String y Bytes
    protected static String RJson;
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
    
    protected static void HttpResponse(HttpExchange httpExchange, ServidorHttp.Respuestas laRespuesta) throws IOException{
        try{
            ServidorHttp.RJson = "{ \"laRespuesta\": " + new Gson().toJson(laRespuesta) + "}";
            // System.out.println( "ServidorHttp.RJson: " + ServidorHttp.RJson );
            httpExchange.getResponseHeaders().set(ServidorHttp.HEADER_CONTENT_TYPE, ServidorHttp.HEADER_CONTENT_TYPE_FORMAT);
            httpExchange.sendResponseHeaders(ServidorHttp.PETICION_ESTADO_OK, ServidorHttp.RJson.getBytes(ServidorHttp.CHARSET).length);
            httpExchange.getResponseBody().write( ServidorHttp.RJson.getBytes(ServidorHttp.CHARSET) );
        }
        catch (IOException ex) {
            Logger.getLogger(ServidorHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            httpExchange.getResponseBody().close();
            httpExchange.close();
        }
    }
    
    protected static final String HttpRequestBodyGetJson(HttpExchange httpExchange) throws IOException{
        String JsonRequest = "";
        try{
            String line;
            InputStream is = httpExchange.getRequestBody();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, ServidorHttp.CHARSET ));
            StringBuilder content = new StringBuilder();
            while ((line = br.readLine()) != null) { // br.readLine es siempre nula!
                content.append(line);
            }
            JsonRequest = content.toString();
            //System.out.println( "HttpRequestBodyGetJson: " + JsonRequest );
        }
        catch (IOException ex) {
            Logger.getLogger(ServidorHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JsonRequest;
    }
}
