package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.WeatherReportDAO;

@WebServlet("/generateList")
public class generateList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		WeatherReportDAO weatherReportDAO = new WeatherReportDAO();
		try {
			List<String> locationList = weatherReportDAO.getLocationName();			
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<locationList.size();i++) {
				sb.append("<option value=\'").append(locationList.get(i)).append("'>").append(locationList.get(i)).append("</option>");
			}
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(sb.toString());
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
