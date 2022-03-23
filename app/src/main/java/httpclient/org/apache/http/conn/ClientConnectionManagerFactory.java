package httpclient.org.apache.http.conn;


import httpclient.org.apache.http.ClientConnectionManager;
import httpclient.org.apache.http.conn.scheme.SchemeRegistry;
import httpcore.org.apache.http.params.HttpParams;

@Deprecated
public interface ClientConnectionManagerFactory {

    ClientConnectionManager newInstance(
            HttpParams params,
            SchemeRegistry schemeRegistry);

}
