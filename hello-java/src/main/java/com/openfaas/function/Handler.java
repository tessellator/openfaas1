package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Handler implements com.openfaas.model.IHandler {

    public String exMessage(Exception  e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public String getResource() {
        String r;

        URL u = getClass().getResource("/res_file.xml");
        if ( u == null) {
            r = "getResource = NULL\n";
        }
        else {
            r = "getRessource = " + u.toString() + "\n";

            try {
                InputStream is = u.openConnection().getInputStream();
                r  += new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));
            }
            catch (Exception e) {
                r += exMessage(e);
            }
        }

        return r;
    }

    public String getDirect() {
        String path = "jar:file:/home/app/entrypoint-1.0/lib/function-1.0.jar!/res_file.xml";
        File file = new File(path);

        return file.getName();
    }

    public String getFileList() throws IOException {
        // reference: https://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file
        String details = "";

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

       if(jarFile.isFile()) {
           final JarFile jar = new JarFile(jarFile);
           final Enumeration<JarEntry> entries = jar.entries();
           while(entries.hasMoreElements()) {
               final String name = entries.nextElement().getName();
               details += name + "\n";
           }
           jar.close();
       }

       return details;
    }

    public String getFileListDirect() {
        String details = "";

        File folder = new File("/home/app/entrypoint-1.0/lib/function-1.0.jar!/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                details += "File " + listOfFiles[i].getName();
            } else if (listOfFiles[i].isDirectory()) {
                details += "Directory " + listOfFiles[i].getName();
            }
        }

        return details;
    }

    public IResponse Handle(IRequest req) {
        String r = "";

        try {
            r += "GET RESOURCE:\n";
            r += getResource() + "\n\n";

            r += "DIRECT:\n";
            r += getDirect() + "\n\n";

            r += "FILE LIST:\n";
            r += getFileList() + "\n\n";

            r += "FILE LIST DIRECT:\n";
            r += getFileListDirect() + "\n\n";
        }
        catch(Exception e) {
            r += exMessage(e);
        }

        Response res = new Response();
        res.setBody(r);
        return res;
    }
}
