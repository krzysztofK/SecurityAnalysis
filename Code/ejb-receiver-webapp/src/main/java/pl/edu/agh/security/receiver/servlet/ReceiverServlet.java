package pl.edu.agh.security.receiver.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.edu.agh.security.receiver.ejb.interfaces.IReceiver;

@WebServlet("/receiver")
@RunAs("magister")
public class ReceiverServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@EJB
	private IReceiver receiver;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter responseStream = response.getWriter();
		responseStream.println("Unprotected method call is ");
		try {
			responseStream.println(receiver.receiveUnprotected());
		} catch (EJBAccessException ejbAccessException) {
			ejbAccessException.printStackTrace(responseStream);
		}

		responseStream
				.println("----------------------------------------------------------------------");

		responseStream.println("Protected method call is ");
		try {
			responseStream.println(receiver.receive());
		} catch (EJBAccessException ejbAccessException) {
			ejbAccessException.printStackTrace(responseStream);
		}

		responseStream
				.println("----------------------------------------------------------------------");

		responseStream.println("Protected method call for other role is ");
		try {
			responseStream.println(receiver.receiveForOtherRole());
		} catch (EJBAccessException ejbAccessException) {
			ejbAccessException.printStackTrace(responseStream);
		}

		responseStream
				.println("----------------------------------------------------------------------");

		responseStream.println("Forbidden method call is ");
		try {
			responseStream.println(receiver.receiveForbiddenMessage());
		} catch (EJBAccessException ejbAccessException) {
			ejbAccessException.printStackTrace(responseStream);
		}

		responseStream.close();
	}

}
