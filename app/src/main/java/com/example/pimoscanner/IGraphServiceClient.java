//package com.example.pimoscanner;
//
//import static java.lang.String.format;
//
//import com.azure.identity.ClientSecretCredential;
//import com.azure.identity.ClientSecretCredentialBuilder;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
//import com.microsoft.graph.models.User;
//import com.microsoft.graph.requests.GraphServiceClient;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.message.BasicNameValuePair;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class IGraphServiceClient {
//
//    private static final String UTF_8 = null ;
//    private static String reader;
//
//    public void getAuthProvider() {
//
//        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder ()
//                .clientId ( "fc69ca49-09f4-40c1-8d93-e7183c28542d" )
//                .clientSecret ( "fYpHebffLhlIjnJN0S9mNMaqA6yXCpZX/X8TplHKz7c=" )
//                .tenantId ( "ayamahtibaa.sharepoint.com" )
//                .build ();
//
//        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider ( clientSecretCredential );
//
//        final GraphServiceClient graphClient =
//                GraphServiceClient
//                        .builder ()
//                        .authenticationProvider ( tokenCredentialAuthProvider )
//                        .buildClient ();
//
//        final User me = graphClient.me ().buildRequest ().get ();
//
//    }
//
//
//    private String getAuthToken() throws IOException {
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost (format("https://login.microsoftonline.com/%s/oauth2/v2.0/token", "ayamahtibaa.sharepoint.com"));
//        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        request.addHeader("cache-control", "no-cache");
//        List<NameValuePair> nvps = new ArrayList<> ();
//        nvps.add(new BasicNameValuePair("client_id", "fc69ca49-09f4-40c1-8d93-e7183c28542d"));
//        nvps.add(new BasicNameValuePair("client_secret", "fYpHebffLhlIjnJN0S9mNMaqA6yXCpZX/X8TplHKz7c="));
//        nvps.add(new BasicNameValuePair("scope", "https://graph.microsoft.com/.default"));
//        nvps.add(new BasicNameValuePair ("grant_type", "client_credentials"));
//        request.setEntity(new UrlEncodedFormEntity (nvps, UTF_8));
//        HttpResponse response = client.execute(request);
//        final JsonNode jsonNode = reader.readTree(response.getEntity().getContent());
//        return jsonNode.get("access_token").textValue();
//    }
//}
