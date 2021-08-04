package javapluginimpresoras;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServidorHttp {

    // Respuestas Peticiones
    public static int RPeticionCodigo = 200 ;

    // Respuestas Json
    public static String RJson;
    public static int    RJsonCodigo;
    public static String RJsonEstado;
    public static String RJsonMensaje;

    public static void main(String[] args) throws IOException {
        final int elPuerto = 9000;
        HttpServer elServidor = HttpServer.create( new InetSocketAddress( "localhost" , elPuerto ) , 0);
        elServidor.createContext("/Api", new ServerRootHandler() );
        elServidor.createContext("/Api/Estado", new ApiEstadoHandler() );
        elServidor.createContext("/Api/Impresoras/Listado"   , new ImpresorasListadoHandler() );
        elServidor.createContext("/Api/Impresoras/ImprimirEn", new ImpresorasImprimirEnHandler() );
        elServidor.setExecutor(null);
        elServidor.start();
    }
}
