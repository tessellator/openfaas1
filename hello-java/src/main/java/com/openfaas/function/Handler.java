package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Handler implements com.openfaas.model.IHandler {

    public IResponse Handle(IRequest req) {

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
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                r += sw.toString();
            }
        }

        Response res = new Response();
        res.setBody(r);
	    return res;
    }
}


