package pl.edu.agh.security.servlet.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class SimpleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String getCurrentDateAndTime() {
        Date currentDate = new Date();
        DateFormat dateAndTime = SimpleDateFormat.getDateTimeInstance();
        return dateAndTime.format(currentDate);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String currentDateString = getCurrentDateAndTime();
        PrintWriter responseStream = response.getWriter();
        responseStream.println("Current time is: ");
        responseStream.println(currentDateString);
        
        responseStream.println("Request: ");
        for(Enumeration<String> headers = request.getHeaderNames(); headers.hasMoreElements();) {
            String headerName = headers.nextElement();
            responseStream.println(headerName + ":" + request.getHeader(headerName));
        }
        BufferedReader requestReader = request.getReader();
        String requestLine = requestReader.readLine();
        while(requestLine != null) {
            responseStream.println(requestLine);
            requestLine = requestReader.readLine();
        }
        responseStream.println(request);
        responseStream.close();
    }
    
}
