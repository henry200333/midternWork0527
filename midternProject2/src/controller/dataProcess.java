package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.WeatherReportBean;
import model.WeatherReportDAO;

@WebServlet("/dataProcess")
public class dataProcess extends HttpServlet {
	enum Action {
		FindAll, FindOne, InsertData, UpdateData, DeleteData, FindByLocation
	}

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WeatherReportDAO weatherReportDAO = new WeatherReportDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
		sdf.setLenient(false);
		String date = null;
		String time = null;
		String location = null;
		String dateTime = null;
		Timestamp dateTime2 = null;
		String temperature = null;
		Integer temperature2 = null;
		WeatherReportBean bean = null;
		WeatherReportBean beanExist = null;
		StringBuilder sb = null;
		HashMap<String, String> errorMsg = new HashMap<String, String>();
		request.setAttribute("errorMsg", errorMsg);
		Action action = Action.valueOf(request.getParameter("action"));
		switch (action) {
		case FindAll:
			sb = new StringBuilder();
			try {
				List<WeatherReportBean> all = weatherReportDAO.getAll();
				sb.append("[");
				for (WeatherReportBean bean1 : all) {		
					sb.append(bean1.toString()).append(",");
				}
				sb.delete(sb.length()-1,sb.length());
				sb.append("]");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(sb.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case FindOne:
			response.setCharacterEncoding("UTF-8");
			location = request.getParameter("location");
			location = new String(location.getBytes("ISO-8859-1"), "UTF-8");
			date = request.getParameter("date");
			time = request.getParameter("time");
			if (date != null && time != null && date.trim() != "" && time.trim() != "") {
				dateTime = date.trim() + time.trim();
				try {
					dateTime2 = new Timestamp(sdf.parse(dateTime).getTime());
					try {
						WeatherReportBean result = weatherReportDAO.findByLocationameNDatetime(location, dateTime2);					
						if (result != null) {
							
							sb = new StringBuilder();
							sb.append("[");	
							sb.append(result.toString()).append("]");							
							response.setCharacterEncoding("UTF-8");
							response.getWriter().print(sb.toString());
							break;
						} else {
							response.getWriter().print("查無資料");
						}

					} catch (SQLException e) {
						e.printStackTrace();
						response.getWriter().print("查無資料");
					}
				} catch (ParseException e) {
					e.printStackTrace();
					response.getWriter().print("日期無法解析");
				}
			}else {
				response.getWriter().print("日期空白，查無資料");
			}
			break;
		case InsertData:
		case UpdateData:
			location = request.getParameter("location");
			location = new String(location.getBytes("ISO-8859-1"), "UTF-8");
			request.setAttribute("location", location);
			date = request.getParameter("date");
			time = request.getParameter("time");
			if (date != null && time != null && date.trim() != "" && time.trim() != "") {
				dateTime = date.trim() + time.trim();
				try {
					dateTime2 = new Timestamp(sdf.parse(dateTime).getTime());
					request.setAttribute("date", date.trim());
					request.setAttribute("time", time.trim());
				} catch (ParseException e) {
					errorMsg.put("date", "日期有誤");
					e.printStackTrace();
				}
			} else {
				errorMsg.put("date", "不可空白");
			}
			temperature = request.getParameter("temperature");
			if (temperature != null && temperature != "") {
				try {
					temperature2 = Integer.valueOf(temperature);
					request.setAttribute("temperature", temperature);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					errorMsg.put("temperature", "輸入溫度無法解析");
				}
			} else {
				errorMsg.put("temperature", "溫度不可為空白");
			}

			if (!errorMsg.isEmpty()) {
				RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
				rd.forward(request, response);
			} else {
				try {
					beanExist = weatherReportDAO.findByLocationameNDatetime(location, dateTime2);
					if (action.toString() == "InsertData") {
						if (beanExist == null) {
							HttpSession session1 = request.getSession();
							bean = weatherReportDAO.getLatNLon(location);
							bean.setDateTime(dateTime2);
							bean.setValue(temperature2.shortValue());
							weatherReportDAO.insert(bean);
							session1.setAttribute("UpdateBean", bean);
							response.sendRedirect("Success.jsp");
						} else {
							errorMsg.put("existData", "資料已存在，請使用更新功能");
							RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
							rd.forward(request, response);
						}
					} else if (action.toString() == "UpdateData") {
						if (!(beanExist == null)) {
							HttpSession session1 = request.getSession();
							beanExist.setDateTime(dateTime2);
							beanExist.setValue(temperature2.shortValue());
							weatherReportDAO.update(beanExist);
							session1.setAttribute("UpdateBean", beanExist);
							response.sendRedirect("Success.jsp");
						} else {
							errorMsg.put("existData", "資料不存在，請使用新增功能");
							RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
							rd.forward(request, response);
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
					errorMsg.put("existData", "資料值異常，不要鬧");
					RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
					rd.forward(request, response);
				}
			}
			break;

		case DeleteData:
			location = request.getParameter("location");
			location = new String(location.getBytes("ISO-8859-1"), "UTF-8");

			request.setAttribute("location", location);
			date = request.getParameter("date");
			time = request.getParameter("time");
			if (date != null && time != null && date.trim() != "" && time.trim() != "") {
				dateTime = date.trim() + time.trim();
				try {
					dateTime2 = new Timestamp(sdf.parse(dateTime).getTime());
					request.setAttribute("date", date.trim());
					request.setAttribute("time", time.trim());
				} catch (ParseException e) {
					errorMsg.put("date", "日期有誤");
					e.printStackTrace();
				}
			} else {
				errorMsg.put("date", "不可空白");
			}

			if (!errorMsg.isEmpty()) {
				RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
				rd.forward(request, response);
			} else {
				try {
					beanExist = weatherReportDAO.findByLocationameNDatetime(location, dateTime2);
					if (!(beanExist == null)) {
						weatherReportDAO.delete(location, dateTime2);
						HttpSession session1 = request.getSession();
						session1.setAttribute("UpdateBean", beanExist);
						response.sendRedirect("Success.jsp");
					} else {
						errorMsg.put("existData", "資料不存在");
						RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
						rd.forward(request, response);
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			break;
		case FindByLocation:		
			sb = new StringBuilder();
			response.setCharacterEncoding("UTF-8");
			location = request.getParameter("location");
			location = new String(location.getBytes("ISO-8859-1"), "UTF-8");
			try {
				List<WeatherReportBean> all = weatherReportDAO.getByLocation(location);
				sb.append("[");
				for (WeatherReportBean bean1 : all) {
					sb.append(bean1.toString()).append(",");
				}
				sb.delete(sb.length()-1,sb.length());
				sb.append("]");
				response.getWriter().print(sb.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				response.getWriter().print("發生錯誤");
			}
			break;
			
		default:
			RequestDispatcher rd = request.getRequestDispatcher("Form.jsp");
			rd.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}